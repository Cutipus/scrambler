package com.acupofjava;

import java.time.Duration;

public record WordStat(String guess, Duration timeTaken, int hpLost) {
}
