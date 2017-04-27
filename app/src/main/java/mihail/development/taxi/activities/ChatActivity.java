package mihail.development.taxi.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.xml.validation.SchemaFactoryLoader;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import mihail.development.taxi.R;
import mihail.development.taxi.UseCases.RegistrationUseCase;
import mihail.development.taxi.chatrecycler.ChatAdapter;
import mihail.development.taxi.data.ChatItem;
import mihail.development.taxi.data.Driver;
import mihail.development.taxi.data.OkHttp;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mihail on 23.04.2017.
 */

public class ChatActivity extends AppCompatActivity{

    private static final String TAG = "Chat";
    private AppCompatButton sendMessageButton;
    private TextView message;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_view);

      sendMessageButton = (AppCompatButton)findViewById(R.id.btnSendMessage);
      message = (TextView) findViewById(R.id.tvTypeMessage);


        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.chatRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final ChatAdapter chatAdapter = new ChatAdapter();
        recyclerView.setAdapter(chatAdapter);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        final String login_driver = getIntent().getExtras().getString("driver_login");
        final String login_user = sharedPreferences.getString("user_login",null);

        final DisposableObserver<ArrayList<ChatItem>> observer = new DisposableObserver<ArrayList<ChatItem>>() {
            @Override
            public void onNext(@NonNull ArrayList<ChatItem> chatItems) {
                chatAdapter.update(chatItems);
            }

            @Override
            public void onError(@NonNull Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        };

//        createObservable(login_user,login_driver).repeat().subscribe(observer);

        createObservable(login_user,login_driver).repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(@NonNull Observable<Object> objectObservable) throws Exception {
                return  objectObservable.flatMap(new Function<Object, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(@NonNull Object o) throws Exception {
                        return Observable.timer(3,TimeUnit.SECONDS);
                    }
                });
            }
        }).subscribe(observer);



//        createObservable(login_user,login_driver).subscribe(observer);


        sendMessageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Observable.create(new ObservableOnSubscribe<ChatItem>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<ChatItem> observableEmitter) throws Exception {
                        observableEmitter.onNext(postMessage(message.getText().toString(), login_user, login_driver));
                        observableEmitter.onComplete();
                    }
                })      .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableObserver<ChatItem>() {
                    @Override
                    public void onNext(@NonNull ChatItem item) {
//                        createObservable(login_user,login_driver).subscribe(observer);
                        chatAdapter.update(item);
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
                        message.setText("");
            }
        });
    }


    private Observable<ArrayList<ChatItem>> createObservable(final String login_user, final String login_driver)
    {
        return Observable.create(new ObservableOnSubscribe<ArrayList<ChatItem>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<ArrayList<ChatItem>> observableEmitter) throws Exception {
                observableEmitter.onNext(getDialog(login_user,login_driver));
                observableEmitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    private ChatItem postMessage(String message, String login_user, String login_driver)
    {
        FormBody body = new FormBody.Builder()
                .add("login_user", login_user)
                .add("login_driver", login_driver)
                .add("message", message)
                .add("from_driver", "false")
                .build();
        Request request = new Request.Builder()
                .url("http://89.223.29.6:8080/taxi/chats/post")
                .post(body)
                .build();
        String response = null;
        try
        {
            response = OkHttp.CLIENT.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonParser parser = new JsonParser();
        JsonObject o = parser.parse(response).getAsJsonObject();
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(o.get("response"),ChatItem.class);
    }


    private ArrayList<ChatItem> getDialog(String login_user, String login_driver) {
        ArrayList<ChatItem> chatItems = new ArrayList<>();

        HttpUrl url = HttpUrl.parse("http://89.223.29.6:8080/taxi/chats").newBuilder()
                .addEncodedQueryParameter("login_user",login_user)
                .addEncodedQueryParameter("login_driver", login_driver)
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
                chatItems.add(gson.fromJson(arr.get(i), ChatItem.class));
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return chatItems;
    }
}
