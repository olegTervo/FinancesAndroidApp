package com.example.finances.infrastructure.common.services;

import com.example.finances.infrastructure.common.models.EventListenerBase;

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
