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

                System.out.println(i);

            }
            System.out.println();
        });


        Thread readThreadA = new Thread(() -> {
            maxCountA = readMax(symbolAQueue);
        });

        Thread readThreadB = new Thread(() -> {
            maxCountB = readMax(symbolBQueue);
        });

        Thread readThreadC = new Thread(() -> {
            maxCountC = readMax(symbolCQueue);
        });
        writeThread.start();
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

    static int readMax(BlockingQueue<String> symbolQueue) {
//        if (writeThread.isAlive()) {
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
        AtomicInteger count = new AtomicInteger(0);
        AtomicInteger max = new AtomicInteger(0);
        String str;
        while (!symbolQueue.isEmpty()) {
            System.out.println("Считаем");
            try {
                str = symbolQueue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            String finalstr = str;
            IntStream.range(0, str.length())
                    .forEach(x -> {
                        if (finalstr.charAt(x) == 'c') {
                            count.addAndGet(1);
                        }
                    });
            if (count.get() > max.get()) {
                max.set(count.get());
            }
            count.set(0);
        }
        return max.get();
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