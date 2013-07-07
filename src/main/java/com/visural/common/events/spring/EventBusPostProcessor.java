package com.visural.common.events.spring;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * EventBusPostProcessor registers Spring beans with Guava EventBus.
 */
public class EventBusPostProcessor implements BeanPostProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(EventBusPostProcessor.class);
    @Autowired
    private EventBus eventBus;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        initSubscribed(bean, beanName);

        // Spring's AOP proxying is configured to proxy interface not implementation methods.
        // thus @Subscribing on an implementation bean is ignored in initial bean check since 
        // the proxy bean only has interface methods!
        if (bean instanceof Advised) {
            try {
                Advised advised = (Advised) bean;
                Object target = advised.getTargetSource().getTarget();
                initSubscribed(target, beanName + "#[aop-target]");
            } catch (Exception ex) {
                LOG.warn("Error while trying to subscribe AOP proxy target.", ex);
            }
        }

        return bean;
    }

    private void initSubscribed(Object bean, String beanName) throws SecurityException {
        boolean registered = false;
        Method[] methods = bean.getClass().getMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(Subscribe.class)) {
                    if (!registered) {
                        eventBus.register(bean);
                        registered = true;
                    }
                    LOG.info("Method {}.{}() was subscribed for type {}",
                            new Object[]{
                        beanName, method.getName(),
                        method.getParameterTypes()[0].getName()
                    });
                }
            }
        }
    }
}
