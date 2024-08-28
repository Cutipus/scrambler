package com.acupofjava;

public sealed interface PlayResult {
    public static record Victory() implements PlayResult {
    }

    public static record Defeat() implements PlayResult {
    }

    public static record Right(String nextChallenge) implements PlayResult {
    }

    public static record Wrong(int hpLeft) implements PlayResult {
    }
}
