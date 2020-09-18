package com.deepsea.mua.stub.mvp;


public class ResponseModel<T> {

	public String desc;
	public int code;
	public T data;
	public String apk_url;
	@Override
	public String toString() {
		return "ResponseModel{" +
				"desc='" + desc + '\'' +
				", code=" + code +
				", data=" + data +
				", apk_url=" + apk_url +
				'}';
	}
}
