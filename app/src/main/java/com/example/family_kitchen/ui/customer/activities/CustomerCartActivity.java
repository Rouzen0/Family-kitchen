package com.example.family_kitchen.ui.customer.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

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

import com.example.family_kitchen.R;
import com.example.family_kitchen.adapters.CustomerCartItemsAdapter;
import com.example.family_kitchen.databinding.ActivityCustomerCartBinding;
import com.example.family_kitchen.interfaces.SetTotalOrderPrice;
import com.example.family_kitchen.model.Cart;
import com.example.family_kitchen.preference.SharedPref;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
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

public class CustomerCartActivity extends AppCompatActivity implements View.OnClickListener , SetTotalOrderPrice , OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private ActivityCustomerCartBinding binding;
    CustomerCartItemsAdapter customerCartItemsAdapter;
    ArrayList<Cart> itemArrayCartList;
    DatabaseReference db_cart,db_user;
    String user_id;

    SharedPref pref;

    String order_id;
    int orderTotalPrice;


    Geocoder geo;
    private GoogleApiClient client;
    private static final int REQUEST_LOCATION = 1;
    private GoogleMap mMap;

    private FirebaseAuth firebaseAuth;
    double selectedLatitude;
    double selectedLongitude;
    LocationManager locationManager;
    String locationAddress ;
    private String uid,name,phoneNumber,emailAddress;
    SupportMapFragment mapFragment;

    public final int REQUEST_LOCATION_CODE = 99;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCustomerCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.imageViewBack.setOnClickListener(this);
        binding.clPlaceOrder.setOnClickListener(this);

        pref = new SharedPref(this);
        itemArrayCartList=new ArrayList<>();
        user_id = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        db_cart = FirebaseDatabase.getInstance().getReference("Order");
        db_user= FirebaseDatabase.getInstance().getReference("User");
        order_id=pref.getOrderId();
        fetchItemsData();

        uid=FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    private void fetchItemsData() {
        binding.progressbarItems.setVisibility(View.VISIBLE);
        db_cart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clearing the previous User list
                itemArrayCartList.clear();
                orderTotalPrice=0;
                //getting all nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Cart cart = postSnapshot.getValue(Cart.class);
                    if(itemArrayCartList!=null && user_id.equals(cart.getUserId())&& (cart.getOrderStatus().equals("customer"))){

                        orderTotalPrice += Integer.parseInt(cart.getTotalPrice());
                        itemArrayCartList.add(cart);
                    }
                }

                binding.textViewOrderTotal.setText(String.valueOf(orderTotalPrice)+" SR");
                binding.textviewPlaceOrderPrice.setText(String.valueOf(orderTotalPrice+10));

                customerCartItemsAdapter = new CustomerCartItemsAdapter(CustomerCartActivity.this, itemArrayCartList);
                binding.rvItems.setLayoutManager(new GridLayoutManager(CustomerCartActivity.this, 1));
                binding.rvItems.setAdapter(customerCartItemsAdapter);

                binding.progressbarItems.setVisibility(View.GONE);

                if(itemArrayCartList.size()>0){
                    binding.textViewNoItemFound.setVisibility(View.GONE);
                }
                else{
                    binding.textViewNoItemFound.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();

        if(id==R.id.imageView_back){
            finish();
        } else if(id==R.id.cl_place_order){

            db_cart.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //clearing the previous User list
                    //getting all nodes
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Cart cart = postSnapshot.getValue(Cart.class);
                        if(itemArrayCartList!=null && user_id.equals(cart.getUserId())
                                && cart.getOrderStatus().equals("customer") && cart.getOrderId().equals(order_id)){

                            Map<String, Object> updateValues = new HashMap<String, Object>();
                            updateValues.put("orderStatus", "family");
                            updateValues.put("totalOrderPrice", String.valueOf(orderTotalPrice+10));
                            db_cart.child(cart.getOrderItemId()).updateChildren(updateValues);
                            pref.setOrderId("");

                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });


            Toast.makeText(this, "Order Placed", Toast.LENGTH_SHORT).show();
            finishAffinity();
            startActivity(new Intent(this,CustomerHomeActivity.class));

        }
    }


    @Override
    public void setTotalOrderPrice(String totalOrderPrice) {
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocation();
        }

        geo = new Geocoder(this, Locale.getDefault());
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            bulidGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        else{
            checkLocationPermission();

        }
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                mMap.clear();

            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                selectedLatitude=mMap.getCameraPosition().target.latitude;
                selectedLongitude=mMap.getCameraPosition().target.longitude;


                if (geo == null)
                    geo = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> address = geo.getFromLocation(selectedLatitude, selectedLongitude, 1);

                    if(address.size()!=0) {
                        locationAddress = String.valueOf(address.get(0).getAddressLine(0));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double lng = locationGPS.getLongitude();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 8.0f));


                Map<String, Object> updateValues = new HashMap<String, Object>();
                updateValues.put("longitude", String.valueOf(lng));
                updateValues.put("latitude", String.valueOf(lat));
                db_user.child(uid).updateChildren(updateValues);

            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected synchronized void bulidGoogleApiClient() {
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        client.connect();

    }

    public boolean checkLocationPermission()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED ){

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION },REQUEST_LOCATION_CODE);
            }
            else {
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION },REQUEST_LOCATION_CODE);
            }
            return false;
        }
        else {
            return true;
        }
    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}