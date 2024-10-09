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
        completedChallenges.clear();

        if (challenges.hasNext())
            currentScrambleOption = challenges.next();
        else {
            throw new NoSuchElementException("No challenges found!");
        }
    }
}