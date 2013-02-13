package com.visural.common.events.guice;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.visural.common.events.EventBusDeadEventListener;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import javax.inject.Singleton;

public abstract class AbstractEventBusModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(EventBusDeadEventListener.class).in(Scopes.SINGLETON);

        bindListener(methodsAnnotatedWith(Subscribe.class), new TypeListener() {
            public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
                encounter.register(new InjectionListener<I>() {
                    public void afterInjection(I injectee) {
                        getEventBus().register(injectee);
                    }
                });
            }
        });
    }

    protected abstract EventBus getEventBus();

    @Provides
    @Singleton
    public EventBus provideEventBus() {
        return getEventBus();
    }

    private Matcher<TypeLiteral<?>> methodsAnnotatedWith(Class<? extends Annotation> annotation) {
        return new MethodsAnnotatedWith(annotation);
    }

    private static class MethodsAnnotatedWith extends AbstractMatcher<TypeLiteral<?>> {

        private final Class<? extends Annotation> annotation;

        private MethodsAnnotatedWith(Class<? extends Annotation> annotation) {
            this.annotation = annotation;
        }

        @Override
        public boolean matches(TypeLiteral<?> typeLiteral) {
            Method[] methods = typeLiteral.getRawType().getMethods();
            for (Method method : methods) {
                if (method.getAnnotation(annotation) != null) {
                    return true;
                }
            }
            return false;
        }
    }
}
