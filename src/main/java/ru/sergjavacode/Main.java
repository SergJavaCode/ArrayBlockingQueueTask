package ru.sergjavacode;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Main {
    static BlockingQueue<String> symbolAQueue = new ArrayBlockingQueue<>(100);
    static BlockingQueue<String> symbolBQueue = new ArrayBlockingQueue<>(100);
    static BlockingQueue<String> symbolCQueue = new ArrayBlockingQueue<>(100);
    static String maxStringA = new String();
    static String maxStringB = new String();
    static String maxStringC = new String();
    static int maxCountA;
    static int maxCountB;
    static int maxCountC;


    public static void main(String[] args) throws InterruptedException {
        Thread writeThread = new Thread(() -> {
            String randomString;
            for (int i = 0; i < 10_000; i++) {
                randomString = generateText("abc", 100_000);
                try {
                    symbolAQueue.put(randomString);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    symbolBQueue.put(randomString);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    symbolCQueue.put(randomString);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                System.out.println("\n" + i);

            }
            System.out.println();
        });

        Thread readThreadA = new Thread(() -> {
            if (writeThread.isAlive()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            ;
            AtomicInteger count = new AtomicInteger(0);
            AtomicInteger max = new AtomicInteger(0);
            String stringA;
            while (!symbolAQueue.isEmpty()) {
                System.out.println("СчитаемA");
                try {
                    stringA = symbolAQueue.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                String finalStringA = stringA;
                IntStream.range(0, stringA.length())
                        .forEach(x -> {
                            if (finalStringA.charAt(x) == 'a') {
                                count.addAndGet(1);
                            }
                        });
                if (count.get() > max.get()) {
                    max.set(count.get());
                    maxStringA = stringA;
                }
                count.set(0);

            }
            maxCountA = max.get();
            max.set(0);

        });

        Thread readThreadB = new Thread(() -> {
            if (writeThread.isAlive()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            ;
            AtomicInteger count = new AtomicInteger(0);
            AtomicInteger max = new AtomicInteger(0);
            String stringB;
            while (!symbolBQueue.isEmpty()) {
                System.out.println("СчитаемB");
                try {
                    stringB = symbolBQueue.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                String finalStringB = stringB;
                IntStream.range(0, stringB.length())
                        .forEach(x -> {
                            if (finalStringB.charAt(x) == 'b') {
                                count.addAndGet(1);
                            }
                        });
                if (count.get() > max.get()) {
                    max.set(count.get());
                    maxStringB = stringB;
                }
                count.set(0);
            }
            maxCountB = max.get();
            max.set(0);
        });

        Thread readThreadC = new Thread(() -> {
            if (writeThread.isAlive()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            ;
            AtomicInteger count = new AtomicInteger(0);
            AtomicInteger max = new AtomicInteger(0);
            String stringC;
            while (!symbolCQueue.isEmpty()) {
                System.out.println("СчитаемC");
                try {
                    stringC = symbolCQueue.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                String finalStringC = stringC;
                IntStream.range(0, stringC.length())
                        .forEach(x -> {
                            if (finalStringC.charAt(x) == 'c') {
                                count.addAndGet(1);
                            }
                        });
                if (count.get() > max.get()) {
                    max.set(count.get());
                    maxStringC = stringC;
                }
                count.set(0);
            }
            maxCountC = max.get();
            max.set(0);
        });
        writeThread.start();
        // Thread.sleep(10);
        readThreadA.start();
        readThreadB.start();
        readThreadC.start();
        writeThread.join();
        readThreadA.join();
        readThreadB.join();
        readThreadC.join();
        System.out.println("maxStringA" + '\n' + maxCountA + '\n');
        System.out.println("maxStringB" + '\n' + maxCountB + '\n');
        System.out.println("maxStringC" + '\n' + maxCountC + '\n');
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}