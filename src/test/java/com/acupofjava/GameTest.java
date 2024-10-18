package com.acupofjava;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Set;

public class GameTest {

    @Test
    public void testRandomPermutation() {
        for (int i = 0; i < 10000; i++) {
            Set<String> set = Set.of("coyote, oocyte");
            String result = Game.randomPermutation("ceooty", set);
            assertFalse(set.contains(result));

            char[] sortedResultArray = result.toCharArray();
            Arrays.sort(sortedResultArray);
            String sortedString = new String(sortedResultArray);
            assertEquals("ceooty", sortedString);
        }
    }
}
