package com.visural.common.events.guice;

import com.google.common.eventbus.EventBus;

public class EventBusModule extends AbstractEventBusModule {

    private final EventBus eventBus = new EventBus(EventBusModule.class.getCanonicalName());

    @Override
    protected EventBus getEventBus() {
        return eventBus;
    }
}
