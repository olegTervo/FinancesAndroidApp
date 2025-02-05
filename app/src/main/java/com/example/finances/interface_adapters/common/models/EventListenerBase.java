package com.example.finances.interface_adapters.common.models;

import com.example.finances.interface_adapters.common.interfaces.IEventListener;

public abstract class EventListenerBase implements IEventListener {
    @Override
    public abstract void onEventOccurred();
}
