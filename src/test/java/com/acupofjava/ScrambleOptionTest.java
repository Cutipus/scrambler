package com.acupofjava;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.Test;

public class ScrambleOptionTest {
    @Test
    public void testScrambleOption() {
        var scramble = new ScrambleOption("on", Set.of("evil", "bad", "vile", "live", "on", "no"));
        assertThrows(ImpossiblePermutationException.class, () -> scramble.scramble(0));
    }
}
