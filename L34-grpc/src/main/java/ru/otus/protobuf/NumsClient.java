package ru.otus.protobuf;

import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumsClient {
    private static final Logger logger = LoggerFactory.getLogger(NumsClient.class);
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    private static final NumsClientStreamObserver streamObserver = new NumsClientStreamObserver();
    private static volatile long lastServerValue = 0L;

    private static final long NEXT_NUM_DELAY_MILLIS = 1_000;

    public static void main(String[] args) {
        logger.info("Starting client");

        startObservingServerValues();

        long currentValue = 0;
        for (int i = 0; i <= 50; i++) {
            try {
                currentValue = calculateNextValue(currentValue);

                logger.info("currentValue: {}", currentValue);
                Thread.sleep(NEXT_NUM_DELAY_MILLIS);

            } catch (InterruptedException e) {
                logger.error("Interrupted while running", e);
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private static long calculateNextValue(final long currentValue) {
        long serverValue = streamObserver.getLastValue();
        logger.info("serverValue: {}", currentValue);

        long nextVal = currentValue;

        if (serverValue != lastServerValue && serverValue > 0) {
            lastServerValue = serverValue;
            nextVal += serverValue + 1;
        } else {
            nextVal += 1;
        }

        return nextVal;
    }

    private static void startObservingServerValues() {
        GetNumsRequest request =
                GetNumsRequest.newBuilder().setFirstNum(0).setLastNum(30).build();

        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        NumServiceGrpc.newStub(channel).getNumbers(request, streamObserver);
    }
}
