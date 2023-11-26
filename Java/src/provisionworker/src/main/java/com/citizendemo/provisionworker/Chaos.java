package com.citizendemo.provisionworker;

public class Chaos {
    @Override
    protected void finalize() throws Throwable {
        while (true) {
            Thread.yield();
        }
    }
}