package com.visural.common.events.spring;

import com.google.common.eventbus.EventBus;
import com.visural.common.events.EventBusDeadEventListener;
import com.visural.common.events.SubscribingComponent;
import javax.annotation.Resource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@ContextConfiguration(locations = {"classpath:/com/visural/common/events/spring/async-eventbus-context.xml", "classpath:/eventbus-testcontext.xml"})
public class AsyncEventBusPostProcessorTest extends AbstractJUnit4SpringContextTests {

    @Resource
    private EventBus eventBus;
    @Resource
    private SubscribingComponent subscribingComponent;
    @Resource
    private EventBusDeadEventListener eventBusDeadEventListener;
    @Mock
    private Logger log;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
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
