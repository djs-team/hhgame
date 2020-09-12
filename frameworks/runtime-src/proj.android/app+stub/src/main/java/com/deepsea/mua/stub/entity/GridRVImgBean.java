package com.deepsea.mua.stub.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 作者：liyaxing  2019/8/20 16:11
 * 类别 ：
 */
public class GridRVImgBean implements Parcelable {
    public String content;
    public ArrayList<String> photos;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeStringList(this.photos);
    }

    public GridRVImgBean() {
    }

    public GridRVImgBean(String content, ArrayList<String> photos) {
        this.content = content;
        this.photos = photos;
    }

    protected GridRVImgBean(Parcel in) {
        this.content = in.readString();
        this.photos = in.createStringArrayList();
    }

    public static final Parcelable.Creator<GridRVImgBean> CREATOR = new Parcelable.Creator<GridRVImgBean>() {
        @Override
        public GridRVImgBean createFromParcel(Parcel source) {
            return new GridRVImgBean(source);
        }

        @Override
        public GridRVImgBean[] newArray(int size) {
            return new GridRVImgBean[size];
        }
    };
}
