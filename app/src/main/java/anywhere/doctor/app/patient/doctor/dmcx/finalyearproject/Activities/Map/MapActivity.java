package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.sinch.gson.internal.bind.MapTypeAdapterFactory;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.HomeActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ErrorText;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private SupportMapFragment map;
    private SupportPlaceAutocompleteFragment placeAutoComplete;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedClient;
    private LocationRequest mRequest;

    private Marker searchMarker;
    private Marker myMarker;

    private void init() {
        map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        placeAutoComplete = (SupportPlaceAutocompleteFragment) getSupportFragmentManager().findFragmentById(R.id.placeAutoComplete);

        if (placeAutoComplete != null && placeAutoComplete.getView() != null) {
            final EditText searchField = (placeAutoComplete.getView().findViewById(R.id.place_autocomplete_search_input));
            searchField.setTextSize(20.0f);
        }

        if (map != null) {
            map.getMapAsync(this);
        }
    }

    private void event() {
        placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                if (searchMarker != null && searchMarker.isVisible())
                    searchMarker.remove();

                MarkerOptions markerOptions = new MarkerOptions().position(place.getLatLng()).title(place.getName().toString());
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                searchMarker = mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15.0f));

                if (myMarker != null && searchMarker != null) {
                    String origin = myMarker.getPosition().latitude + "," + myMarker.getPosition().longitude;
                    String destination = searchMarker.getPosition().latitude + "," + searchMarker.getPosition().longitude;

                }
            }

            @Override
            public void onError(Status status) {

            }
        });
    }

    private boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
            }, 1111);

            return false;
        }

        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        RefActivity.updateACActivity(this);
        init();
        event();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (checkPermission()) {
            mRequest = LocationRequest.create();
            mRequest.setInterval(1000);
            mRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            mFusedClient = new FusedLocationProviderClient(this);
            mFusedClient.requestLocationUpdates(mRequest, null);
            mFusedClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location == null) {
                        Toast.makeText(RefActivity.refACActivity.get(), ErrorText.LocationNotFound, Toast.LENGTH_SHORT).show();
                    } else {
                        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng, 15.0f);
                        mMap.animateCamera(cameraUpdate);

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(currentLatLng);
                        markerOptions.title("Your Location");
                        myMarker = mMap.addMarker(markerOptions);
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        map = null;
        placeAutoComplete = null;
        mFusedClient = null;
        mRequest = null;
    }

    @Override
    public void onBackPressed() {
        RefActivity.updateACActivity(HomeActivity.instance.get());
        super.onBackPressed();
    }
}
