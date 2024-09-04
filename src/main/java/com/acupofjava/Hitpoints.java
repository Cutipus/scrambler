package com.acupofjava;

public class Hitpoints {

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

    public int getHP() {
        return hp;
    }

    public boolean hit() {
        if (hp == 0)
            throw new IllegalStateException("Can't try if you don't have HP left!");

        return hp-- != 1;
    }
}
