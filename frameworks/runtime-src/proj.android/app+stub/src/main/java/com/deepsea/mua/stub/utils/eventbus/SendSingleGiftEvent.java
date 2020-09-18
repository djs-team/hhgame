package com.deepsea.mua.stub.utils.eventbus;


/**
 * Created by  lyx on 2018/5/4.
 */

public class SendSingleGiftEvent {


    private int aClick;//
    private int aData, aDataIntA, aDataIntB, aDataIntC, aDataIntD;//
    private float fData;//
    private String aDataS, aDataS2, aDataS3;//
    private Object params;

    public Object getParams() {
        return params;
    }

    public void setParams(Object params) {
        this.params = params;
    }

    public SendSingleGiftEvent(int aClick, Object params) {
        this.aClick = aClick;
        this.params = params;
    }

    public SendSingleGiftEvent(int aClick) {
        this.aClick = aClick;
    }

    public SendSingleGiftEvent() {
    }

    public int getClick() {
        return aClick;
    }

    public void setClick(int aClick) {
        this.aClick = aClick;
    }

    public int getData() {
        return aData;
    }

    public void setData(int aClick) {
        this.aData = aClick;
    }

    public float getDataF() {
        return fData;
    }

    public void setDataF(float aClick) {
        this.fData = aClick;
    }

    public String getDataS() {
        return aDataS;
    }


    public void setDataS(String aClick) {
        this.aDataS = aClick;
    }

    public void setDataS2(String aClick) {
        this.aDataS2 = aClick;
    }

    public String getDataS2() {
        return aDataS2;
    }

    public void setDataS3(String aClick) {
        this.aDataS3 = aClick;
    }

    public String getDataS3() {
        return aDataS3;
    }


    public int getDataIntA() {
        return aDataIntA;
    }

    public void setDataIntA(int aClick) {
        this.aDataIntA = aClick;
    }

    public int getDataIntB() {
        return aDataIntB;
    }

    public void setDataIntB(int aClick) {
        this.aDataIntB = aClick;
    }

    public int getDataIntC() {
        return aDataIntC;
    }

    public void setDataIntC(int aClick) {
        this.aDataIntC = aClick;
    }

    public int getDataIntD() {
        return aDataIntD;
    }

    public void setDataIntD(int aClick) {
        this.aDataIntD = aClick;
    }
}
