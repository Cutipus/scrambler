package com.acupofjava;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

public class ScrambleOptionTest {
    @Test
    public void testScrambleOption() {
        var scramble = new ScrambleOption("on", List.of("evil", "bad", "vile", "live", "on", "no"));
        assertThrows(ImpossiblePermutationException.class, () -> scramble.scramble(0));
    }
}
