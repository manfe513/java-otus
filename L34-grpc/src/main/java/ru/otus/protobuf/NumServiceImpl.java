package ru.otus.protobuf;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"squid:S2142"})
public class NumServiceImpl extends NumServiceGrpc.NumServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(NumServiceImpl.class);

    private static final long NEXT_NUM_DELAY_MILLIS = 2_000;

    @Override
    public void getNumbers(final GetNumsRequest request, final StreamObserver<GetNumResponse> responseObserver) {

        logger.info("Received request: firstValue={}, lastValue={}", request.getFirstNum(), request.getLastNum());

        try {
            for (long i = request.getFirstNum() + 1; i <= request.getLastNum(); i++) {
                GetNumResponse response = GetNumResponse.newBuilder().setNum(i).build();
                responseObserver.onNext(response);

                logger.info("Sent value: {}", i);

                Thread.sleep(NEXT_NUM_DELAY_MILLIS);
            }
        } catch (InterruptedException e) {
            logger.error("Interrupted while streaming", e);
            Thread.currentThread().interrupt();
        }

        responseObserver.onCompleted();
        logger.info("Stream completed");
    }
}
