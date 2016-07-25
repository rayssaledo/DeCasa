package projeto1.ufcg.edu.decasa.utils;

import android.util.Log;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import projeto1.ufcg.edu.decasa.R;
import projeto1.ufcg.edu.decasa.models.Professional;

public class MainMapFragment extends MapFragment {

    public Marker placeMarker(Professional profInfo, LatLng latLng) {

        Marker m = null;
        if (latLng != null) {
          //  if (service.equals("Eletricistas") || service.equals("Electricians")) {
                m = getMap().addMarker(new MarkerOptions()
                        .title(profInfo.getName())
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.electrician_location_icon)));
       //   } else if (service.equals("Encanadores") || service.equals("Plumbers")) {
//                m = getMap().addMarker(new MarkerOptions()
//                        .title(profInfo.getName())
//                        .position(latLng)
//                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.plumber_location_icon)));
//
//            } else if (service.equals("Montadores") || service.equals("Fitters")) {
//                m = getMap().addMarker(new MarkerOptions()
//                        .title(profInfo.getName())
//                        .position(latLng)
//                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.fitter_location_icon)));
//            }
        }
        return m;
    }

    public Marker placeMarker(LatLng latLng) {
        Marker m = getMap().addMarker(new MarkerOptions()
                .title("Minha localização")
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.user_location_icon)));
        return m;
    }
}

