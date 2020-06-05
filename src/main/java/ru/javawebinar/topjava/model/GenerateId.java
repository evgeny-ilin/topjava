package ru.javawebinar.topjava.model;

import java.util.concurrent.atomic.AtomicInteger;

public class GenerateId {
    private static AtomicInteger id;

    private GenerateId() {
    }

    public static int getId() {
        if (id == null) id = new AtomicInteger(0);
        return id.incrementAndGet();
    }
}
