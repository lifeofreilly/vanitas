package com.gmail.lifeofreilly.vanitas;

import org.apache.log4j.Logger;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import com.google.common.base.CharMatcher;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.LongStream;

/**
 * A Bitcoin vanity address generator that leverages the Java 8 streams API.
 */
class AddressGenerator {
    private static final Logger log = Logger.getLogger(AddressGenerator.class);
    private static final int BTC_ADDRESS_MAX_LENGTH = 35;
    private final NetworkParameters netParams;
    private final String searchString;

    /**
     * Sole constructor for AddressGenerator
     *
     * @param searchString the desired bitcoin address substring
     * @param netParams    the target bitcoin network
     */
    public AddressGenerator(final String searchString, final NetworkParameters netParams) {
        this.netParams = netParams;

        if (isValidSearchString(searchString)) {
            this.searchString = searchString;
        } else {
            log.error("Filed to create AddressGenerator. IllegalArgumentException: " +
                    "The target phrase '" + searchString + "' is not valid in a bitcoin address.");
            throw new IllegalArgumentException("The target phrase '" + searchString + "' is not valid in a bitcoin address.");
        }

    }

    /**
     * Generates a stream of keys and returns the first match.
     *
     * @return an ECKey with an address that contains the search string
     */
    public ECKey generate() {
        Optional<ECKey> found = LongStream.iterate(0L, n -> n + 1)
                .parallel()
                .peek(AddressGenerator::logAttempts)
                .mapToObj(ignore -> new ECKey())
                .filter(key -> key.toAddress(netParams).toString().contains(searchString))
                .findAny();
        return found.get();
    }

    /**
     * Verifies that the requested search string represents a valid bitcoin address substring
     * Reference: https://en.bitcoin.it/wiki/Address#Address_validation
     *
     * @param substring the requested phrase
     * @return true if the requested phrase is a valid bitcoin address substring
     */
    public static boolean isValidSearchString(final String substring) {
        boolean validity = true;

        if (!CharMatcher.JAVA_LETTER_OR_DIGIT.matchesAllOf(substring) ||
                substring.length() > BTC_ADDRESS_MAX_LENGTH ||
                CharMatcher.anyOf("OIl0").matchesAnyOf(substring)) {
            validity = false;
        }

        return validity;
    }

    /**
     * For every 1 million attempts, log the total number of attempts
     *
     * @param attempts the current number of attempts
     */
    private static void logAttempts(long attempts) {
        if (attempts == 0) {
            log.debug("Address generation initiated.");
        } else if (attempts % 1000000 == 0) {
            log.debug("Address generation in progress, attempts made: " +
                    NumberFormat.getNumberInstance(Locale.US).format(attempts));
        }
    }

}


