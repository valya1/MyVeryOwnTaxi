package mihail.development.taxi.drawer;

import android.content.Context;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import mihail.development.taxi.R;

/**
 * Created by mihail on 23.04.2017.
 */

public class RouteDrawer {

    PolylineOptions line;
    LatLngBounds.Builder builder;


    public RouteDrawer() {
        this.line = new PolylineOptions();
        line.width(4f).color(R.color.colorPrimary);
        this.builder = new LatLngBounds.Builder();
    }



    public void draw(List<LatLng> points, GoogleMap map, Context context)
    {
        for(int i = 0; i< points.size();i++)
        {
            line.add(points.get(i));
            builder.include(points.get(i));
        }
        map.addPolyline(line);
        int size = context.getResources().getDisplayMetrics().widthPixels;
        LatLngBounds latLngBounds = builder.build();
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,size,size,25));
    }

}
