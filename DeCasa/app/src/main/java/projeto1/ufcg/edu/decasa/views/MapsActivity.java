package projeto1.ufcg.edu.decasa.views;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import projeto1.ufcg.edu.decasa.R;
import projeto1.ufcg.edu.decasa.controllers.ProfessionalController;
import projeto1.ufcg.edu.decasa.models.Professional;
import projeto1.ufcg.edu.decasa.utils.MainMapFragment;
import projeto1.ufcg.edu.decasa.utils.MapWrapperLayout;
import projeto1.ufcg.edu.decasa.utils.MySharedPreferences;

import android.os.Handler;

public class MapsActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    public static final String TAG = MainActivity.class.getSimpleName();
    private List<Professional> professionals;
    private TextView tv_profession;
    private TextView tv_name_professional;
    private TextView tv_cpf;
    private HashMap<Marker, Professional> professionalMarkerMap;

    private ViewGroup infoWindow;
    private MainMapFragment mapFragment;
    private MapWrapperLayout mapWrapperLayout;

    private ProfessionalController professionalController;
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

//        Intent i = getIntent();
//        service = (String) i.getSerializableExtra("SERVICEA");

        Intent it = getIntent();
        professionals = (List<Professional>) it.getSerializableExtra("PROFESSIONALSERVICE");
        Log.d("LISTPROFESSIONAL", professionals.size()+"");

        mapFragment = new MainMapFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.map, mapFragment);
        ft.commit();

        mapWrapperLayout = (MapWrapperLayout) findViewById(R.id.map_relative_layout);


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        //professionals = createProfessionals();
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


    private void setUpProfessionalsMarker() {

        professionalMarkerMap = new HashMap<Marker, Professional>();
        Marker marker;
        LatLng latLng;
        for (Professional prof: professionals) {
           // latLng = new LatLng(prof.getLocation().getLatitude(),prof.getLocation().getLongitude());
           // Log.d("LATLNG", latLng+"");
           // Log.d("PROF", prof.getName());
            //marker = mapFragment.placeMarker(prof, latLng);
          //  Log.d("SERVICE2", service);
            marker = mapFragment.placeMarker(prof, getLatLng(prof.getLocation().getProvider()));
            Log.d("LATING", getLatLng(prof.getLocation().getProvider()) + " ");
            professionalMarkerMap.put(marker, prof);
        }

        mapFragment.getMap().setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                final Professional professionalInfo = professionalMarkerMap.get(marker);

                mapWrapperLayout.init(mapFragment.getMap(), getPixelsFromDp(MapsActivity.this, 39 + 20));

                infoWindow = null;

                if (!marker.getTitle().equals("Minha localização")) {
                    infoWindow = (ViewGroup) getLayoutInflater().inflate(R.layout.infowindow_professional, null);

                    tv_profession = (TextView) infoWindow.findViewById(R.id.tv_profession);
                    tv_profession.setText(professionalInfo.getServices()[0]);

                    tv_name_professional = (TextView) infoWindow.findViewById(R.id.tv_name_professional);
                    tv_name_professional.setText(professionalInfo.getName());

                    tv_cpf = (TextView) infoWindow.findViewById(R.id.tv_cpf);
                    tv_cpf.setText(professionalInfo.getCpf());
                }
                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                return infoWindow;
            }
        });

    }
    public void setView(Context context, Class classe){
        Intent it = new Intent();
        it.setClass(context, classe);
        startActivity(it);
    }

    private List<Professional> createProfessionals() {
        if (service.equals(getApplication().getString(R.string.title_electricians))){
            professionals = professionalController.getProfessionalsByService("Eletricista", handler);
        } else  if (service.equals(getApplication().getString(R.string.title_plumbers))){
            professionals = professionalController.getProfessionalsByService("Encanador", handler);
        } else {
            professionals = professionalController.getProfessionalsByService("Montador", handler);
        }
        return professionals;
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

}