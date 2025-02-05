package com.example.finances.interface_adapters.common.services;

import com.example.finances.interface_adapters.common.models.EventListenerBase;

import java.util.ArrayList;
import java.util.List;

public class EventService {
    private List<EventListenerBase> listeners = new ArrayList<>();

    public void addListener(EventListenerBase listener) {
        listeners.add(listener);
    }

    public void removeListener(EventListenerBase listener) {
        listeners.remove(listener);
    }

    public void fireEvent() {
        for (EventListenerBase listener : listeners) {
            listener.onEventOccurred();
        }
    }
}
