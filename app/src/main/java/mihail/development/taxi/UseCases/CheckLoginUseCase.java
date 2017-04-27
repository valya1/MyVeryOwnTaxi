package mihail.development.taxi.UseCases;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import mihail.development.taxi.UseCase;
import mihail.development.taxi.data.OkHttp;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.HttpUrl;
import okhttp3.Request;


public class CheckLoginUseCase extends UseCase<Boolean, String> {

    public String login;

    public CheckLoginUseCase(String s) {
        super(Schedulers.io());
        this.login = s;
    }


    protected Observable<Boolean> createObservable(final String criteria) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                if(!(isExistingLogin(criteria)))
                {
                    e.onNext(true);
                }
                else
                {
                    e.onNext(false);
                }
            }
        }).debounce(1, TimeUnit.SECONDS);
    }


    private boolean isExistingLogin(String login) {
        HttpUrl url = HttpUrl.parse("http://89.223.29.6:8080/taxi/users/by_login").newBuilder()
                .addEncodedQueryParameter("login",login)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        String response = null;
        try {
            response = OkHttp.CLIENT.newCall(request).execute().body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonParser parser = new JsonParser();
        JsonObject resp = parser.parse(response).getAsJsonObject();

       return resp.get("code").toString().equals("0");

    }
}
