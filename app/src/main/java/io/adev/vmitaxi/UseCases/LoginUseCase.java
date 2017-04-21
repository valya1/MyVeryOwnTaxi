package io.adev.vmitaxi.UseCases;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import io.adev.vmitaxi.UseCase;
import io.adev.vmitaxi.data.OkHttp;
import io.adev.vmitaxi.data.User;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class LoginUseCase extends UseCase<Boolean,LoginUseCase.Criteria> {

    public LoginUseCase() {
        super(Schedulers.io());
    }

    public static class Criteria {
        public final String login;
        public final String password;

        public Criteria(String login, String password) {
            this.login = login;
            this.password = password;
        }
    }

    @Override
    protected Observable<Boolean> createObservable(final Criteria criteria) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                JsonObject JSonUser = getUser(criteria.login, criteria.password);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                if(JSonUser.get("code").toString().equals("0")) {
                    User user = gson.fromJson(JSonUser.get("response"), User.class);
                    e.onNext(true);
                    e.onComplete();
                }
                else
                    e.onError((IOException) e);

            }
        });
    }

    private JsonObject getUser(String login, String password) {
        HttpUrl url = HttpUrl.parse("http://89.223.29.6:8080/taxi/users/login").newBuilder()
                .addEncodedQueryParameter("login",login)
                .addEncodedQueryParameter("password", password)
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
        return parser.parse(resp).getAsJsonObject();
    }
}



