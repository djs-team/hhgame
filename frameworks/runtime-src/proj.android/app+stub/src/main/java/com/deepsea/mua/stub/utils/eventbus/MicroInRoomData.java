package com.deepsea.mua.stub.utils.eventbus;

import com.deepsea.mua.stub.entity.socket.MicroOrder;

import java.util.List;

public class MicroInRoomData {
    public List<MicroOrder> microOrders;

    public MicroInRoomData() {
    }

    public MicroInRoomData(List<MicroOrder> microOrders) {
        this.microOrders = microOrders;
    }
}
