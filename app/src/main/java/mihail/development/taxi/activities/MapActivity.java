package mihail.development.taxi.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import mihail.development.taxi.R;
import mihail.development.taxi.data.OkHttp;
import mihail.development.taxi.data.RouteResponse;
import mihail.development.taxi.drawer.RouteDrawer;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;


import static mihail.development.taxi.R.id.map;


public class MapActivity extends FragmentActivity
        implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "Map";
    private GoogleMap googleMap;
    private PlaceAutocompleteFragment autocompleteFragment;
    private PopupWindow popupWindow;
    private LayoutInflater inflater;
    private LocationManager locationManager;
    private Location location;
    private AppCompatButton toDriversList;
    private AppCompatButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        SupportMapFragment mapFragment = new SupportMapFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(map, mapFragment)
                .commit();
        mapFragment.getMapAsync(this);

        GoogleApiClient mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        mGoogleApiClient.connect();

        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setHint("Введите точку прибытия");


        autocompleteFragment.setFilter(new AutocompleteFilter.Builder()
                .setCountry("RU")
                .build()
        );

        toDriversList = (AppCompatButton) findViewById(R.id.btnOrder);
        back = (AppCompatButton) findViewById(R.id.btnBack);


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i(TAG, "Place: " + place.getName());
                googleMap.clear();

                drawRoute(new LatLng(location.getLatitude(),location.getLongitude()), place.getLatLng());

                googleMap.addMarker(new MarkerOptions().position(place.getLatLng()).icon
                        (BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));


                LayoutInflater inflater = getLayoutInflater();
                final View popUpView = inflater.inflate(R.layout.map_pop_up_menu, null);


                final float[] distance = new float[1];
                Location.distanceBetween(location.getLatitude(), location.getLongitude(), place.getLatLng().latitude, place.getLatLng().longitude, distance);
                List<android.location.Address> list;
                String from = null;
                String to = null;
                try {
                    list = new Geocoder(getApplicationContext(), Locale.getDefault()).getFromLocation(location.getLatitude(),
                            location.getLongitude(), 1);
                    from = list.get(0).getAddressLine(0);
                    to = place.getName().toString();

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MapActivity.this);
                SharedPreferences.Editor prfEdit = preferences.edit();
                prfEdit.putString("distance", String.valueOf(distance[0]))
                        .putString("from", from)
                        .putString("to", to)
                        .apply();


                popupWindow = new PopupWindow(popUpView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

                popUpView.findViewById(R.id.btnOrder).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MapActivity.this, DriversListActivity.class));
                    }
                });

                popUpView.findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        googleMap.clear();
                        popupWindow.dismiss();
                    }
                });

                popupWindow.setAnimationStyle(R.style.Animation);
                popupWindow.showAtLocation(findViewById(R.id.mapLayout), Gravity.BOTTOM,
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }



            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.googleMap = map;
        setupMap();
    }

    private void setupMap() {
        if (googleMap != null) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            googleMap.setMyLocationEnabled(true);
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            String provider = locationManager.getBestProvider(new Criteria(), true);
            location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
                autocompleteFragment.setBoundsBias(new LatLngBounds(new LatLng(location.getLatitude() - 0.1, location.getLongitude() - 0.1),
                        new LatLng(location.getLatitude() + 0.1, location.getLongitude() + 0.1)));
            }
        }
    }

    private void drawRoute(final LatLng from, final LatLng to) {

        Observable<List<LatLng>> routeObs = Observable.create(new ObservableOnSubscribe<List<LatLng>>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<List<LatLng>> observableEmitter) throws Exception {
                observableEmitter.onNext(getRoute(from, to));
                observableEmitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        routeObs.subscribe(new DisposableObserver<List<LatLng>>() {
            @Override
            public void onNext(@io.reactivex.annotations.NonNull List<LatLng> latLngs) {
                RouteDrawer drawer = new RouteDrawer();
                drawer.draw(latLngs, googleMap, MapActivity.this);
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    private List<LatLng> getRoute(LatLng from, LatLng to) {
        HttpUrl url = HttpUrl.parse("https://maps.googleapis.com/maps/api/directions/json").newBuilder()
                .addEncodedQueryParameter("origin", String.valueOf(from.latitude)+ "," + String.valueOf(from.longitude))
                .addEncodedQueryParameter("destination", String.valueOf(to.latitude)+ "," + String.valueOf(to.longitude))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        RouteResponse route = null;
        try {
            Response response = OkHttp.CLIENT.newCall(request).execute();
           String resp = response.body().string();
            Gson gson = new GsonBuilder().create();
            JsonParser parser = new JsonParser();
            JsonObject jsonObj = parser.parse(resp).getAsJsonObject();
            route = gson.fromJson(jsonObj, RouteResponse.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return toLatLngs(route);
    }




    private List<LatLng> toLatLngs(RouteResponse route) {
        try {return PolyUtil.decode(route.getPoints());}
        catch (RuntimeRemoteException e)
        {e.printStackTrace();}
        return null;
    }
}