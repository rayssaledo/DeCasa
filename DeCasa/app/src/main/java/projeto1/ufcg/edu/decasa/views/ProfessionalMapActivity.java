package projeto1.ufcg.edu.decasa.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.pkmmte.view.CircularImageView;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import projeto1.ufcg.edu.decasa.R;
import projeto1.ufcg.edu.decasa.controllers.ProfessionalController;
import projeto1.ufcg.edu.decasa.models.Professional;
import projeto1.ufcg.edu.decasa.utils.DownloadFile;
import projeto1.ufcg.edu.decasa.utils.MainMapFragment;
import projeto1.ufcg.edu.decasa.utils.MapWrapperLayout;
import projeto1.ufcg.edu.decasa.utils.MySharedPreferences;
import projeto1.ufcg.edu.decasa.utils.OnInfoWindowElemTouchListener;

public class ProfessionalMapActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    public static final String TAG = MainActivity.class.getSimpleName();
    private TextView tv_profession;
    private CircularImageView iv_professional;
    private TextView tv_name_professional;
    private RatingBar rb_evaluation;
    private Button btn_more_information;
    private Button btn_call;
    private OnInfoWindowElemTouchListener btn_more_listener;
    private OnInfoWindowElemTouchListener btn_call_listener;
    private HashMap<Marker, Professional> professionalMarkerMap;
    private ViewGroup infoWindow;
    private MainMapFragment mapFragment;
    private MapWrapperLayout mapWrapperLayout;
    private String service;
    private MySharedPreferences mySharedPreferences;
    private String address;
    private Professional professional;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_map);

        mySharedPreferences = new MySharedPreferences(getApplicationContext());

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

        Intent it = getIntent();
        professional = it.getParcelableExtra("PROFESSIONALMAP");
    }

    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    private void handleNewLocation(Location location) {
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng;

        if (currentLatitude == 0.0  && currentLongitude == 0.0) {
            latLng = getLatLng(location.getProvider());
            if (latLng.latitude == 0.0 && latLng.longitude == 0.0) {
                final AlertDialog.Builder builder =  new AlertDialog.Builder(ProfessionalMapActivity.this);
                builder.setMessage("Endereço não encontrado!")
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface d, int id) {
                                        service = mySharedPreferences.getService();
                                        Intent intent = new Intent(ProfessionalMapActivity.this,
                                                ProfessionalsActivity.class);
                                        intent.putExtra("SERVICE",service);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                builder.create().show();
            }
        } else {
            latLng = new LatLng(currentLatitude, currentLongitude);
        }

        Log.d("LATLNG", latLng + "");

        if (latLng != null) {
            mapFragment.placeMarker(latLng);
            mapFragment.getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));
        }
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

        if  (mySharedPreferences.getUserLocation() !=  null)  {
            Log.d("USERLOCATION", mySharedPreferences.getUserLocation() + "");

            Location location = new Location(mySharedPreferences.getUserLocation());
            setUpProfessionalsMarker();
            handleNewLocation(location);

        } else if (mLastLocation != null) {
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
        service = mySharedPreferences.getService();
        marker = mapFragment.placeMarker(service, professional, getLatLng(professional.getLocation().
                getProvider()));
        professionalMarkerMap.put(marker, professional);


        mapFragment.getMap().setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                final Professional professionalInfo = professionalMarkerMap.get(marker);

                mapWrapperLayout.init(mapFragment.getMap(), getPixelsFromDp(ProfessionalMapActivity.this,
                        39 + 20));

                infoWindow = null;

                if (!marker.getTitle().equals(getApplication().
                        getString(R.string.text_my_location))) {
                    infoWindow = (ViewGroup) getLayoutInflater().inflate(R.layout.
                            infowindow_professional, null);

                    iv_professional = (CircularImageView) infoWindow.
                            findViewById(R.id.iv_professional);

                    if (professionalInfo.getPicture() != null &&
                            !professionalInfo.getPicture().equals("null")) {
                        File f = new File(DownloadFile.getPathDownload() + File.separator +
                                professionalInfo.getPicture());
                        Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
                        iv_professional.setImageBitmap(bmp);
                    }

                    tv_profession = (TextView) infoWindow.findViewById(R.id.tv_profession);
                    service = mySharedPreferences.getService();
                    if(service.equals(getApplication().getString(R.string.title_electricians))){
                        tv_profession.setText(getApplication().getString(R.string.text_electrician));
                    } else if (service.equals(getApplication().getString(R.string.title_plumbers))) {
                        tv_profession.setText(getApplication().getString(R.string.text_plumber));
                    } else {
                        tv_profession.setText(getApplication().getString(R.string.text_fitter));
                    }

                    tv_name_professional = (TextView) infoWindow.findViewById(R.id.
                            tv_name_professional);
                    tv_name_professional.setText(professionalInfo.getName());

                    rb_evaluation = (RatingBar) infoWindow.findViewById(R.id.
                            rb_evaluation_infowindow);
                    rb_evaluation.setRating(professionalInfo.getAvg());

                    btn_more_information = (Button) infoWindow.findViewById(R.id.
                            btn_more_information);
                    btn_call = (Button) infoWindow.findViewById(R.id.btn_call);
                    btn_more_listener = new OnInfoWindowElemTouchListener(btn_more_information)
                    {
                        @Override
                        protected void onClickConfirmed(View v, Marker marker) {
                            if (mySharedPreferences.isUserLoggedIn()) {
                                Intent intent = new Intent(ProfessionalMapActivity.this,
                                        ProfessionalProfileGoldPlanActivity.class);
                                intent.putExtra("PROFESSIONAL", professionalInfo);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(ProfessionalMapActivity.this,
                                        CadastreOrLoginActivity.class);
                                intent.putExtra("PROFESSIONAL", professionalInfo);
                                startActivity(intent);
                            }
                        }
                    };
                    btn_more_information.setOnTouchListener(btn_more_listener);
                    btn_call_listener = new OnInfoWindowElemTouchListener(btn_call)
                    {
                        @Override
                        protected void onClickConfirmed(View view, Marker marker) {
                            if (mySharedPreferences.isUserLoggedIn()) {
                                String phone = professionalInfo.getPhone1().substring(1, 3) +
                                        professionalInfo.getPhone1().
                                        substring(4, 9) + professionalInfo.getPhone1().substring(9);
                                Uri uri = Uri.parse("tel:" + phone);
                                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(ProfessionalMapActivity.this,
                                        CadastreOrLoginActivity.class);
                                intent.putExtra("PROFESSIONAL", professionalInfo);
                                startActivity(intent);
                            }
                        }
                    };
                    btn_call.setOnTouchListener(btn_call_listener);

                }
                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                return infoWindow;
            }
        });

    }

    public LatLng getLatLng(String location){
        List<Address> addressList = null;
        if(location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location , 1);
                if (addressList.size() > 0) {
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude() , address.getLongitude());
                    return latLng;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new LatLng(0, 0);
    }

    public static void setView(Context context, Class classe){
        Intent it = new Intent();
        it.setClass(context, classe);
        context.startActivity(it);
    }

}