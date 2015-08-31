package com.gmail.lifeofreilly.vanitas;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.MoreExecutors;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import org.hamcrest.core.StringContains;

import javax.annotation.ParametersAreNonnullByDefault;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


/**
 * Unit tests for Vanitas
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
     *  Generates a bitcoin vanity address based on the requested phrase.
     */
    public void testAddressGenerator() {
        final NetworkParameters NET_PARAMS = MainNetParams.get();
        final ListeningExecutorService execService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(1));
        final String targetPhrase = "BTC";

        Callable<ECKey> callable = new AddressGenerator(targetPhrase, NET_PARAMS);
        ListenableFuture<ECKey> future = execService.submit(callable);

        Futures.addCallback(future, new FutureCallback<ECKey>() {
            @Override
            public void onSuccess(ECKey ecKey) {
                if (ecKey.toAddress(NET_PARAMS).toString().contains(targetPhrase)) {
                    assertTrue(true);
                }
                execService.shutdownNow();
            }

            @Override
            @ParametersAreNonnullByDefault
            public void onFailure(Throwable thrown) {
                System.out.print(thrown.getMessage());
                fail();
            }
        });

        try {
            String vanityAddress = future.get().toAddress(NET_PARAMS).toString();
            assertThat(vanityAddress, StringContains.containsString(targetPhrase));
            System.out.println("Test Address Generated: " + vanityAddress);
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail();
        } catch (ExecutionException e) {
            e.printStackTrace();
            fail();
        }

    }

    /**
     * Verifies that a request that contains illegal characters is rejected
     */
    public void testAddressGeneratorIllegalCharacters() {
        final NetworkParameters NET_PARAMS = MainNetParams.get();
        final String targetPhrase = "OIl0";

        try {
            new AddressGenerator(targetPhrase, NET_PARAMS);
            fail("Expected: IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is(equalTo("The requested phrase is not a valid bitcoin address substring.")));
        }
    }

    /**
     * Verifies that a request that exceeds the maximum address length is rejected
     */
    public void testAddressGeneratorInvalidLength() {
        final NetworkParameters NET_PARAMS = MainNetParams.get();
        final String targetPhrase = "111111111111111111111111111111111111";

        try {
            new AddressGenerator(targetPhrase, NET_PARAMS);
            fail("Expected: IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is(equalTo("The requested phrase is not a valid bitcoin address substring.")));
        }
    }

    /**
     * Verifies that a request that contains invalid characters is rejected
     */
    public void testAddressGeneratorInvalidCharacters() {
        final NetworkParameters NET_PARAMS = MainNetParams.get();
        final String targetPhrase = "foo.bar";

        try {
            new AddressGenerator(targetPhrase, NET_PARAMS);
            fail("Expected: IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is(equalTo("The requested phrase is not a valid bitcoin address substring.")));
        }
    }
}
