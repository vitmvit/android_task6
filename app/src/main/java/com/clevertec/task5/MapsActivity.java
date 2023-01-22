package com.clevertec.task5;

import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.clevertec.task5.api.service.ApiService;
import com.clevertec.task5.databinding.ActivityMapsBinding;
import com.clevertec.task5.model.pojo.Marker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import static com.clevertec.task5.constants.Constants.*;
import static com.clevertec.task5.util.MarkerUtils.getSortedList;

/*
 * В качестве слоя ViewModel выступает класс ApiService
 * */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        ApiService model = new ViewModelProvider(this).get(ApiService.class);
        LiveData<List<Marker>> markerList = model.getMarkers(googleMap, DEFAULT_LATITUDE_COORD, DEFAULT_LONGITUDE_COORD);
        markerList.observe(this, new Observer<List<Marker>>() {

            @Override
            public void onChanged(List<Marker> markers) {

                mMap.addMarker(
                        new MarkerOptions()
                                .position(new LatLng(DEFAULT_LATITUDE_COORD, DEFAULT_LONGITUDE_COORD))
                                .title(SELECT_TITLE)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_red))
                                .zIndex(1)
                );

                markers = getSortedList(markers, COUNT_MARKERS);

                if (markers.size() != 0) {
                    for (Marker m : markers) {
                        mMap.addMarker(
                                new MarkerOptions()
                                        .position(new LatLng(Double.parseDouble(m.getGpsX()), Double.parseDouble(m.getGpsY())))
                                        .title(m.getTypeObject())
                                        .snippet(m.getSnippet())
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_blue))

                        );
                    }
                } else {
                    Toast.makeText(MapsActivity.this, NO_OBJECT_IN_CITY_ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}