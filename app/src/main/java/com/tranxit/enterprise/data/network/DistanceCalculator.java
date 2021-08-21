package com.tranxit.enterprise.data.network;
import com.google.android.gms.maps.model.LatLng;

import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class DistanceCalculator {

    static double PI_RAD = Math.PI / 180.0;

    /**
     * Use Great Circle distance formula to calculate distance between 2 coordinates in meters.
     */
    public double greatCircleInFeet(LatLng latLng1, LatLng latLng2) {
        return greatCircleInKilometers(latLng1.latitude, latLng1.longitude, latLng2.latitude,
                latLng2.longitude) * 3280.84;
    }

    /**
     * Use Great Circle distance formula to calculate distance between 2 coordinates in meters.
     */
    public static double greatCircleInMeters(LatLng latLng1, LatLng latLng2) {


        double meterValue = greatCircleInKilometers(latLng1.latitude, latLng1.longitude, latLng2.latitude,
                latLng2.longitude) * 1000;

        return Double.isNaN(meterValue) ? 0 : meterValue;
    }

    /**
     * Use Great Circle distance formula to calculate distance between 2 coordinates in kilometers.
     * https://software.intel.com/en-us/blogs/2012/11/25/calculating-geographic-distances-in-location-aware-apps
     */
    public static double greatCircleInKilometers(double lat1, double long1, double lat2, double long2) {
        double phi1 = lat1 * PI_RAD;
        double phi2 = lat2 * PI_RAD;
        double lam1 = long1 * PI_RAD;
        double lam2 = long2 * PI_RAD;

        return 6371.01 * acos(sin(phi1) * sin(phi2) + cos(phi1) * cos(phi2) * cos(lam2 - lam1));
    }
}
