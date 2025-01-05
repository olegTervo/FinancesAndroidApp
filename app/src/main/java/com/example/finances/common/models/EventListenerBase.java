package com.example.finances.common.models;

import com.example.finances.common.interfaces.IEventListener;

public abstract class EventListenerBase implements IEventListener {
    @Override
    public abstract void onEventOccurred();
}
