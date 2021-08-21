package com.tranxit.enterprise.ui.activity.location_pick;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.tranxit.enterprise.base.BaseActivity;
import com.tranxit.enterprise.common.RecyclerItemClickListenerNew;
import com.tranxit.enterprise.data.network.model.Address;
import com.tranxit.enterprise.data.network.model.AddressResponse;
import com.tranxit.enterprise.driver.R;
import com.tranxit.enterprise.ui.adapter.PlacesAutoCompleteAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tranxit.enterprise.MvpApplication.DEFAULT_ZOOM;
import static com.tranxit.enterprise.MvpApplication.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.tranxit.enterprise.MvpApplication.mLastKnownLocation;


public class LocationPickActivity extends BaseActivity implements OnMapReadyCallback,
        GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationPickIView, RecyclerItemClickListenerNew.OnItemClickListener {

    private static final String TAG = LocationPickActivity.class.getSimpleName();
    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(
            new LatLng(-0, 0), new LatLng(0, 0));
    private final LatLng mDefaultLocation = new LatLng(21.162879, 79.087623);
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.source)
    EditText source;
    @BindView(R.id.destination)
    EditText destination;
    @BindView(R.id.destination_layout)
    LinearLayout destinationLayout;
    @BindView(R.id.home_address_layout)
    LinearLayout homeAddressLayout;
    @BindView(R.id.work_address_layout)
    LinearLayout workAddressLayout;
    @BindView(R.id.home_address)
    TextView homeAddress;
    @BindView(R.id.work_address)
    TextView workAddress;
    @BindView(R.id.locations_rv)
    RecyclerView locationsRv;
    @BindView(R.id.location_bs_layout)
    CardView locationBsLayout;
    @BindView(R.id.dd)
    CoordinatorLayout dd;
    SupportMapFragment mapFragment;
    GoogleMap googleMap;
    BottomSheetBehavior bottomSheetBehavior;
    Boolean isEditable = true;
    Address home, work = null;
    List<Object> list = new ArrayList<>();
    EditText selectedEditText;
    boolean isEnableIdle = false;
    LinearLayoutManager mLinearLayoutManager;
    private boolean mLocationPermissionGranted;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationPickPresenter<LocationPickActivity> presenter = new LocationPickPresenter<>();
    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    private PlacesClient placesClient;
    private AutocompleteSessionToken token;
    private RectangularBounds bounds;

    private TextWatcher filterTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if (isEditable && !s.toString().isEmpty() && s.length() > 2 && placesClient != null) {
                getAutocompletePredictions(s.toString());
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED)
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else
                locationsRv.setVisibility(View.GONE);
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_location_pick;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void initView() {
        // Initialize Places.
        Places.initialize(getApplicationContext(),
                getResources().getString(R.string.google_map_key));

        // Create a new Places client instance.
        placesClient = Places.createClient(this);

        // Create a new token for the autocomplete session. Pass this to
        // FindAutocompletePredictionsRequest, and once again when the user makes a selection
        // (for example when calling fetchPlace()).
        token = AutocompleteSessionToken.newInstance();

        // Create a RectangularBounds object.
        bounds = RectangularBounds.newInstance(BOUNDS_INDIA);

        ButterKnife.bind(this);
        presenter.attachView(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        bottomSheetBehavior = BottomSheetBehavior.from(locationBsLayout);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        mAutoCompleteAdapter = new PlacesAutoCompleteAdapter(this);

        mLinearLayoutManager = new LinearLayoutManager(this);
        locationsRv.setLayoutManager(mLinearLayoutManager);
        locationsRv.addOnItemTouchListener(new RecyclerItemClickListenerNew(this, this));
        mAutoCompleteAdapter = new PlacesAutoCompleteAdapter(this);
        locationsRv.setAdapter(mAutoCompleteAdapter);

        source.addTextChangedListener(filterTextWatcher);
        destination.addTextChangedListener(filterTextWatcher);

        source.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                selectedEditText = source;
            }
        });
        destination.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                selectedEditText = destination;
            }
        });

        destination.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
                return true;
            }
            return false;
        });

        if (RIDE_REQUEST.containsKey("s_address")) {
            isEditable = false;
            source.setText(String.valueOf(RIDE_REQUEST.get("s_address")));
            isEditable = true;
        }
        if (RIDE_REQUEST.containsKey("d_address")) {
            isEditable = false;
            destination.setText(String.valueOf(RIDE_REQUEST.get("d_address")));
            isEditable = true;
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            boolean isHideDestination = extras.getBoolean("isHideDestination", false);
            destinationLayout.setVisibility(isHideDestination ? View.GONE : View.VISIBLE);

            boolean desFocus = extras.getBoolean("desFocus", false);
            if (desFocus) {
                destination.requestFocus();
            }
        }

        presenter.address();
    }

    private void setLocationText(String address, LatLng latLng) {
        if (address != null && latLng != null) {
            isEditable = false;
            selectedEditText.setText(address);
            isEditable = true;

            if (selectedEditText.getTag().equals("source")) {
                RIDE_REQUEST.put("s_address", address);
                RIDE_REQUEST.put("s_latitude", latLng.latitude);
                RIDE_REQUEST.put("s_longitude", latLng.longitude);
            }

            if (selectedEditText.getTag().equals("destination")) {
                RIDE_REQUEST.put("d_address", address);
                RIDE_REQUEST.put("d_latitude", latLng.latitude);
                RIDE_REQUEST.put("d_longitude", latLng.longitude);
            }
        } else {
            isEditable = false;
            selectedEditText.setText("");
            isEditable = true;

            if (selectedEditText.getTag().equals("source")) {
                RIDE_REQUEST.remove("s_address");
                RIDE_REQUEST.remove("s_latitude");
                RIDE_REQUEST.remove("s_longitude");
            }
            if (selectedEditText.getTag().equals("destination")) {
                RIDE_REQUEST.remove("d_address");
                RIDE_REQUEST.remove("d_latitude");
                RIDE_REQUEST.remove("d_longitude");
            }
        }
    }

    @OnClick({R.id.source, R.id.destination, R.id.reset_source, R.id.reset_destination,
            R.id.home_address_layout, R.id.work_address_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.source:
                break;
            case R.id.destination:
                break;
            case R.id.reset_source:
                selectedEditText = source;
                source.requestFocus();
                setLocationText(null, null);
                break;
            case R.id.reset_destination:
                destination.requestFocus();
                selectedEditText = destination;
                setLocationText(null, null);
                break;
            case R.id.home_address_layout:
                if (home != null) {
                    setLocationText(home.getAddress(), new LatLng(home.getLatitude(),
                            home.getLongitude()));
                }
                break;
            case R.id.work_address_layout:
                if (work != null) {
                    setLocationText(work.getAddress(), new LatLng(work.getLatitude(),
                            work.getLongitude()));
                }
                break;
        }
    }

    @Override
    public void onCameraIdle() {
        try {
            CameraPosition cameraPosition = googleMap.getCameraPosition();
            if (isEnableIdle) {
                String address = getAddress(cameraPosition.target);
                System.out.println("onCameraIdle " + address);
                setLocationText(address, cameraPosition.target);
            }
            isEnableIdle = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCameraMove() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        try {
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,
                    R.raw.style_json));
        } catch (Resources.NotFoundException e) {
            Log.d("Map:Style", "Can't find style. Error: ");
        }
        this.googleMap = googleMap;
        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();
    }

    void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        // Set the map's camera position to the current location of the device.
                        mLastKnownLocation = task.getResult();
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                    } else {
                        Log.d("Map", "Current location is null. Using defaults.");
                        Log.e("Map", "Exception: %s", task.getException());
                        googleMap.moveCamera(CameraUpdateFactory
                                .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void updateLocationUI() {
        if (googleMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                googleMap.setOnCameraMoveListener(this);
                googleMap.setOnCameraIdleListener(this);
            } else {
                googleMap.setMyLocationEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    updateLocationUI();
                }
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v("Google API Callback", "Connection Suspended");
        Log.v("Code", String.valueOf(i));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.v("Error Code", String.valueOf(connectionResult.getErrorCode()));
        Toast.makeText(this, "API_NOT_CONNECTED", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.location_pick_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSuccess(AddressResponse address) {
        if (address.getHome().isEmpty()) {
            homeAddressLayout.setVisibility(View.GONE);
        } else {
            home = address.getHome().get(address.getHome().size() - 1);
            homeAddress.setText(home.getAddress());
            homeAddressLayout.setVisibility(View.VISIBLE);
        }

        if (address.getWork().isEmpty()) {
            workAddressLayout.setVisibility(View.GONE);
        } else {
            work = address.getWork().get(address.getWork().size() - 1);
            workAddress.setText(work.getAddress());
            workAddressLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onError(Throwable e) {

    }

    private void getAutocompletePredictions(String query) {
        // Use the builder to create a FindAutocompletePredictionsRequest.
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                // Call either setLocationBias() OR setLocationRestriction().
                .setLocationBias(bounds)
                //.setLocationRestriction(bounds)
                .setTypeFilter(TypeFilter.GEOCODE)
                .setSessionToken(token)
                .setQuery(query)
                .build();
        placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener((response) -> {
                    mAutoCompleteAdapter.setPredictions(response.getAutocompletePredictions());
                    mAutoCompleteAdapter.notifyDataSetChanged();
                    locationsRv.setVisibility(View.VISIBLE);
                })
                .addOnFailureListener((exception) -> {
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                        Toast.makeText(getApplicationContext(), apiException.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onItemClick(View view, int position) {
        String locationName = mAutoCompleteAdapter.getPredictions()
                .get(position).getFullText(null).toString();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<android.location.Address> addresses = geocoder.getFromLocationName(locationName,
                    1);
            if (!addresses.isEmpty())
                setLocationText(locationName, new LatLng(addresses.get(0).getLatitude(),
                        addresses.get(0).getLongitude()));
        } catch (IOException e) {
            e.printStackTrace();
            Log.println(Log.ERROR, TAG, e.toString());
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
