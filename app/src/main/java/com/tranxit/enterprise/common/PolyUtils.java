package com.tranxit.enterprise.common;

import android.content.Context;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.goride.provider.R;

import java.util.ArrayList;
import java.util.List;


public class PolyUtils {
    private PolylineOptions polylineOptions;
    private GoogleMap googleMap;
    private Context context;

    public PolyUtils(Context context, GoogleMap googleMap, String encodedPolyPoints) {
        this.context = context;
        this.googleMap = googleMap;
        List<LatLng> polyz = decodePolyPoints(encodedPolyPoints);
        polylineOptions = new PolylineOptions()
                .clickable(true)
                .addAll(polyz)
                .color(context.getResources().getColor(R.color.colorAccent))
                .width(5);
    }


    public void start() {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : polylineOptions.getPoints()) {
            builder.include(latLng);
        }
        final LatLngBounds bounds = builder.build();


        final LatLng origin = polylineOptions.getPoints().get(0);
        final LatLng destination = polylineOptions.getPoints().get(polylineOptions.getPoints().size() - 1);

        googleMap.setOnMapLoadedCallback(() -> {
            googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.src_icon)).position(origin));
            googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.des_icon)).position(destination));
            googleMap.addPolyline(polylineOptions);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        });
    }


    private ArrayList<LatLng> decodePolyPoints(String encodedPath) {
        int len = encodedPath.length();

        final ArrayList<LatLng> path = new ArrayList<LatLng>();
        int index = 0;
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int result = 1;
            int shift = 0;
            int b;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            result = 1;
            shift = 0;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            path.add(new LatLng(lat * 1e-5, lng * 1e-5));
        }

        return path;
    }
}
