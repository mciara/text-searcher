package com.mciara.textsearcher;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.mciara.textsearcher.TextSearcher.normalizeWord;

/**
 * Reads words from files and keeps them in a map.
 * Words are stored as keys and FileWordCounter objects are stored as values.
 */
public class WordIndexer {
    public Map<String, List<FileWordCounter>> index(Stream<Path> stream) {
        Stream<Map<String, FileWordCounter>> fileWords = stream
                .map(file -> {
                    try {
                        return readFile(file);
                    } catch (IOException e) {
                        return Collections.emptyMap();
                    }
                });

        return fileWords
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                ));
    }

    private Map<String, FileWordCounter> readFile(Path file) throws IOException {
        Map<String, FileWordCounter> indexedWords = new HashMap<>();

        try (Scanner s = new Scanner(new BufferedReader(new FileReader(file.toFile())))) {
            String fileName = file.getFileName().toString();

            while (s.hasNext()) {
                String word = s.next();
                String normalizedWord = normalizeWord(word);
                FileWordCounter count = indexedWords.containsKey(normalizedWord) ? indexedWords.get(normalizedWord).incrementWordCount() : new FileWordCounter(fileName, normalizedWord, 1);
                indexedWords.put(normalizedWord, count);
            }
        }
        return indexedWords;
    }
}
