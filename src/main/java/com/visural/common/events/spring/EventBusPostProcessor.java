package com.visural.common.events.spring;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class EventBusPostProcessor implements BeanPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(EventBusPostProcessor.class);

    @Autowired
    private EventBus eventBus;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Method[] methods = bean.getClass().getMethods();
        if (isSubscriber(methods, bean, beanName)) {
            registerSubscriber(bean, beanName);
        }
        return bean;
    }

    private void registerSubscriber(Object bean, String beanName) {
        eventBus.register(bean);
        log.trace("Bean {} subscribed to event bus", beanName);
    }

    private boolean isSubscriber(Method[] methods, Object bean, String beanName) {
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(Subscribe.class)) {
                    return true;
                }
            }
        }
        return false;
    }
}