package com.deepsea.mua.stub.utils.eventbus;


/**
 * Created by  lyx on 2018/5/4.
 */

public class DownloadEvent {


    public int downloadState;//

    public DownloadEvent(int downloadState) {
        this.downloadState = downloadState;
    }
}
