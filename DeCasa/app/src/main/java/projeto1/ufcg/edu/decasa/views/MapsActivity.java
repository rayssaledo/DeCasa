package projeto1.ufcg.edu.decasa.views;

import android.location.Address;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import projeto1.ufcg.edu.decasa.R;
import projeto1.ufcg.edu.decasa.controllers.ProfessionalController;
import projeto1.ufcg.edu.decasa.models.Professional;
import projeto1.ufcg.edu.decasa.utils.MainMapFragment;
import projeto1.ufcg.edu.decasa.utils.MySharedPreferences;

public class MapsActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    public static final String TAG = MapsActivity.class.getSimpleName();
    private MainMapFragment mapFragment;
    private List<Professional> professionals;
    private HashMap<Marker, Professional> professionalMarkerMap;
    private ProfessionalController professionalController;
    private MySharedPreferences mySharedPreferences;
    private String service;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 100) {
                setUpProfessionalsMarker();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        professionalController = new ProfessionalController(MapsActivity.this);

        Intent it = getIntent();
        service = (String) it.getSerializableExtra("PROFESSIONALSERVICE");

        professionals = professionalController.getProfessionalsByService(service, handler);
        Log.d("ServiçoMaps:", service+"");

        mapFragment = new MainMapFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.map, mapFragment);
        ft.commit();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        setMaps(service);
    }

    private void setMaps(String service) {
        if (service.equals(getApplication().getString(R.string.title_electricians))){
            professionals = professionalController.getProfessionalsByService("Eletricista", handler);
        } else  if (service.equals(getApplication().getString(R.string.title_plumbers))){
            professionals = professionalController.getProfessionalsByService("Encanador", handler);
        } else {
            professionals = professionalController.getProfessionalsByService("Montador", handler);
        }
        setUpProfessionalsMarker();
    }

    private void setUpProfessionalsMarker() {
        professionalMarkerMap = new HashMap<Marker, Professional>();
        Marker marker;
        //LatLng latLng;
        Log.d("LISTAMAPS", professionals.size()+"");
        for (Professional prof : professionals) {
            //latLng = new LatLng(prof.getLocation().getLatitude(), prof.getLocation().getLongitude());
            Log.d("LATLNG", getLatLng(prof.getLocation().getProvider())+"");
            marker = mapFragment.placeMarker(prof, getLatLng(prof.getLocation().getProvider()));
            professionalMarkerMap.put(marker, prof);
        }
    }

    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private void handleNewLocation(Location location) {
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        mapFragment.placeMarker(latLng);
        mapFragment.getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onConnected(Bundle bundle) {
        loadLocationsOnMap();
    }

    private void loadLocationsOnMap() {
        mapFragment.getMap().clear();
        if (mLastLocation == null) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
        }

        if  ( mLastLocation !=  null )  {
            setUpProfessionalsMarker();
            handleNewLocation(mLastLocation);

        } else {
            Log.i("MY LOCATION", "NULL");
        }
    }

    public LatLng getLatLng(String location){
        List<Address> addressList = null;
        if(location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location , 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude() , address.getLongitude());

            return latLng;
        }
        return new LatLng(0, 0);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    protected  void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected  void onStop ()  {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

    }


}
