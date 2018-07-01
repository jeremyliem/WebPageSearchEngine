package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import search.models.Webpage;

import java.net.URI;

/**
 * This class is responsible for computing how "relevant" any given document is
 * to a given search query.
 *
 * See the spec for more details.
 */
public class TfIdfAnalyzer {
    // This field must contain the IDF score for every single word in all
    // the documents.
    private IDictionary<String, Double> idfScores;
    // This field must contain the TF-IDF vector for each webpage you were given
    // in the constructor.
    //
    // We will use each webpage's page URI as a unique key.
    private IDictionary<URI, IDictionary<String, Double>> documentTfIdfVectors;

    // Feel free to add extra fields and helper methods.
    private IDictionary<URI, Double> normDocTfIdfVectors;

    public TfIdfAnalyzer(ISet<Webpage> webpages) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.

        this.idfScores = this.computeIdfScores(webpages);
        this.documentTfIdfVectors = this.computeAllDocumentTfIdfVectors(webpages);
        this.normDocTfIdfVectors = new ChainedHashDictionary<>();
        for (KVPair<URI, IDictionary<String, Double>> pair: documentTfIdfVectors) {
            normDocTfIdfVectors.put(pair.getKey(), norm(pair.getValue()));
        }
    }

    // Note: this method, strictly speaking, doesn't need to exist. However,
    // we've included it so we can add some unit tests to help verify that your
    // constructor correctly initializes your fields.
    public IDictionary<URI, IDictionary<String, Double>> getDocumentTfIdfVectors() {
        return this.documentTfIdfVectors;
    }

    // Note: these private methods are suggestions or hints on how to structure your
    // code. However, since they're private, you're not obligated to implement exactly
    // these methods: feel free to change or modify these methods however you want. The
    // important thing is that your 'computeRelevance' method ultimately returns the
    // correct answer in an efficient manner.

    /**
     * Return a dictionary mapping every single unique word found
     * in every single document to their IDF score.
     */
    private IDictionary<String, Double> computeIdfScores(ISet<Webpage> pages) {
        IDictionary<String, Double> result = new ChainedHashDictionary<String, Double>();
        IDictionary<String, Double> resultFinal = new ChainedHashDictionary<String, Double>();
        double numDoc = pages.size();
        for (Webpage page : pages) {
            ISet<String> words = this.computeUniqueWords(page.getWords());
            for (String word: words) {
                if (result.containsKey(word)) {
                    result.put(word, result.get(word) + 1);
                } else {
                    result.put(word, 1.0);
                }              
            }
        }
        for (KVPair<String,  Double> pair: result) {
            String key = pair.getKey();
            double value = Math.log(numDoc/pair.getValue());
            resultFinal.put(key, value);
        }
        return resultFinal;
    }

    /**
     * Returns a dictionary mapping every unique word found in the given list
     * to their term frequency (TF) score.
     *
     * The input list represents the words contained within a single document.
     */
    private IDictionary<String, Double> computeTfScores(IList<String> words) {
        IDictionary<String, Double> temp = new ChainedHashDictionary<String, Double>();
        double size = words.size();
        for (String word: words) {
            double value;
            if (temp.containsKey(word)) {
                value = (temp.get(word) * size) + 1;
            } else {
                value = 1;
            }
            temp.put(word, value/size);
        }
        return temp;
    }

    /**
     * See spec for more details on what this method should do.
     */
    private IDictionary<URI, IDictionary<String, Double>> computeAllDocumentTfIdfVectors(ISet<Webpage> pages) {
        // Hint: this method should use the idfScores field and
        // call the computeTfScores(...) method.
        IDictionary<URI, IDictionary<String, Double>> result = 
                new ChainedHashDictionary<URI, IDictionary<String, Double>>();
        for (Webpage web: pages) {
            IDictionary<String, Double> temp = new ChainedHashDictionary<String, Double>();
            IList<String> words = web.getWords();
            IDictionary<String, Double> tf = computeTfScores(words);  
            for (KVPair<String, Double> pair: tf) {
                temp.put(pair.getKey(), pair.getValue()*idfScores.get(pair.getKey()));   
            }
            result.put(web.getUri(), temp);
        }
        return result;
    }

    /**
     * Returns the cosine similarity between the TF-IDF vector for the given query and the
     * URI's document.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public Double computeRelevance(IList<String> query, URI pageUri) {
        // Note: The pseudocode we gave you is not very efficient. When implementing,
        // this method, you should:
        //
        // 1. Figure out what information can be precomputed in your constructor.
        //    Add a third field containing that information.
        //
        // 2. See if you can combine or merge one or more loops.
        IDictionary<String, Double> documentVector = this.documentTfIdfVectors.get(pageUri);
        IDictionary<String, Double> queryVector = this.computeQueryVector(query);
        double numerator = 0.0;
        double docWordScore;
        for (KVPair<String, Double> pair: queryVector) {
            String word = pair.getKey();
            if (documentVector.containsKey(word)) {
                docWordScore = documentVector.get(word);
            } else {
                docWordScore = 0.0;
            }
            double queryWordScore = queryVector.get(word);
            numerator += docWordScore * queryWordScore;
        }
        double denominator = this.normDocTfIdfVectors.get(pageUri) * norm(queryVector);
        if (denominator != 0.0) {
            return numerator/denominator;
        } else {
            return 0.0;
        }
    }
    
    private IDictionary<String, Double> computeQueryVector(IList<String> query) {
        IDictionary<String, Double> result = new ChainedHashDictionary<>();
        ISet<String> uniqueWords = this.computeUniqueWords(query);
        IDictionary<String, Double> tfScores = this.computeTfScores(query);
        for (String word : uniqueWords) {
            if (this.idfScores.containsKey(word)) {
                double score = this.idfScores.get(word) * tfScores.get(word);
                result.put(word, score);
            }
        }
        return result;
    }
   
    private ISet<String> computeUniqueWords(IList<String> words) {
        ISet<String> unique = new ChainedHashSet<>();
        for (String word : words) {
            unique.add(word);
        }
        return unique;
    }
    private double norm(IDictionary<String, Double> vector) {
        double output = 0.0;
        double score;
        for (KVPair<String, Double> pair: vector) {
            score = pair.getValue();
            output += score * score;
        }
        return Math.sqrt(output);
}
}
    