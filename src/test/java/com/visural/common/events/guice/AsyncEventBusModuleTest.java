package com.visural.common.events.guice;

import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.visural.common.events.EventBusDeadEventListener;
import com.visural.common.events.SubscribingComponent;
import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class AsyncEventBusModuleTest {

    private Injector i;
    private EventBus eventBus;
    private SubscribingComponent subscribingComponent;
    private EventBusDeadEventListener eventBusDeadEventListener;
    @Mock
    private Logger log;

    @Before
    public void setup() {
        BasicConfigurator.configure();
        MockitoAnnotations.initMocks(this);
        i = Guice.createInjector(new AsyncEventBusModule());
        eventBus = i.getInstance(EventBus.class);
        subscribingComponent = i.getInstance(SubscribingComponent.class);
        eventBusDeadEventListener = i.getInstance(EventBusDeadEventListener.class);
        eventBusDeadEventListener.setLog(log);
    }

    @Test
    public void testEventBus() throws InterruptedException {
        eventBus.post("test");
        Thread.sleep(1000);
        assertEquals("test", subscribingComponent.getValue().get());
        eventBus.post("foo");
        Thread.sleep(1000);
        assertEquals("foo", subscribingComponent.getValue().get());
    }

    @Test
    public void testDeadEvent() throws InterruptedException {
        Integer theInteger = Integer.valueOf(123);
        eventBus.post(theInteger);
        Thread.sleep(1000);
        verify(log).warn(any(String.class), anyObject(), eq(theInteger));
    }
}
