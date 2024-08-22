package com.acupofjava;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record ScrambleOption(String characters, Set<String> allWords) {
    public String scramble(int seed) throws ImpossiblePermutationException {
        if (seed < 0)
            throw new IllegalArgumentException("Index must be non-negative!");
        Set<String> permutations = generatePermutations();
        Set<String> nonRealWordPermutations = permutations.stream().filter(w -> !allWords.contains(w))
                .collect(Collectors.toSet());
        if (nonRealWordPermutations.size() == 0)
            throw new ImpossiblePermutationException("bad");
        int correctedIndex = seed % nonRealWordPermutations.size();
        return (String) nonRealWordPermutations.toArray()[correctedIndex];
    }

    private Set<String> generatePermutations() {
        Set<String> result = new HashSet<>();
        permute("", characters, result);
        return result;
    }

    private void permute(String prefix, String str, Set<String> result) {
        int n = str.length();
        if (n == 0) {
            result.add(prefix);
        } else {
            for (int i = 0; i < n; i++) {
                permute(prefix + str.charAt(i), str.substring(0, i) + str.substring(i + 1, n), result);
            }
        }
    }

    public boolean matches(String userInputText) {
        return allWords.contains(userInputText);
    }
}
