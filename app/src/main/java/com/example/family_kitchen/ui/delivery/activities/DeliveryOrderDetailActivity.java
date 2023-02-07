package com.example.family_kitchen.ui.delivery.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.family_kitchen.R;
import com.example.family_kitchen.databinding.ActivityCustomerCartBinding;
import com.example.family_kitchen.databinding.ActivityDeliveryOrderDetailBinding;
import com.example.family_kitchen.preference.SharedPref;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DeliveryOrderDetailActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, RoutingListener, View.OnClickListener {
    private ActivityDeliveryOrderDetailBinding binding;
    Geocoder geo;
    private GoogleApiClient client;
    private static final int REQUEST_LOCATION = 1;
    private GoogleMap mMap;
    private SharedPref pref;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference db;

    LatLng delivery=null;
    double selectedLatitude;
    double selectedLongitude;
    LocationManager locationManager;
    String locationAddress;
    private String uid, name, phoneNumber, emailAddress;
    SupportMapFragment mapFragment;
    private final static int LOCATION_REQUEST_CODE = 23;
    String customer_longitude, customer_latitude, family_longitude, family_latitude;
    LatLng pickup = null;
    public final int REQUEST_LOCATION_CODE = 99;
    String orderId, customerName, orderDate, customerId, familyId,driverId;

    private List<Polyline> polylines = null;

    boolean locationPermission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDeliveryOrderDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        orderId = getIntent().getStringExtra("order_id");
        customerName = getIntent().getStringExtra("customer_name");
        orderDate = getIntent().getStringExtra("order_date");
        customerId = getIntent().getStringExtra("customer_id");
        familyId = getIntent().getStringExtra("family_id");
        driverId = getIntent().getStringExtra("driver_id");


        binding.textViewName.setText(customerName);
        binding.textViewOrderDateTime.setText(orderDate);
        binding.textViewOrderId.setText("Order Id #" + orderId);
        binding.textViewTextImage.setText(String.valueOf(customerName.charAt(0)));
        binding.buttonChat.setOnClickListener(this);
        binding.imageViewBack.setOnClickListener(this);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        db = FirebaseDatabase.getInstance().getReference("User");


    }


    protected void onStart() {

        super.onStart();


        db.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                family_latitude = dataSnapshot.child(customerId).child("latitude").getValue(String.class);
                family_longitude = dataSnapshot.child(customerId).child("longitude").getValue(String.class);
                customer_latitude = dataSnapshot.child(familyId).child("latitude").getValue(String.class);
                customer_longitude = dataSnapshot.child(familyId).child("longitude").getValue(String.class);

                pickup = new LatLng(Float.parseFloat(family_latitude), Float.parseFloat(family_longitude));
                delivery = new LatLng(Float.parseFloat(customer_latitude), Float.parseFloat(customer_longitude));
                Findroutes(pickup, delivery);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        int id=view.getId();

        if(id==R.id.imageView_back){
            finish();
        } else if(id==R.id.button_chat){

            Intent intent=new Intent(this, DeliveryChatActivity.class);
            intent.putExtra("order_id",orderId);
            intent.putExtra("family_id",familyId);
            intent.putExtra("user_id",customerId);
            intent.putExtra("driver_id",driverId);

            startActivity(intent);

        }
    }


    @Override
    public void onRoutingFailure(RouteException e) {
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        CameraUpdate center = CameraUpdateFactory.newLatLng(pickup);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(8);
        if(polylines!=null) {
            polylines.clear();
        }
        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng=null;
        LatLng polylineEndLatLng=null;


        polylines = new ArrayList<>();
        for (int i = 0; i <route.size(); i++) {

            if(i==shortestRouteIndex)
            {
                polyOptions.color(getResources().getColor(R.color.black));
                polyOptions.width(7);
                polyOptions.addAll(route.get(shortestRouteIndex).getPoints());
                Polyline polyline = mMap.addPolyline(polyOptions);
                polylineStartLatLng=polyline.getPoints().get(0);
                int k=polyline.getPoints().size();
                polylineEndLatLng=polyline.getPoints().get(k-1);
                polylines.add(polyline);

            }
            else {

            }

        }

        //Add Marker on route starting position
        MarkerOptions startMarker = new MarkerOptions();
        startMarker.position(polylineStartLatLng);
        startMarker.title("Family");
        mMap.addMarker(startMarker);

        //Add Marker on route ending position
        MarkerOptions endMarker = new MarkerOptions();
        endMarker.position(polylineEndLatLng);
        endMarker.title("Customer");
        mMap.addMarker(endMarker);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(pickup.latitude, pickup.longitude), 8.0f));
    }

    @Override
    public void onRoutingCancelled() {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    public void Findroutes(LatLng Start, LatLng End)
    {
        if(Start==null || End==null) {
            Toast.makeText(DeliveryOrderDetailActivity.this,"Unable to get location",Toast.LENGTH_LONG).show();
        }
        else
        {

            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(Start, End)
                    .key(getResources().getString(R.string.google_maps_key))
                    .build();
            routing.execute();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermission = true;

                }
                return;
            }
        }
    }

}