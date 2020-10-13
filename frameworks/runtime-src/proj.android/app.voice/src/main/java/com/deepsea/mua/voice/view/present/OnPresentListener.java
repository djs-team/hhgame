package com.deepsea.mua.voice.view.present;

import com.deepsea.mua.stub.entity.socket.MultiSend;
import com.deepsea.mua.stub.entity.socket.SingleSend;

import java.util.List;

/**
 * Created by JUN on 2019/7/26
 */
public interface OnPresentListener {

    /**
     * 赠礼
     *
     * @param sendModel
     */
    void onMultiSend(MultiSend sendModel);

    /**
     * 打赏
     *
     * @param sendModel
     */
    void onSingleSend(SingleSend sendModel);

    /**
     * 充值
     */
    void onRecharge();

}
