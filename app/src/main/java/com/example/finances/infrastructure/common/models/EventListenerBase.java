package com.example.finances.infrastructure.common.models;

import com.example.finances.infrastructure.common.interfaces.IEventListener;

public abstract class EventListenerBase implements IEventListener {
    @Override
    public abstract void onEventOccurred();
}
