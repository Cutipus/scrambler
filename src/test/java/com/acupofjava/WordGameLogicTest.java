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

    @Test
    public void guessingWrongDecrementsHP() {
        var game = new WordGameLogic("hello", 3);
        game.tryWord("not!");
        assertEquals(2, game.getHP());
    }

    @Test
    public void guessingWrongWith1HPLeftThrowsException() {
        var game = new WordGameLogic("hello", 1);
        game.tryWord("noooo");
        assertThrows(IllegalStateException.class, () -> game.tryWord("not!"));
    }

    @Test
    public void scrambleReturnsEmptyStringGivenEmptyString() {
        assertEquals("", new WordGameLogic("", 2).scrambleWord());
    }

    @Test
    public void sameCharacter() {
        assertEquals("a", new WordGameLogic("a", 2).scrambleWord());
    }

    @Test
    public void scrambleReturnsReverseOfStringOfLengthTwo() {
        assertEquals("ba", new WordGameLogic("ab", 2).scrambleWord());
    }

    @Test
    public void scrambleReturnsDifferentWordThanGivenWordLongerThanOneCharacter() {
        assertNotEquals("cat", new WordGameLogic("cat", 2).scrambleWord()); // TODO: test with wow
    }

    @Test
    public void scrambleReturnsSameCharactersAsGivenString() {
        String given = "hello";
        String result = new WordGameLogic(given, 2).scrambleWord();
        String sortedGiven = given.chars()
                .sorted()
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        String sortedResult = result.chars()
                .sorted()
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        assertEquals(sortedGiven, sortedResult);
    }

}
