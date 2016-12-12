# AMapLocation
高德定位基础library类
## 1.引入  

```
dependencies {
    compile project(':AMapLocation')
}
```
或者直接将.arr包放入libs文件夹  
## 2.配置
先在 **AndroidManifest.xml** 中添加
```
<application
    ...
    >
    
    <meta-data
        android:name="com.amap.api.v2.apikey"
        android:value="${amapKey}" />

</application>
```
然后再
```
buildTypes {
    release {
        ...
        manifestPlaceholders = [
                amapKey: "这里放key"
        ]
    }
    debug {
        ...
        manifestPlaceholders = [
                amapKey: "这里放key"
        ]
    }
}
```
## 3.使用

```
private void startLocation() {
    AMapLocationService locationService = new AMapLocationService(this);
    locationService.setLocationOption(locationService.getDefaultLocationOption());
    locationService.addLocationListener(new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            //do something
        }
    });
    locationService.startLocation();
}

@Override
protected void onDestroy() {
    super.onDestroy();
    locationService.stopLocation();  
    locationService.destroyLocationClient();
}
```  
## 注意
- Android 6.0 + 需要动态请求位置权限
- 更多详情高德定位Android sdk http://lbs.amap.com/api/android-location-sdk/locationsummary/


