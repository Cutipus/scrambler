package com.acupofjava;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WordGameLogicTest {

    @Test
    public void tryingSameWordShouldReturnTrue() {
        WordGameLogic obj = new WordGameLogic("Hello", 3);
        assertTrue(obj.tryWord("Hello"));
    }

    @Test
    public void capitalOrLowercaseLettersDontMatter() {
        WordGameLogic obj = new WordGameLogic("HeLlo", 3);
        assertTrue(obj.tryWord("hello"));
        assertTrue(obj.tryWord("heLLo"));
        assertTrue(obj.tryWord("HELLO"));
    }

    @Test
    public void shouldFailWhenWrongWordIsEntered() {
        WordGameLogic obj = new WordGameLogic("bye", 3);
        assertFalse(obj.tryWord("Hello"));
    }

    @Test
    public void hpValueIsValid() {
        assertThrows(IllegalArgumentException.class, () -> {
            new WordGameLogic("hello", 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new WordGameLogic("hello", -2);
        });
        assertDoesNotThrow(() -> {
            new WordGameLogic("hello", 6);
        });
    }
}
