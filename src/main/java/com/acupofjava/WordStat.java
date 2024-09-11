package com.acupofjava;

import java.time.Duration;

public record WordStat(String word, Duration timeTaken, int hpLost) {
}
