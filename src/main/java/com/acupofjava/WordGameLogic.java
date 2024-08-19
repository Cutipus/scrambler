package com.acupofjava;

public class WordGameLogic {
    enum State {
        VICTORY, DEFEAT, WRONG_ANSWER_STILL_ALIVE
    }

    private String currentWord;
    private int hp;

    public WordGameLogic(String currentWord, int hp) {
        if (hp <= 0) {
            throw new IllegalArgumentException("HP value invalid. Make sure it's 1 or greater.\nHP value: " + hp);
        }
        this.currentWord = currentWord;
        this.hp = hp;
    }

    public int getHP() {
        return hp;
    }

    public String getWord() {
        return currentWord;
    }

    public State tryWord(String userGuess) {
        if (hp == 0)
            throw new IllegalStateException("Can't try if you don't have HP left!");

        if (!userGuess.equalsIgnoreCase(currentWord)) {
            if (hp-- == 1)
                return State.DEFEAT;
            return State.WRONG_ANSWER_STILL_ALIVE;
        } else {
            return State.VICTORY;
        }
    }

    public String scrambleWord() {
        return new StringBuilder(this.currentWord).reverse().toString();
    }

}
