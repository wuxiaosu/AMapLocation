package com.wuxiaosu.amaplocation;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Su on 2016/11/27.
 * 定位服务
 */

public class AMapLocationService implements AMapLocationListener {

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption mOption;
    private AMapLocationClientOption DIYOption;
    private Object objLock = new Object();
    private List<AMapLocationListener> listenerList = new ArrayList<>();

    public AMapLocationService(Context locationContext) {
        if (locationClient == null) {
            synchronized (objLock) {
                if (locationClient == null) {
                    locationClient = new AMapLocationClient(locationContext);
                    locationClient.setLocationOption(getDefaultLocationOption());
                    locationClient.setLocationListener(this);
                }
            }
        }
    }

    /**
     * 定位参数
     *
     * @param option
     * @return
     */
    public boolean setLocationOption(AMapLocationClientOption option) {
        boolean isSuccess = false;
        if (option != null) {
            if (locationClient.isStarted()) {
                locationClient.stopLocation();
            }
            DIYOption = option;
            locationClient.setLocationOption(option);
            isSuccess = true;
        }
        return isSuccess;
    }

    public AMapLocationClientOption getOption() {
        return DIYOption;
    }

    public AMapLocationClientOption getDefaultLocationOption() {
        if (mOption == null) {
            mOption = new AMapLocationClientOption();
            mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
            mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
            mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
            mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
            mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
            mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
            mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
            AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
            mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        }
        return mOption;
    }

    public void startLocation() {
        synchronized (objLock) {
            if (locationClient != null && !locationClient.isStarted()) {
                locationClient.startLocation();
            }
        }
    }

    public void stopLocation() {
        synchronized (objLock) {
            if (locationClient != null && !locationClient.isStarted()) {
                locationClient.stopLocation();
            }
        }
    }

    /**
     * 销毁定位
     */
    public void destroyLocationClient() {
        if (null != locationClient) {
            locationClient.onDestroy();
            locationClient = null;
            mOption = null;
            DIYOption = null;
        }
    }

    /**
     * 定位监听
     *
     * @param listener
     * @return
     */
    public boolean addLocationListener(AMapLocationListener listener) {
        return listenerList.add(listener);
    }

    public boolean removeLocationListener(AMapLocationListener listener) {
        return listenerList.remove(listener);
    }

    private void notifyListener(AMapLocation aMapLocation) {
        for (AMapLocationListener aMapLocationListener : listenerList) {
            aMapLocationListener.onLocationChanged(aMapLocation);
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        notifyListener(aMapLocation);
    }
}
