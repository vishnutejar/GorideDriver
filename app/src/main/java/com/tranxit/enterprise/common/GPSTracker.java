package com.tranxit.enterprise.common;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.tranxit.enterprise.driver.R;
import com.tranxit.enterprise.common.kalman.KalmanLocationManager;
import com.tranxit.enterprise.ui.activity.main.MainActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by santhosh@appoets.com on 11-10-2017.
 */


public class GPSTracker extends Service{

    String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";

    /**
     * Request location updates with the highest possible frequency on gps.
     * Typically, this means one update per second for gps.
     */
    private static final long GPS_TIME = 1000;

    /**
     * For the network provider, which gives locations with less accuracy (less reliable),
     * request updates every 5 seconds.
     */
    private static final long NET_TIME = 5000;

    /**
     * For the filter-time argument we use a "real" value: the predictions are triggered by a timer.
     * Lets say we want 5 updates (estimates) per second = update each 200 millis.
     */
    private static final long FILTER_TIME = 1000;

    private KalmanLocationManager mKalmanLocationManager;


    private static final String TAG = "GPSTracker";
    private LocationManager locationManager = null;

    String status_update = "Init";
    Context context;

    /**
     * Listener used to get updates from KalmanLocationManager (the good old Android LocationListener).
     */
    private android.location.LocationListener mLocationListener = new android.location.LocationListener() {

        @Override
        public void onLocationChanged(Location location) {


//            Log.e("onLocationChanged", "onLocationChanged"+latLng);


            /*// GPS location
            if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {

            }

            // Network location
            if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {

            }*/

            // If Kalman location and google maps activated the supplied mLocationSource
            if (location.getProvider().equals(KalmanLocationManager.KALMAN_PROVIDER)) {

                //LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                Log.d("MYLOcation", location.getLatitude() +" "+location.getLongitude());
                EventBus.getDefault().postSticky(location);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.OUT_OF_SERVICE:
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    break;
                case LocationProvider.AVAILABLE:
                    break;
            }
        }

        @Override
        public void onProviderEnabled(String provider) {

//            Toast.makeText(context, String.format("Provider '%s' enabled", provider), Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onProviderDisabled(String provider) {

//            Toast.makeText(context, String.format("Provider '%s' disabled", provider), Toast.LENGTH_SHORT).show();

        }
    };


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        status_update = "Service Starts";

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            startMyOwnForeground();
        } else {

            Intent notificationIntent = new Intent(this, MainActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);

            Notification notification = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(getString(R.string.app_name)+"- Current location sharing")
                    .setContentIntent(pendingIntent).build();

            startForeground(1, notification);
        }

        return START_STICKY;
    }


    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        context = getBaseContext();

        mKalmanLocationManager = new KalmanLocationManager(context);
        initializeLocationManager();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "location provider requires ACCESS_FINE_LOCATION | ACCESS_COARSE_LOCATION");
            return;
        }


        mKalmanLocationManager.requestLocationUpdates(
                KalmanLocationManager.UseProvider.GPS_AND_NET, FILTER_TIME, GPS_TIME, NET_TIME, mLocationListener, true);


    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        status_update = "Service Task destroyed onDestroy";

    }

    private void initializeLocationManager() {
        Log.d(TAG, "initializeLocationManager");
        if (locationManager == null) {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        status_update = "Service Task destroyed onTaskRemoved";


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String channelName = "Current location sharing";
        @SuppressLint("InlinedApi") NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle(getString(R.string.app_name)+ "- Current location sharing")
                .setPriority(NotificationManager.IMPORTANCE_MAX)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setOngoing(true)
                .setAutoCancel(false)
                .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(), 0))
                .build();
        notification.flags = Notification.FLAG_ONGOING_EVENT;

        startForeground(2, notification);
    }

}

/*public class GPSTracker extends Service {
    private static final String TAG = "GPSTracker";
    private LocationManager locationManager = null;
    public static final String CHANNEL_ID = "test_channel";

    String status_update="Init";


    private static final int LOCATION_INTERVAL = 1000 * 10 * 1; // 1 minute
    private static final float LOCATION_DISTANCE = 10; // 10 meters


 *//*   private static final int LOCATION_INTERVAL = 0; // 1 minute
    private static final float LOCATION_DISTANCE =0; // 10 meters*//*
    Context context;

    private class LocationListener implements android.location.LocationListener {

        public LocationListener(String provider) {
            Log.d(TAG, "LocationListener  " + provider);
            //GlobalData.CURRENT_LOCATION = new Location(provider);
        }

        @Override
        public void onLocationChanged(final Location location) {
            Log.d(TAG, "onLocationChanged: " + location);

            EventBus.getDefault().post(location);

            //updateLocationToServer();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "onStatusChanged: " + provider);
            //Toast.makeText(LocationTracking.this, "s_Distance : "+LocationTracking.distance, Toast.LENGTH_SHORT).show();
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");



        //super.onStartCommand(intent, flags, startId);
       // Toast.makeText(getApplicationContext(), "Service Starts", Toast.LENGTH_SHORT).show();

        status_update =  "Service Starts";

        *//*if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
*//*
        return START_STICKY;
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground()
    {
        String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
        String channelName = "My Background Service";
        @SuppressLint("InlinedApi") NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_visa)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MAX)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setOngoing(true)
                .setAutoCancel(false)
                .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(), 0))
                .build();
        notification.flags = Notification.FLAG_ONGOING_EVENT;

        startForeground(2, notification);
    }


    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        context = getBaseContext();
        initializeLocationManager();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "location provider requires ACCESS_FINE_LOCATION | ACCESS_COARSE_LOCATION");
            return;
        }


        try {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }


        try {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();


       *//* if (locationManager != null) {
            for (LocationListener mLocationListener : mLocationListeners) {
                try {
                    locationManager.removeUpdates(mLocationListener);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listeners, ignore", ex);
                }
            }
        }*//*


        status_update =  "Service Task destroyed onDestroy";

       // Toast.makeText(getApplicationContext(), status_update, Toast.LENGTH_LONG).show();
        //  Toast.makeText(getApplicationContext(), "Start Alarm", Toast.LENGTH_SHORT).show();


        Intent myIntent = new Intent(getApplicationContext(), GPSTracker.class);

        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, myIntent, 0);

        AlarmManager alarmManager1 = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.add(Calendar.SECOND, 10);

        alarmManager1.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }

    private void initializeLocationManager() {
        Log.d(TAG, "initializeLocationManager");
        if (locationManager == null) {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        status_update =  "Service Task destroyed onTaskRemoved";
        //Toast.makeText(getApplicationContext(), status_update, Toast.LENGTH_SHORT).show();

        super.onTaskRemoved(rootIntent);

        Intent myIntent = new Intent(getApplicationContext(), GPSTracker.class);

        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, myIntent, 0);

        AlarmManager alarmManager1 = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.add(Calendar.SECOND, 10);

        alarmManager1.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }

}*/
