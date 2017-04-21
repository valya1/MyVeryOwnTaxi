package mihail.development.taxi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;

import mihail.development.taxi.data.Driver;
import mihail.development.taxi.data.OkHttp;
import mihail.development.taxi.recyclerview.DriversAdapter;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mihail on 18.04.2017.
 */

public class DriversListActivity extends AppCompatActivity {

    private static final String TAG = "DriverListActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final DriversAdapter driversAdapter = new DriversAdapter();
        recyclerView.setAdapter(driversAdapter);

        final Observable<ArrayList<Driver>> driverObservable = Observable.create(new ObservableOnSubscribe<ArrayList<Driver>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<ArrayList<Driver>> e) throws Exception {
                e.onNext(getDrivers(5,0));
                e.onComplete();
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());

        driverObservable.subscribe(new DisposableObserver<ArrayList<Driver>>() {
            @Override
            public void onNext(ArrayList<Driver> drivers) {
                driversAdapter.addData(drivers);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });

    }

    private ArrayList<Driver> getDrivers(int count, int offset) {
        ArrayList<Driver> drivers = new ArrayList<>();

        HttpUrl url = HttpUrl.parse("http://89.223.29.6:8080/taxi/drivers").newBuilder()
                .addEncodedQueryParameter("count", String.valueOf(count))
                .addEncodedQueryParameter("offset", String.valueOf(offset))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try{
            Response response = OkHttp.CLIENT.newCall(request).execute();
            String resp = response.body().string();
            JsonParser parser = new JsonParser();
            JsonObject o = parser.parse(resp).getAsJsonObject();
            JsonArray arr = o.get("response").getAsJsonArray();
            Gson gson = new GsonBuilder().create();
            for(int i = 0; i< arr.size(); i++)
            {
                drivers.add(gson.fromJson(arr.get(i), Driver.class));
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return drivers;
    }
}
