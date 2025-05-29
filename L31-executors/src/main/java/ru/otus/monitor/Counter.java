package ru.otus.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Counter {

    private static final Logger logger = LoggerFactory.getLogger(Counter.class);

    private int num = 0;

    private boolean isAscending = true;

    private String lastActorName = "";

    private synchronized void count(String actorName, boolean canChangeCounter) {

        while (!Thread.currentThread().isInterrupted()) {
            try {

                // spurious wakeup https://en.wikipedia.org/wiki/Spurious_wakeup
                // поэтому не if
                while (lastActorName.equals(actorName)) {
                    this.wait();
                }

                if (canChangeCounter) {
                    calculateNextNum();
                }

                logger.info("num: " + num);
                lastActorName = actorName;

                sleep();
                notifyAll();

            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void calculateNextNum() {

        if (num == 1) {
            isAscending = true;
            num++;

        } else if (num < 10) {
            if (isAscending) {
                num++;
            } else {
                num--;
            }

        } else {
            isAscending = false;
            num--;
        }
    }

    public static void main(String[] args) {

        Counter counter = new Counter();
        new Thread(() -> counter.count("firstActor", true)).start();
        new Thread(() -> counter.count("secondActor", false)).start();
    }

    private static void sleep() {

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
