package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private int callsMade = 0;
    private final BreedFetcher fetcher;
    private final HashMap<String, List<String>> cachedBreeds = new HashMap<>();
    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) {
        if (!cachedBreeds.containsKey(breed)) {
            callsMade++;
            List<String> subTrees = new ArrayList<>();
            try {
                subTrees = fetcher.getSubBreeds(breed);
                cachedBreeds.put(breed, subTrees);
            } catch (BreedNotFoundException e) {
                return subTrees;
            }
        }
        return cachedBreeds.get(breed);
    }

    public int getCallsMade() {
        return callsMade;
    }
}