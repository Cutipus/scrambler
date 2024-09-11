package com.acupofjava;

import java.time.Duration;

public record WordStat(String word, Duration timeTaken, int hpLost) implements Comparable<WordStat> {
    @Override
    public int compareTo(WordStat o) {
        return this.timeTaken.compareTo(o.timeTaken());
    }

    @Override
    public String toString() {
        return String.format("['%s' | took %d seconds to complete | lost %d hp on it]",
                word,
                timeTaken.toSeconds(),
                hpLost);
    }
}
