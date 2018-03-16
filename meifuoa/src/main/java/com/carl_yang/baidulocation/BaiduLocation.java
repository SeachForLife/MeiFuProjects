package com.carl_yang.baidulocation;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class BaiduLocation {
	private LocationClient locationClient = null;
	private BDLocationListener locationListener = null;
	private LocationClientOption option = null;

	private LocateCallBack callback = null;
	private int scanSpan = 0;

	public BaiduLocation(Context context, LocateCallBack callback, int scanSpan) {
		this.callback = callback;
		this.scanSpan = scanSpan;
		locationClient = new LocationClient(context);
		locationListener = new MyLocationListener();
		locationClient.registerLocationListener(locationListener);
	}

	public BaiduLocation(Context context, int scanSpan) {
		this.scanSpan = scanSpan;
		locationClient = new LocationClient(context);
		locationListener = new MyLocationListener();
		locationClient.registerLocationListener(locationListener);
	}

	private class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			if (location == null) {
				return;
			}
			callback.saveLocation(location);
		}

		@Override
		public void onConnectHotSpotMessage(String s, int i) {

		}
	}

	public void locationRequest() {
		setLocationParams();
		if (locationClient != null && locationClient.isStarted()) {
			locationClient.requestLocation();
		} else {
		}
	}

	public void stopLocation() {
		if (locationClient != null) {
			locationClient.stop();
			locationClient = null;
		}
	}

	private void setLocationParams() {
		option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll");
		option.setScanSpan(scanSpan);
		option.setIsNeedAddress(true);
		if (locationClient != null) {
			locationClient.setLocOption(option);
			locationClient.start();
		}
	}

}
