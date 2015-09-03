package com.gmail.lifeofreilly.vanitas;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;

import java.util.concurrent.TimeUnit;

/**
 * A command line application for generating a vanity bitcoin address.
 * As a best practice you should not reuse bitcoin addresses.
 * Address reuse harms your privacy, as well as the privacy of others.
 * For more information see: https://en.bitcoin.it/wiki/Address_reuse
 */
class Vanitas {
    //use production bitcoin network
    private static final NetworkParameters NET_PARAMS = MainNetParams.get();

    /**
     * A command line application for generating a vanity bitcoin address.
     *
     * @param args required argument. The desired bitcoin address substring.
     */
    public static void main(String[] args) {
        final String searchString;

        if (args.length == 1) {
            searchString = args[0];

            if (AddressGenerator.isValidSearchString(searchString)) {
                AddressGenerator generator = new AddressGenerator(searchString, NET_PARAMS);
                System.out.println("Searching for a bitcoin address that contains: " + searchString);
                System.out.println("Status is available at: " + System.getProperty("user.dir") + "/logs/error.log");

                final long startTime = System.nanoTime();
                ECKey key = generator.generate();
                System.out.println("Found in " + getElapsedTimeMinSec(startTime));
                System.out.println("Address: " + key.toAddress(NET_PARAMS));
                System.out.println("Private Key: " + key.getPrivKey());

            } else {
                System.out.println("Your target phrase '" + searchString + "' contains illegal characters. " +
                        "Please see: https://en.bitcoin.it/wiki/Address#Address_validation");
                System.exit(-1);
            }
        }
    }

    /**
     * Get elapsed time in minutes and seconds e.g, 1 min, 30 sec
     *
     * @param startTime the start time
     * @return elapsed time in minutes and seconds
     */
    private static String getElapsedTimeMinSec(long startTime) {
        long elapsedTime = System.nanoTime() - startTime;

        return String.format("%d min, %d sec",
                TimeUnit.NANOSECONDS.toMinutes(elapsedTime),
                TimeUnit.NANOSECONDS.toSeconds(elapsedTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.NANOSECONDS.toMinutes(elapsedTime))
        );
    }

}
