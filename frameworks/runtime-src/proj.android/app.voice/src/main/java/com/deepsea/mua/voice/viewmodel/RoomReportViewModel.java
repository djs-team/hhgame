package com.deepsea.mua.voice.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.OSSConfigBean;
import com.deepsea.mua.stub.entity.ReportListBean;
import com.deepsea.mua.stub.entity.ReportsBean;
import com.deepsea.mua.stub.utils.SignatureUtils;
import com.deepsea.mua.voice.repository.RoomReportRepository;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/8
 */
public class RoomReportViewModel extends ViewModel {

    private final RoomReportRepository reportRepository;

    @Inject
    public RoomReportViewModel(RoomReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }


    public LiveData<Resource<List<ReportListBean>>> getReports() {
        return reportRepository.getReports();
    }
    public LiveData<Resource<BaseApiResult>> report_room(String report_roomid, String report_content) {
        return reportRepository.report_room(report_roomid, report_content);
    }

    public LiveData<Resource<BaseApiResult>> complain(Map<String,String> map) {
        return reportRepository.complain(map);
    }
    public LiveData<Resource<OSSConfigBean>> getOssConfig() {
        return reportRepository.getOssConfig();
    }
}
