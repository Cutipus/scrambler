package com.acupofjava;

import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void scrambleReturnsEmptyStringGivenEmptyString() {
        assertEquals("", App.scrambleWord(""));
    }

    @Test
    public void sameCharacter() {
        assertEquals("a", App.scrambleWord("a"));
    }

    @Test
    public void scrambleReturnsReverseOfStringOfLengthTwo() {
        assertEquals("ba", App.scrambleWord("ab"));
    }

    @Test
    public void scrambleReturnsDifferentWordThanGivenWordLongerThanOneCharacter() {
        assertNotEquals("cat", App.scrambleWord("cat"));
    }

    @Test
    public void scrambleReturnsSameCharactersAsGivenString() {
        String given = "hello";
        String result = App.scrambleWord(given);
        String sortedGiven = given.chars()
                .sorted()
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        String sortedResult = result.chars()
                .sorted()
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();;
        assertEquals(sortedGiven, sortedResult);
    }
}
