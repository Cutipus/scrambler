package com.acupofjava;

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

    public Game(List<String> words, Hitpoints hp) {
        this.words = words;
        this.hp = hp;
        getChallenges();

        if (challenges.hasNext())
            currentScrambleOption = challenges.next();
        else {
            throw new NoSuchElementException("No challenges found!");
        }
    }

    public String generateScramble() {
        return currentScrambleOption.scramble((int) (Math.random() * Integer.MAX_VALUE));
    }

    public int getHP() {
        return hp.getHP();
    }

    public PlayResult play(String userGuess) {
        if (currentScrambleOption.matches(userGuess)) {
            if (challenges.hasNext()) {
                currentScrambleOption = challenges.next();
                return new PlayResult.Right(generateScramble());
            } else {
                return new PlayResult.Victory();
            }
        } else {
            if (hp.hit()) {
                return new PlayResult.Wrong(hp.getHP());
            } else {
                return new PlayResult.Defeat();
            }
        }
    }

    private void getChallenges() {
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

}
