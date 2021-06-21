package com.example.householderproject.intentview;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.householderproject.R;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class MapViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        Intent intent = getIntent();

        String locationX = intent.getExtras().getString("locationX", "");
        String locationY = intent.getExtras().getString("locationY", "");
        String location = intent.getExtras().getString("location", "");

        if(location.equals("") || locationX.equals("") || locationY.equals("")) {

            Toast.makeText(this, "표시 할 수 없는 위치 입니다.", Toast.LENGTH_LONG).show();
            finish();

        } else {

            MapView mapView = new MapView(this);

            mapView.setDaumMapApiKey("b20e3d8b588ecf47154a131332b5c7d5");
            ViewGroup mapViewGroup = findViewById(R.id.mapView);

            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(Double.parseDouble(locationY), Double.parseDouble(locationX));
            mapView.setMapCenterPoint(mapPoint, true);
            mapViewGroup.addView(mapView);

            MapPOIItem marker = new MapPOIItem();
            marker.setItemName(location);
            marker.setTag(0);
            marker.setMapPoint(mapPoint);
            marker.setMarkerType(MapPOIItem.MarkerType.YellowPin);
            mapView.addPOIItem(marker);

        }

    }

}
