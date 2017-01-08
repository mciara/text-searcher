package com.mciara.textsearcher;

import java.util.*;
import java.util.stream.Collectors;

public class RankCalculator {
    private static final int MAX_RESULTS = 10;
    private static final double MAX_RANKING_SCALE = 100;

    public static Map<String, Long> calculate(Map<String, List<FileWordCounter>> index, Map<String, Long> searchWords, int searchWordCount) {
        Map<String, Long> foundWords = findWordsAndRankThem(index, searchWords);

        final double rankPerWord = MAX_RANKING_SCALE / searchWordCount;

        return sortByAndNormalizeRank(foundWords, rankPerWord);
    }

    private static Map<String, Long> sortByAndNormalizeRank(Map<String, Long> words, double rankPerWord) {
        return words.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(MAX_RESULTS)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> Math.round(e.getValue() * rankPerWord),
                        (v1, v2) -> v1,
                        LinkedHashMap::new));
    }

    private static Map<String, Long> findWordsAndRankThem(Map<String, List<FileWordCounter>> index, Map<String, Long> searchWords) {
        Map<String, Long> wordsWithRank = new HashMap<>();

        index.entrySet()
                .stream()
                .filter(es -> searchWords.containsKey(es.getKey()))
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .forEach(e -> {
                    long currentWordsMatched = Math.min(e.getWordCount(), searchWords.get(e.getWord()));
                    Long previousWordsMatched = wordsWithRank.getOrDefault(e.getFilename(), 0L);

                    wordsWithRank.put(e.getFilename(), currentWordsMatched + previousWordsMatched);
                });
        return wordsWithRank;
    }
}
