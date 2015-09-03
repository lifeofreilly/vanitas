package com.gmail.lifeofreilly.vanitas;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.runners.Parameterized.Parameters;


/**
 * Parameterized unit test for AddressGenerator.isValidSearchString
 */
@RunWith(Parameterized.class)
public class AddressValidityTest {
    private String searchString;
    private boolean expected;

    public AddressValidityTest(String searchString, boolean expected) {
        this.searchString = searchString;
        this.expected = expected;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"1B", true},
                {"1Bitcoin", true},
                {"1BitcoinEater", true},
                {"1BitcoinEaterAddressDontSendf59kuE", true},
                {"O", false},
                {"I", false},
                {"l", false},
                {"0", false},
                {".", false},
                {",", false},
                {"_", false},
                {"-", false},
                {"111111111111111111111111111111111111", false}
        });
    }

    @Test
    public void test() {
        assertEquals(expected, AddressGenerator.isValidSearchString(searchString));
    }

}
