package com.gmail.lifeofreilly.vanitas;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.MoreExecutors;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;

import org.apache.log4j.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import javax.annotation.ParametersAreNonnullByDefault;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

class Vanitas {
    private static final Logger log = Logger.getLogger(Vanitas.class);
    private static final NetworkParameters NET_PARAMS = MainNetParams.get(); //use production bitcoin network

    /**
     * An application for generating a vanity bitcoin address.
     * As a best practice you should not reuse bitcoin addresses.
     * Address reuse harms your privacy, as well as the privacy of others.
     * For more information see: https://en.bitcoin.it/wiki/Address_reuse
     *
     * @param args required argument. The desired bitcoin address substring.
     */
    public static void main(String[] args) {
        final String targetPhrase;

        if (args.length == 1) {
            targetPhrase = args[0];

            try {
                generateAddress(targetPhrase);
                System.out.println("Searching for a bitcoin address that contains: " + targetPhrase);
                System.out.println("Status is available at: " + System.getProperty("user.dir") + "/logs/error.log");
            } catch (IllegalArgumentException ex) {
                System.out.println("Your target phrase '" + targetPhrase + "' contains illegal characters. " +
                        "Please see: https://en.bitcoin.it/wiki/Address#Address_validation");
                System.exit(-1);
            }

        } else {
            System.out.println("Invalid number of arguments. Usage: Vanitas [phrase]");
            System.exit(-1);
        }

    }

    /**
     * Establishes a thread pool based on the number of available processors, then executes an AddressGenerator for each.
     * The resulting bitcoin address and the associated private key will be written to the standard output stream.
     *
     * @param targetPhrase The desired bitcoin address substring.
     */
    private static void generateAddress(final String targetPhrase) {
        final int cores = Runtime.getRuntime().availableProcessors();
        log.info("Searching for a bitcoin address that contains: " + targetPhrase);
        log.info("Number of threads that will be used: " + cores);

        final ListeningExecutorService execService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(cores));
        final long timeStart = System.nanoTime();

        for (int i = 0; i < cores; i++) {
            Callable<ECKey> callable = new AddressGenerator(targetPhrase, NET_PARAMS);
            ListenableFuture<ECKey> future = execService.submit(callable);
            Futures.addCallback(future, new FutureCallback<ECKey>() {

                @Override
                public void onSuccess(ECKey key) {
                    if (key.toAddress(NET_PARAMS).toString().contains(targetPhrase)) {
                        System.out.println("Found in " + MINUTES.convert((System.nanoTime() - timeStart), NANOSECONDS) + " minutes");
                        System.out.println("Address: " + key.toAddress(NET_PARAMS));
                        System.out.println("Private Key: " + key.getPrivKey());

                    }
                    execService.shutdownNow();
                }

                @Override
                @ParametersAreNonnullByDefault
                public void onFailure(Throwable thrown) {
                    log.error(thrown.getMessage());
                }
            });

        }
    }

}
