package com.acupofjava;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.random.RandomGenerator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Game {
    Map<String, Set<String>> words;
    Iterator<Entry<String, Set<String>>> challenges;
    Entry<String, Set<String>> currentScrambleOption;
    Hitpoints hp;

    boolean isFirstWord = true;
    final long startTimeMS = System.currentTimeMillis();

    long currentChallengeStartTimeMS = startTimeMS;
    private int hpLostThisWord;
    List<WordStat> completedChallenges = new ArrayList<>();
    private String lastScrambledWord;

    public Game(Map<String, Set<String>> words, int hp) {
        this.words = words;
        this.hp = new Hitpoints(hp);
        resetIterator();

        if (challenges.hasNext()) {
            currentScrambleOption = challenges.next();
        } else {
            throw new NoSuchElementException("No challenges found!");
        }
    }

    public int getHP() {
        return hp.getCurrentHP();
    }

    public PlayResult play(String userGuess) {
        long endTimeMS = System.currentTimeMillis();
        Duration durationSinceStartOfGame = Duration.ofMillis(endTimeMS - startTimeMS);
        Duration durationSinceStartOfLastChallenge = Duration.ofMillis(endTimeMS - currentChallengeStartTimeMS);

        if (matches(currentScrambleOption, userGuess)) {
            if (challenges.hasNext()) {
                completedChallenges.add(new WordStat(userGuess, durationSinceStartOfLastChallenge, hpLostThisWord));
                currentChallengeStartTimeMS = System.currentTimeMillis();
                currentScrambleOption = challenges.next();
                isFirstWord = false;
                return new PlayResult.Right(scrambleWord());
            } else {
                if (hp.getCurrentHP() == hp.getStartingHP()) {
                    completedChallenges.add(new WordStat(userGuess, durationSinceStartOfLastChallenge, hpLostThisWord));
                    currentChallengeStartTimeMS = System.currentTimeMillis();
                    return new PlayResult.FlawlessVictory(
                            durationSinceStartOfGame,
                            completedChallenges.stream().max(WordStat::compareTo).get(),
                            completedChallenges.stream().min(WordStat::compareTo).get());
                } else {
                    completedChallenges.add(new WordStat(userGuess, durationSinceStartOfLastChallenge, hpLostThisWord));
                    currentChallengeStartTimeMS = System.currentTimeMillis();
                    return new PlayResult.Victory(
                            hp.getCurrentHP(),
                            durationSinceStartOfGame,
                            completedChallenges.stream().max(WordStat::compareTo).get(),
                            completedChallenges.stream().min(WordStat::compareTo).get());
                }
            }
        } else {
            if (hp.hit()) {
                // DOESNT ADD TO COMPLETED CHALLENGES because the challenge hasn't compeleted!
                hpLostThisWord += 1;
                return new PlayResult.Wrong(hp.getCurrentHP());
            } else {
                hpLostThisWord += 1;
                if (isFirstWord) {
                    completedChallenges
                            .add(new WordStat(lastScrambledWord, durationSinceStartOfLastChallenge, hpLostThisWord));
                    currentChallengeStartTimeMS = System.currentTimeMillis();
                    return new PlayResult.EpicDefeat(completedChallenges.getFirst());
                } else {
                    completedChallenges.add(new WordStat(userGuess, durationSinceStartOfLastChallenge, hpLostThisWord));
                    currentChallengeStartTimeMS = System.currentTimeMillis();
                    return new PlayResult.Defeat(
                            durationSinceStartOfGame,
                            completedChallenges.stream().max(WordStat::compareTo).get(),
                            completedChallenges.stream().min(WordStat::compareTo).get());
                }
            }
        }
    }

    public void restart() {
        hp.resetHP();
        resetIterator();
        completedChallenges.clear();

        if (challenges.hasNext())
            currentScrambleOption = challenges.next();
        else {
            throw new NoSuchElementException("No challenges found!");
        }
    }

    public String scrambleWord() {
        lastScrambledWord = scramble(currentScrambleOption);
        return lastScrambledWord;
    }

    private void resetIterator() {
        List<Entry<String, Set<String>>> challengeList = words.entrySet().stream()
                .filter(entry -> checkEntry(entry).isEmpty())
                .toList();

        for (Entry<String, Set<String>> s : challengeList) {
            System.out.println(s);
        }

        challenges = challengeList.iterator();
    }

    public boolean matches(Entry<String, Set<String>> entry, String userInputText) {
        return entry.getValue().contains(userInputText);
    }

    public static String scramble(Entry<String, Set<String>> entry) {
        checkEntry(entry).ifPresent(error -> {
            throw new ImpossiblePermutationException(error);
        });

        RandomGenerator randomGenerator = RandomGenerator.getDefault();
        char[] sourceArray = entry.getKey().toCharArray();

        String randPermutation;
        do {
            // https://en.wikipedia.org/wiki/Random_permutation#Fisher-Yates_shuffles
            for (int i = 0; i < sourceArray.length - 1; i++) {
                int j = randomGenerator.nextInt(i, sourceArray.length);
                char temp = sourceArray[i];
                sourceArray[i] = sourceArray[j];
                sourceArray[j] = temp;
            }
            randPermutation = new String(sourceArray);
        } while (entry.getValue().contains(randPermutation));
        return randPermutation;
    }

    private static Optional<String> checkEntry(Entry<String, Set<String>> entry) {
        if (entry.getKey().equals(""))
            return Optional.of("source string cannot be empty!");

        if (entry.getKey().length() == 1)
            return Optional.of("source string must be longer than 1 character!");

        if (entry.getValue().size() == 0)
            return Optional.of("Must have at least one valid word in permutationsToExclude!");

        if (entry.getValue().size() == numPermutations(entry.getKey()))
            return Optional.of("There couldn't possibly be a non-excluded permutation of source!");

        for (String validWord : entry.getValue()) {
            var sortedWord = validWord.toCharArray();
            Arrays.sort(sortedWord);

            var sortedSource = entry.getKey().toCharArray();
            Arrays.sort(sortedSource);

            if (new String(sortedSource).equals(new String(sortedWord)))
                return Optional.of(String.format(
                        "entry.getValue() must contain only permutations of source! source:%s  permutation:%s",
                        entry.getKey(),
                        validWord));
        }

        return Optional.empty();
    }

    public static int numPermutations(String s) {
        Map<Character, Integer> repetitions = new HashMap<>();

        for (char c : s.toCharArray()) {
            repetitions.putIfAbsent(c, 0);
            repetitions.put(c, repetitions.get(c) + 1);
        }

        var numerator = factorial(s.length());
        var denominator = 1;
        for (int x : repetitions.values()) {
            denominator *= factorial(x);
        }

        return numerator / denominator;
    }

    public static int factorial(int x) {
        int result = 1;
        for (int i = 2; i <= x; i++) {
            result *= i;
        }
        return result;
    }
}

class ImpossiblePermutationException extends RuntimeException {
    public ImpossiblePermutationException() {
    }

    public ImpossiblePermutationException(String message) {
        super(message);
    }
}

class Hitpoints {

    private int hp;
    private final int startingHP;

    public Hitpoints(int hp) {
        if (hp <= 0) {
            throw new IllegalArgumentException("HP value invalid. Make sure it's 1 or greater.\nHP value: " + hp);
        }
        this.hp = hp;
        startingHP = hp;
    }

    public void resetHP() {
        this.hp = startingHP;
    }

    public int getStartingHP() {
        return startingHP;
    }

    public int getCurrentHP() {
        return hp;
    }

    public boolean hit() {
        if (hp == 0)
            throw new IllegalStateException("Can't try if you don't have HP left!");

        return hp-- != 1;
    }
}
