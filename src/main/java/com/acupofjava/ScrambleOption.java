package com.acupofjava;

import java.util.ArrayList;
import java.util.List;

public record ScrambleOption(String characters, List<String> allWords) {
    public String scramble(int i) throws ImpossiblePermutationException {
        if (i < 0)
            throw new IllegalArgumentException("Index must be non-negative!");
        List<String> permutations = generatePermutations();
        List<String> nonRealWordPermutations = permutations.stream().filter(w -> !allWords.contains(w)).toList();
        if (nonRealWordPermutations.size() == 0)
            throw new ImpossiblePermutationException("bad");
        int correctedIndex = i % nonRealWordPermutations.size();
        return nonRealWordPermutations.get(correctedIndex);
    }

    private List<String> generatePermutations() {
        List<String> result = new ArrayList<>();
        permute("", characters, result);
        return result;
    }

    private void permute(String prefix, String str, List<String> result) {
        int n = str.length();
        if (n == 0) {
            result.add(prefix);
        } else {
            for (int i = 0; i < n; i++) {
                permute(prefix + str.charAt(i), str.substring(0, i) + str.substring(i + 1, n), result);
            }
        }
    }
}
