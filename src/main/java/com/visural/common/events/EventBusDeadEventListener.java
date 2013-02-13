package com.visural.common.events;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventBusDeadEventListener {

    private Logger log = LoggerFactory.getLogger(EventBusDeadEventListener.class);

    @Subscribe
    public void onDeadEvent(DeadEvent deadEvent) {
        log.warn("Dead event from source {}: {}", deadEvent.getSource(), deadEvent.getEvent());
    }

    public void setLog(Logger log) {
        this.log = log;
    }
}
