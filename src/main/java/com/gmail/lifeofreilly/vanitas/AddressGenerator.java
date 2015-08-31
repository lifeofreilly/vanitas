package com.gmail.lifeofreilly.vanitas;

import com.google.common.base.CharMatcher;
import org.apache.log4j.Logger;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.Callable;

class AddressGenerator implements Callable<ECKey> {
    private static final Logger log = Logger.getLogger(AddressGenerator.class);
    private static final int BTC_ADDRESS_MAX_LENGTH = 35;
    private final NetworkParameters netParams;
    private long attempts;
    private String targetPhrase;

    /**
     * Sole constructor for AddressGenerator
     *
     * @param targetPhrase the desired bitcoin address substring
     * @param netParams    the target bitcoin network e.g production or testnet
     */
    public AddressGenerator(final String targetPhrase, final NetworkParameters netParams) {
        this.netParams = netParams;

        if (isValidBTCAddressSubstring(targetPhrase)) {
            this.targetPhrase = targetPhrase;
        } else {
            throw new IllegalArgumentException("The requested phrase is not a valid bitcoin address substring.");
        }

    }

    /**
     * Attempts to compute a bitcoin address that contains the target phrase.
     *
     * @return An ECKey which represents an elliptic curve public key (bitcoin address) and private key
     * @throws Exception
     */
    @Override
    public ECKey call() throws Exception {
        ECKey key;

        do {
            key = new ECKey();
            attempts++;
            logAttempts();
        } while (!(key.toAddress(netParams).toString().contains(targetPhrase)) &&
                !Thread.currentThread().isInterrupted());

        log.debug("Exiting thread " + Thread.currentThread().getName() +
                ", Attempts made: " + NumberFormat.getNumberInstance(Locale.US).format(attempts));
        return key;
    }

    /**
     * Logs progress every 1M attempts
     */
    private void logAttempts() {
        if (attempts % 1000000 == 0) {
            log.debug("Thread " + Thread.currentThread().getName() + " is still working, # of attempts: " +
                    NumberFormat.getNumberInstance(Locale.US).format(attempts));
        }
    }

    /**
     * Verifies that the requested phrase represents a valid bitcoin address substring
     *
     * @param substring the requested phrase
     * @return true if the requested phrase is a valid bitcoin address substring
     */
    private static boolean isValidBTCAddressSubstring(final String substring) {
        boolean validity = true;

        if (!CharMatcher.JAVA_LETTER_OR_DIGIT.matchesAllOf(substring) ||
                substring.length() > BTC_ADDRESS_MAX_LENGTH ||
                CharMatcher.anyOf("OIl0").matchesAnyOf(substring)) {
            validity = false;
        }

        return validity;
    }
}
