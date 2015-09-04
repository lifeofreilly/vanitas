package com.gmail.lifeofreilly.vanitas;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;

import org.hamcrest.core.StringContains;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


/**
 * Unit tests for AddressGenerator
 */
public class AddressGeneratorTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AddressGeneratorTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AddressGeneratorTest.class);
    }

    /**
     * Generates a bitcoin vanity address based on the requested phrase.
     */
    public void testAddressGenerator() {
        final NetworkParameters NET_PARAMS = MainNetParams.get();
        final String targetPhrase = "BTC";

        try {
            AddressGenerator generator = new AddressGenerator(targetPhrase, NET_PARAMS);
            assertThat(generator.generate().toAddress(NET_PARAMS).toString(),
                    StringContains.containsString(targetPhrase));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fail();
        }

    }

    /**
     * Search string contains illegal characters
     */
    public void testAddressGeneratorIllegalCharacters() {
        final NetworkParameters NET_PARAMS = MainNetParams.get();
        final String targetPhrase = "OIl0";

        try {
            new AddressGenerator(targetPhrase, NET_PARAMS);
            fail("Expected: IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is(equalTo("The target phrase '" + targetPhrase + "' is not valid in a bitcoin address.")));
        }
    }

    /**
     * Search string exceeds the maximum address length
     */
    public void testAddressGeneratorInvalidLength() {
        final NetworkParameters NET_PARAMS = MainNetParams.get();
        final String targetPhrase = "111111111111111111111111111111111111";

        try {
            new AddressGenerator(targetPhrase, NET_PARAMS);
            fail("Expected: IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is(equalTo("The target phrase '" + targetPhrase + "' is not valid in a bitcoin address.")));
        }
    }

    /**
     * Search string contains invalid characters
     */
    public void testAddressGeneratorInvalidCharacters() {
        final NetworkParameters NET_PARAMS = MainNetParams.get();
        final String targetPhrase = "foo.bar";

        try {
            new AddressGenerator(targetPhrase, NET_PARAMS);
            fail("Expected: IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is(equalTo("The target phrase '" + targetPhrase + "' is not valid in a bitcoin address.")));
        }
    }

}
