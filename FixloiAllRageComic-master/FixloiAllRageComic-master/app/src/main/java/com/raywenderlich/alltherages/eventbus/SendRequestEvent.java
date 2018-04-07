package com.raywenderlich.alltherages.eventbus;

import android.os.IBinder;

import com.raywenderlich.alltherages.database.model.SingleOrder;

/**
 * Created by laptopTCC on 6/13/2017.
 */

public class SendRequestEvent {
    private TypeRequestEvent typeRequestEvent;
    private SingleOrder singleOrder;
    private IBinder windowToken;

    public SendRequestEvent(SingleOrder singleOrder, TypeRequestEvent typeRequest) {
        this.singleOrder = singleOrder;
        this.typeRequestEvent = typeRequest;
    }

    public SendRequestEvent(TypeRequestEvent typeRequestEvent) {
        this.typeRequestEvent = typeRequestEvent;
    }

    public SendRequestEvent(TypeRequestEvent typeRequestEvent, IBinder windowToken) {
        this.typeRequestEvent = typeRequestEvent;
        this.windowToken = windowToken;
    }

    public TypeRequestEvent getTypeRequestEvent() {
        return typeRequestEvent;
    }

    public void setTypeRequestEvent(TypeRequestEvent typeRequestEvent) {
        this.typeRequestEvent = typeRequestEvent;
    }

    public SingleOrder getSingleOrder() {
        return singleOrder;
    }

    public void setSingleOrder(SingleOrder singleOrder) {
        this.singleOrder = singleOrder;
    }

    public IBinder getWindowToken() {
        return windowToken;
    }

    public void setWindowToken(IBinder windowToken) {
        this.windowToken = windowToken;
    }
}
