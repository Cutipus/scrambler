package com.acupofjava;

import java.time.Duration;

public sealed interface PlayResult {
        public static record Victory(
                        Duration totalTime,
                        WordStat wordThatTookLongest,
                        WordStat wordThatTookShortest) implements PlayResult {
        }

        public static record FlawlessVictory(
                        Duration totalTime,
                        WordStat wordThatTookLongest,
                        WordStat wordThatTookShortest) implements PlayResult {
        }

        public static record Defeat(
                        Duration totalTime,
                        WordStat wordThatTookLongest,
                        WordStat wordThatTookShortest) implements PlayResult {
        }

        public static record EpicDefeat(WordStat wordThatDefeatedPlayer) implements PlayResult {
        }

        public static record Right(String nextChallenge) implements PlayResult {
        }

        public static record Wrong(int hpLeft) implements PlayResult {
        }
}
