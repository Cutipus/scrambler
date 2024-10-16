package com.acupofjava;

import java.time.Duration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;
import java.util.ArrayList;

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
        lastScrambledWord = scramble(currentScrambleOption,
                (int) (Math.random() * Integer.MAX_VALUE));
        return lastScrambledWord;
    }

    private void resetIterator() {
        List<Entry<String, Set<String>>> challengeList = words.entrySet().stream()
                .filter(entry -> {
                    try {
                        scramble(entry, 0);
                        return true;
                    } catch (ImpossiblePermutationException e) {
                        return false;
                    }
                })
                .toList();

        for (Entry<String, Set<String>> s : challengeList) {
            System.out.println(s);
        }

        challenges = challengeList.iterator();
    }

    public boolean matches(Entry<String, Set<String>> entry, String userInputText) {
        return entry.getValue().contains(userInputText);
    }

    // TODO: Remove seed
    public static String scramble(Entry<String, Set<String>> entry, int seed) {
        return randomPermutation(entry.getKey(), entry.getValue());
    }

    public static String randomPermutation(String source, Set<String> permutationsToExclude) {
        RandomGenerator randomGenerator = RandomGenerator.getDefault();
        char[] sourceArray = source.toCharArray();

        String randPermutation;
        do {
            for (int i = 0; i < sourceArray.length - 1; i++) {
                int j = randomGenerator.nextInt(i, sourceArray.length);
                char temp = sourceArray[i];
                sourceArray[i] = sourceArray[j];
                sourceArray[j] = temp;
            }
            randPermutation = new String(sourceArray);
        } while (permutationsToExclude.contains(randPermutation));
        return randPermutation;
    }

    private static Set<String> generatePermutations(String characters) {
        Set<String> result = new HashSet<>();
        permute("", characters, result);
        return result;
    }

    private static void permute(String prefix, String str, Set<String> result) {
        if (str.length() == 0) {
            result.add(prefix);
            return;
        }

        for (int i = 0; i < str.length(); i++) {
            permute(prefix + str.charAt(i), str.substring(0, i) + str.substring(i + 1, str.length()), result);
        }

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
