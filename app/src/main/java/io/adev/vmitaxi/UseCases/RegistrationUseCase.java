package io.adev.vmitaxi.UseCases;

import android.location.Criteria;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.adev.vmitaxi.UseCase;
import io.adev.vmitaxi.data.OkHttp;
import io.adev.vmitaxi.data.User;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class RegistrationUseCase extends UseCase<Boolean, RegistrationUseCase.Criteria> {


    public RegistrationUseCase() {
        super(Schedulers.io());
    }

    public static class Criteria
    {
        public Criteria(User user) {
            this.user = user;
        }
        public final User user;

   }

    protected Observable<Boolean> createObservable(final Criteria criteria)
    {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                AddUser(criteria);
                e.onNext(true);
                e.onComplete();
            }
        });
    }

    private String AddUser(Criteria criteria) {
        FormBody body = new FormBody.Builder()
                .add("f_name", criteria.user.getF_name())
                .add("l_name", criteria.user.getL_name())
                .add("faculty", criteria.user.getFaculty())
                .add("login", criteria.user.getLogin())
                .add("password", criteria.user.getPassword())
                .add("photo", criteria.user.getPhoto())
                .build();
        Request request = new Request.Builder()
                .url("http://89.223.29.6:8080/taxi/users/register")
                .post(body)
                .build();
        String response = null;
        try
        {
            response = OkHttp.CLIENT.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;

    }



}
