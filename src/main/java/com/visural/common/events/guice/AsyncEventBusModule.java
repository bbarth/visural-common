package com.visural.common.events.guice;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AsyncEventBusModule extends AbstractEventBusModule {

    private final AsyncEventBus eventBus;

    public AsyncEventBusModule() {
        this(Executors.newCachedThreadPool());
    }

    public AsyncEventBusModule(int numThreads) {
        this(Executors.newFixedThreadPool(numThreads));
    }

    public AsyncEventBusModule(Executor executor) {
        eventBus = new AsyncEventBus(AsyncEventBusModule.class.getCanonicalName(), executor);
    }

    @Override
    protected EventBus getEventBus() {
        return eventBus;
    }
}
