package com.github.ruiadrmartins.locationsearcher;

import com.github.ruiadrmartins.locationsearcher.util.Utilities;

import org.junit.Test;

import static org.junit.Assert.*;

public class locationFunctionsUnitTest {
    @Test
    public void cleanupBreaks_isCorrect() {
        String originalString = "<b>Te</b>st";
        String cleanString = "Test";
        assertEquals(Utilities.cleanupBreaks(originalString), cleanString);
        assertNotEquals(Utilities.cleanupBreaks(originalString), originalString);
    }
}