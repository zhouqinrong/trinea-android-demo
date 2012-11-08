package com.trinea.android.demo;

import android.os.Bundle;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;

public class BaiduMapDemo extends MapActivity {

    MapView       bMap;
    BMapManager   bMapM;
    MapController bMapController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baidu_map_demo);

        bMapM = new BMapManager(getApplication());
        bMapM.init(Constants.BAIDU_MAP_KEY, null);
        super.initMapActivity(bMapM);

        bMap = (MapView)findViewById(R.id.baiduMapView);
        bMap.setBuiltInZoomControls(true);

        bMapController = bMap.getController();
        GeoPoint point = new GeoPoint((int)(39.915 * 1E6), (int)(116.404 * 1E6));
        bMapController.setCenter(point);
        bMapController.setZoom(12);

        // 设置视图
        // bMap.setStreetView(true);
        bMap.setSatellite(true);
        bMap.setTraffic(true);
    }

    @Override
    protected void onDestroy() {
        if (bMapM != null) {
            bMapM.destroy();
            bMapM = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (bMapM != null) {
            bMapM.stop();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (bMapM != null) {
            bMapM.start();
        }
        super.onResume();
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}
