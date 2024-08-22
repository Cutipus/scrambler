package com.acupofjava;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.Test;

public class ScrambleOptionTest {
    @Test
    public void testScrambleOption() {
        var scramble = new ScrambleOption("on", Set.of("on", "no"));
        assertThrows(ImpossiblePermutationException.class, () -> scramble.scramble(0));
    }

    @Test
    public void testRNG() {
        ScrambleOption scrambleOption = new ScrambleOption("on", Set.of("on", "no"));
        assertThrows(ImpossiblePermutationException.class,() -> scrambleOption.scramble(4));
    }
}
