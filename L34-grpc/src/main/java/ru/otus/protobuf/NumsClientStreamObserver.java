package ru.otus.protobuf;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumsClientStreamObserver implements StreamObserver<GetNumResponse> {
    private static final Logger logger = LoggerFactory.getLogger(NumsClientStreamObserver.class);
    private volatile long lastValue;

    @Override
    public void onNext(GetNumResponse numberResponse) {
        synchronized (this) {
            lastValue = numberResponse.getNum();
        }
        logger.info("new value: {}", lastValue);
    }

    @Override
    public void onError(Throwable throwable) {
        logger.error("Error occurred: ", throwable);
    }

    @Override
    public void onCompleted() {
        logger.info("request completed");
    }

    public long getLastValue() {
        return lastValue;
    }
}
