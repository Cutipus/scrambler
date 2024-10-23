package com.acupofjava;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Set;
import java.util.AbstractMap.SimpleEntry;

public class GameTest {
    @Test
    public void testRandomPermutation() {
        for (int i = 0; i < 10000; i++) {
            SimpleEntry<String, Set<String>> entry = new SimpleEntry<>("ceooty", Set.of("coyote, oocyte"));
            String result = Game.scramble(entry);
            assertFalse(Set.of("coyote, oocyte").contains(result));

            char[] sortedResultArray = result.toCharArray();
            Arrays.sort(sortedResultArray);
            String sortedString = new String(sortedResultArray);
            assertEquals("ceooty", sortedString);
        }
    }
}
