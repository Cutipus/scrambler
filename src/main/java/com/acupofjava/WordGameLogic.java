package com.acupofjava;

public class WordGameLogic {
    private String currentWord;
    private int hp;

    public WordGameLogic(String currentWord, int hp) {
        if (hp <= 0) {
            throw new IllegalArgumentException("HP value invalid. Make sure it's 1 or greater.\nHP value: " + hp);
        }
        this.currentWord = currentWord;
        this.hp = hp;
    }

    public boolean tryWord(String userGuess) {
        if (!userGuess.equalsIgnoreCase(currentWord)) {
            hp--;
            return false;
        } else {
            return true;
        }
    }
}
