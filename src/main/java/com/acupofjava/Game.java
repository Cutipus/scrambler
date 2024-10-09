package com.acupofjava;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.ArrayList;

public class Game {
    List<String> words;
    Iterator<ScrambleOption> challenges;
    ScrambleOption currentScrambleOption;
    Hitpoints hp;

    boolean isFirstWord = true;
    final long startTimeMS = System.currentTimeMillis();

    long currentChallengeStartTimeMS = startTimeMS;
    private int hpLostThisWord;
    List<WordStat> completedChallenges = new ArrayList<>();
    private String lastScrambledWord;

    public Game(List<String> words, int hp) {
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
        // LOOK AT THIS
        long endTimeMS = System.currentTimeMillis();
        Duration durationSinceStartOfGame = Duration.ofMillis(endTimeMS - startTimeMS);
        Duration durationSinceStartOfLastChallenge = Duration.ofMillis(endTimeMS - currentChallengeStartTimeMS);

        if (currentScrambleOption.matches(userGuess)) {
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
        lastScrambledWord = currentScrambleOption.scramble((int) (Math.random() * Integer.MAX_VALUE));
        return lastScrambledWord;
    }

    private void resetIterator() {
        List<ScrambleOption> challengeList = validWordsPermutation(words.stream()).entrySet().stream()
                .map(ScrambleOption::new)
                .filter(scrambleOption -> {
                    try {
                        scrambleOption.scramble(0);
                        return true;
                    } catch (ImpossiblePermutationException e) {
                        return false;
                    }
                })
                .toList();

        for (ScrambleOption s : challengeList) {
            System.out.println(s);
        }

        challenges = challengeList.iterator();
    }

    private Map<String, Set<String>> validWordsPermutation(Stream<String> dictionary) {
        return dictionary.collect(
                Collectors.toMap(
                        word -> {
                            char[] characters = word.toCharArray();
                            Arrays.sort(characters);
                            return new String(characters);
                        },
                        Collections::singleton,
                        (existingValue, newValue) -> Stream.concat(existingValue.stream(), newValue.stream())
                                .collect(Collectors.toSet())));
    }

}

record ScrambleOption(Entry<String, Set<String>> entry) {
    public String scramble(int seed) {
        // TODO: OPTIMIZE THIS! too slow after ~10 characters
        if (seed < 0)
            throw new IllegalArgumentException("Index must be non-negative!");
        Set<String> permutations = generatePermutations();
        Set<String> nonRealWordPermutations = permutations.stream().filter(w -> !entry.getValue().contains(w))
                .collect(Collectors.toSet());
        if (nonRealWordPermutations.size() == 0)
            throw new ImpossiblePermutationException("bad");
        int correctedIndex = seed % nonRealWordPermutations.size();
        return (String) nonRealWordPermutations.toArray()[correctedIndex];
    }

    private Set<String> generatePermutations() {
        Set<String> result = new HashSet<>();
        permute("", entry.getKey(), result);
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
        return entry.getValue().contains(userInputText);
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
