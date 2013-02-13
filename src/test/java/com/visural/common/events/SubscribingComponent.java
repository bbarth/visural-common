package com.visural.common.events;

import com.google.common.base.Optional;
import com.google.common.eventbus.Subscribe;

/**
 *
 * @author nicholr
 */
public class SubscribingComponent {
    
    private Optional<String> value = Optional.absent();

    public Optional<String> getValue() {
        return value;
    }
    
    @Subscribe
    public void recieveString(String value) {
        this.value = Optional.fromNullable(value);
    }
}
