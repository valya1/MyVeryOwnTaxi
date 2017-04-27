package mihail.development.taxi.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.text.DecimalFormat;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import mihail.development.taxi.R;
import mihail.development.taxi.data.OkHttp;
import mihail.development.taxi.data.Tariff;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

public class ConfirmActivity extends Activity{

    private TextView distanceTextView;
    private TextView priceTextView;
    private TextView distanceValueTextView;
    private TextView fromTextView;
    private TextView toTextView;
    private TextView fromValue;
    private TextView toValue;
    private TextView priceValueTextView;
    private CircleImageView driverImage;
    private AppCompatButton btnDecline;
    private AppCompatButton btnConfirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_fragment);


        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        final double distance = Double.parseDouble(preferences.getString("distance", null)) / 1000;
        final String from =" " + preferences.getString("from",null);
        final String to = " " + preferences.getString("to", null);

        fromTextView = (TextView) findViewById(R.id.tvTextFrom);
        toTextView = (TextView) findViewById(R.id.tvTextTo);
        fromValue = (TextView) findViewById(R.id.tvFromValue);
        toValue = (TextView) findViewById(R.id.tvToValue);

        priceTextView = (TextView) findViewById(R.id.tvTextPriceOfTrip);
        distanceTextView = (TextView) findViewById(R.id.tvTextDistance);
        driverImage = (CircleImageView) findViewById(R.id.ivConfirmDriver);
        distanceTextView = (TextView) findViewById(R.id.tvTextDistance);
        priceValueTextView = (TextView) findViewById(R.id.tvPriceValue);
        distanceValueTextView = (TextView) findViewById(R.id.tvDistanceValue);
        btnConfirm = (AppCompatButton) findViewById(R.id.btnConfirm);
        btnDecline = (AppCompatButton) findViewById(R.id.btnDecline);


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfirmActivity.this, WaitingActivity.class));

            }
        });

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Observable.create(new ObservableOnSubscribe<Tariff>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Tariff> observableEmitter) throws Exception {
                observableEmitter.onNext(getTaRiff());
                observableEmitter.onComplete();

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Tariff>() {
                    @Override
                    public void onNext(@NonNull Tariff tariff) {

                        String p = String.valueOf(Math.floor( tariff.getPrice_km() * distance + tariff.getPrice_minute()* distance *6/4 + 40)) + " руб.";
                        priceValueTextView.setText(p);
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        String d = new DecimalFormat("#0.00").format(distance) + " км.";
        distanceValueTextView.setText(d);
        fromValue.setText(from);
        toValue.setText(to);

    }


    private Tariff getTaRiff() {
        HttpUrl url = HttpUrl.parse("http://89.223.29.6:8080/taxi/tariffs").newBuilder()
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        String resp = null;
        try {
            Response response = OkHttp.CLIENT.newCall(request).execute();
            resp = response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(resp).getAsJsonObject().get("response").getAsJsonObject();

        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonObject, Tariff.class);
    }
}
