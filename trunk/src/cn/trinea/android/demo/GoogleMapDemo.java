package cn.trinea.android.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Toast;
import cn.trinea.android.demo.utils.AppUtils;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

/**
 * GoogleMapDemo
 * 
 * @author Trinea 2013-5-9
 */
public class GoogleMapDemo extends MapActivity {

    /** 缩放比例 **/
    private static int           ZOOM_RATE             = 13;
    /** 双击间隔，毫秒为单位 **/
    private static long          DOUBLE_CLICK_INTERVAL = 1000;
    /** 上次touch时间 **/
    private long                 lastTouchTime         = 0;
    /** 最多返回的地址数目 **/
    private int                  MAX_ADDR_RESULT_NUM   = 5;

    private GMapLocationListener locListener           = new GMapLocationListener();
    private LocationManager      locManager;

    MapView                      gMap;
    MapController                gMapController;

    private Button               trineaInfoTv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_map_demo);

        AppUtils.initTrineaInfo(this, trineaInfoTv, getClass());

        ActionBar bar = getActionBar();
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP);

        gMap = (MapView)findViewById(R.id.googleMapView);
        gMapController = gMap.getController();
        gMap.setBuiltInZoomControls(true);
        gMap.displayZoomControls(true);

        // 设置视图
        // gMap.setStreetView(true);
        // gMap.setSatellite(true);
        // gMap.setTraffic(true);

        // 定位到自己的位置并随移动更新
        locManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        Location l = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (l == null) {
            Toast.makeText(getApplication(), "无法获取自己的位置", Toast.LENGTH_SHORT).show();
        } else {
            GeoPoint gp = new GeoPoint((int)(l.getLatitude() * 1E6), (int)(l.getLongitude() * 1E6));
            gMapController.animateTo(gp);
            gMapController.setCenter(gp);
            gMapController.setZoom(ZOOM_RATE);
            Toast.makeText(getApplication(), getAddressFromGeoPoint(gp), Toast.LENGTH_SHORT).show();

            // 显示图钉
            List<Overlay> overlays = gMap.getOverlays();
            overlays.clear();
            overlays.add(new GoogleMapOverlay(gp));
            gMap.invalidate();
        }

        // 查找上海图书馆
        // String libraryAddr = "上海浦东新区前程路88号";
        String libraryAddr = "上海图书馆";
        List<GeoPoint> libraryGp = getGeoPointFromAddress(libraryAddr);
        if (libraryGp == null || libraryGp.size() <= 0) {
            Toast.makeText(getApplication(), "找不到" + libraryAddr, Toast.LENGTH_SHORT).show();
        } else {
            List<Overlay> overlays = gMap.getOverlays();
            overlays.clear();
            overlays.add(new GoogleMapItemOverlay(getResources().getDrawable(R.drawable.mark),
                                                  libraryGp));
            // overlays.add(new GoogleMapOverlay(libraryGp));
            gMap.invalidate();
        }
    }

    protected void OnPause() {
        locManager.removeUpdates(locListener);
        super.onPause();
    }

    protected void OnResume() {
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10 * 1000, 1, locListener);
        super.onResume();
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    /**
     * map覆盖图层
     * 
     * @author Trinea 2012-9-19 上午11:24:00
     */
    public class GoogleMapOverlay extends Overlay {

        private List<GeoPoint> gpList = new ArrayList<GeoPoint>();

        public GoogleMapOverlay(GeoPoint gp){
            super();
            gpList.add(gp);
        }

        public GoogleMapOverlay(List<GeoPoint> gpList){
            super();
            if (gpList != null && gpList.size() > 0) {
                this.gpList.addAll(gpList);
            }
        }

        public GoogleMapOverlay(int latitudeE6, int longitudeE6){
            super();
            gpList.add(new GeoPoint(latitudeE6, longitudeE6));
        }

        @Override
        public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
            super.draw(canvas, mapView, shadow, when);

            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.mark);
            for (GeoPoint gp : gpList) {
                Point p = new Point();
                gMap.getProjection().toPixels(gp, p);

                // 显示标记，可以自己精确调整显示
                canvas.drawBitmap(bmp, p.x, p.y, null);
            }
            return true;
        }

        @Override
        public boolean onTouchEvent(MotionEvent e, MapView mapView) {
            if (e.getAction() == MotionEvent.ACTION_UP) {
                // GeoPoint p = gMap.getProjection().fromPixels((int)e.getX(), (int)e.getY());

                /**
                 * 显示触摸的位置
                 */
                // Toast.makeText(getApplication(), "点击位置坐标为" + p.getLatitudeE6() / 1E6 + ", " + p.getLongitudeE6() /
                // 1E6,
                // Toast.LENGTH_SHORT).show();

                /**
                 * 判断是否是有效双击，是则放大
                 */
                long t = System.currentTimeMillis();
                if (t - lastTouchTime < DOUBLE_CLICK_INTERVAL) {
                    gMapController.zoomIn();
                }
                lastTouchTime = t;

                /**
                 * 显示触摸的位置地址
                 */
                // Toast.makeText(getApplication(), "地址为：" + getAddressFromGeoPoint(p), Toast.LENGTH_SHORT).show();
            }
            return super.onTouchEvent(e, mapView);
        }
    }

    /**
     * map item覆盖图层
     * 
     * @author Trinea 2012-9-19 下午03:28:35
     */
    public class GoogleMapItemOverlay extends ItemizedOverlay<OverlayItem> {

        private List<GeoPoint> gpList = new ArrayList<GeoPoint>();

        public GoogleMapItemOverlay(Drawable defaultMarker, GeoPoint gp){
            super(boundCenterBottom(defaultMarker));
            gpList.add(gp);
            populate();
        }

        public GoogleMapItemOverlay(Drawable defaultMarker, List<GeoPoint> gpList){
            super(boundCenterBottom(defaultMarker));
            if (gpList != null && gpList.size() > 0) {
                this.gpList.addAll(gpList);
            }
            populate();
        }

        public GoogleMapItemOverlay(Drawable defaultMarker, int latitudeE6, int longitudeE6){
            super(defaultMarker);
            gpList.add(new GeoPoint(latitudeE6, longitudeE6));
            populate();
        }

        @Override
        protected OverlayItem createItem(int i) {
            if (gpList != null && gpList.size() > 0) {
                return new OverlayItem(gpList.get(i), "title" + i, "snippet" + i);
            }
            return null;
        }

        /**
         * 返回标记的数目
         * 
         * @return
         */
        @Override
        public int size() {
            return (gpList == null) ? 0 : gpList.size();
        }

        @Override
        public boolean onTouchEvent(MotionEvent e, MapView mapView) {
            if (e.getAction() == MotionEvent.ACTION_UP) {
                /**
                 * 判断是否是有效双击，是则放大
                 */
                long t = System.currentTimeMillis();
                if (t - lastTouchTime < DOUBLE_CLICK_INTERVAL) {
                    gMapController.zoomIn();
                }
                lastTouchTime = t;
            }
            return super.onTouchEvent(e, mapView);
        }
    }

    /**
     * 位置变化listener
     * 
     * @author Trinea 2012-9-19 上午11:10:28
     */
    public class GMapLocationListener implements LocationListener {

        /**
         * 位置更新
         */
        @Override
        public void onLocationChanged(Location location) {
            GeoPoint gp = new GeoPoint((int)(location.getLatitude() * 1E6),
                                       (int)(location.getLongitude() * 1E6));
            gMapController.animateTo(gp);
            gMapController.setZoom(ZOOM_RATE);
            Toast.makeText(getApplication(), "位置已更新", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    }

    /**
     * 从经纬度坐标获得地址列表
     **/
    public String getAddressFromGeoPoint(GeoPoint p) {
        StringBuilder s = new StringBuilder();
        Geocoder gc = new Geocoder(getApplication(), Locale.getDefault());
        try {
            // 根据坐标地址搜索地址
            List<Address> l = gc.getFromLocation(p.getLatitudeE6() / 1E6, p.getLongitudeE6() / 1E6,
                                                 MAX_ADDR_RESULT_NUM);
            if (l != null && l.size() > 0) {
                for (Address a : l) {
                    for (int i = 0; i <= a.getMaxAddressLineIndex(); i++) {
                        s.append(a.getAddressLine(i));
                    }
                    s.append("\n");
                }
            }
        } catch (IOException e) {
            Toast.makeText(getApplication(), "获取地址异常", Toast.LENGTH_SHORT).show();
        }
        return s.toString();
    }

    /**
     * 从地址获得经纬度坐标
     **/
    public List<GeoPoint> getGeoPointFromAddress(String address) {
        List<GeoPoint> gpList = new ArrayList<GeoPoint>();
        Geocoder gc = new Geocoder(getApplication(), Locale.getDefault());
        try {
            // 根据具体位置搜索地址
            List<Address> l = gc.getFromLocationName(address, MAX_ADDR_RESULT_NUM);
            if (l != null && l.size() > 0) {
                for (Address a : l) {
                    for (int i = 0; i < a.getMaxAddressLineIndex(); i++) {
                        gpList.add(new GeoPoint((int)(a.getLatitude() * 1E6),
                                                (int)(a.getLongitude() * 1E6)));
                    }
                }
            }
        } catch (IOException e) {
            Toast.makeText(getApplication(), "获取地址异常", Toast.LENGTH_SHORT).show();
        }
        return gpList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
        }
        return false;
    }
}
