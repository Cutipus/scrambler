package com.acupofjava;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Game {
    List<String> words;
    Iterator<ScrambleOption> challenges;
    ScrambleOption currentScrambleOption;
    Hitpoints hp;
    boolean isFirstWord = true;
    long startTimeMS = System.currentTimeMillis();

    public Game(List<String> words, Hitpoints hp) {
        this.words = words;
        this.hp = hp;
        resetIterator();

        if (challenges.hasNext())
            currentScrambleOption = challenges.next();
        else {
            throw new NoSuchElementException("No challenges found!");
        }
    }

    public Game(List<String> words, int hp) {
        this(words, new Hitpoints(hp));
    }

    public String generateScramble() {
        return currentScrambleOption.scramble((int) (Math.random() * Integer.MAX_VALUE));
    }

    public int getHP() {
        return hp.getCurrentHP();
    }

    public PlayResult play(String userGuess) {
        long endTimeMS = System.currentTimeMillis();
        Duration durationSinceStartOfGame = Duration.ofMillis(endTimeMS - startTimeMS);
        if (currentScrambleOption.matches(userGuess)) {
            if (challenges.hasNext()) {
                currentScrambleOption = challenges.next();
                isFirstWord = false;
                return new PlayResult.Right(generateScramble());
            } else {
                if (hp.getCurrentHP() == hp.getStartingHP()) {
                    return new PlayResult.FlawlessVictory(
                            durationSinceStartOfGame,
                            null,
                            null);
                } else {
                    return new PlayResult.Victory(
                            durationSinceStartOfGame,
                            null,
                            null);
                }
            }
        } else {
            if (hp.hit()) {
                return new PlayResult.Wrong(hp.getCurrentHP());
            } else {
                if (isFirstWord) {
                    return new PlayResult.EpicDefeat(
                            durationSinceStartOfGame,
                            null);
                } else {
                    return new PlayResult.Defeat(
                            durationSinceStartOfGame,
                            null,
                            null);
                }
            }
        }
    }

    private void resetIterator() {
        List<ScrambleOption> challengeList = validWordsPermutation(words.stream()).entrySet().stream()
                .map(ScrambleOption::fromEntry)
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

    public void restart() {
        hp.resetHP();
        resetIterator();

        if (challenges.hasNext())
            currentScrambleOption = challenges.next();
        else {
            throw new NoSuchElementException("No challenges found!");
        }
    }
}
