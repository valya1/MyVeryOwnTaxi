package mihail.development.taxi.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.functions.Function4;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import mihail.development.taxi.R;
import mihail.development.taxi.activities.MapActivity;
import mihail.development.taxi.data.User;
import mihail.development.taxi.presenters.MyCheckLoginPresenter;
import mihail.development.taxi.presenters.MyRegistrationPresenter;
import mihail.development.taxi.presenters.contracts.CheckLoginContract;
import mihail.development.taxi.presenters.contracts.RegistrationContract;


public class RegisterActivity extends AppCompatActivity implements RegistrationContract.View, CheckLoginContract.View {

    private static final int GALLERY_REQUEST = 1;
    final RegistrationContract.RegistrationPresenter registrationPresenter = new MyRegistrationPresenter(this);
    final CheckLoginContract.CheckLoginPresenter checkLoginPresenter = new MyCheckLoginPresenter(this);
    private static final String TAG = "RegisterActivity";

    private AppCompatButton registry;
    private EditText login;
    private EditText password;
    private EditText firstName;
    private EditText lastName;
    private Spinner faculty;
    private String facultyStr;
    private CircleImageView avatar;
   // private ProgressBar progressBar;
    private TextView onLoginCheck;
    private boolean loginCheck = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registry);

        registry = (AppCompatButton) findViewById(R.id.btRegistry);
        login = (EditText) findViewById(R.id.tvLogin);
        password = (EditText) findViewById(R.id.tvPassword);
        firstName = (EditText) findViewById(R.id.tvLFirstName);
        lastName = (EditText) findViewById(R.id.tvLastName);
        faculty = (Spinner) findViewById(R.id.typesSpinner);
        onLoginCheck = (TextView) findViewById(R.id.tvCheckLogin);
        avatar = (CircleImageView) findViewById(R.id.ivUserAvatar);
        //progressBar = (ProgressBar) findViewById(R.id.progressBar);


        final String[] data = {"ВШЭКН", "не ВШЭКН" +
                ""};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        faculty.setAdapter(adapter);
        faculty.setPrompt("Факультет");
        faculty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                facultyStr = data[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        registry.setEnabled(false);

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                startActivityForResult(photoPicker, GALLERY_REQUEST);

            }
        });

                login.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        checkButtonEnabled();
                        checkLoginPresenter.onCheckLogin(login.getText().toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkButtonEnabled();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        password.addTextChangedListener(watcher);
        firstName.addTextChangedListener(watcher);
        lastName.addTextChangedListener(watcher);

        registry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.buildDrawingCache();

                Bitmap bitmap = view.getDrawingCache();
                ByteArrayOutputStream b = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100, b);
                byte[] bytes = b.toByteArray();
                String image = Base64.encodeToString(bytes, Base64.DEFAULT);
//                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RegisterActivity.this);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString("avatar", image).apply();
                User user = new User(firstName.getText().toString(),lastName.getText().toString(),
                        facultyStr, login.getText().toString(),
                        password.getText().toString(), "lol");
                registrationPresenter.onRegistration(user);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap;
        if (resultCode == RESULT_OK)
        {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                avatar.invalidate();
                avatar.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onInAccessibleLogin() {
        onLoginCheck.setText("Логин занят");
        onLoginCheck.setVisibility(View.VISIBLE);
        onLoginCheck.setTextColor(Color.rgb(244,66,66));
        Log.i(TAG, "Not Available login");
        loginCheck = false;
        checkButtonEnabled();
    }

    @Override
    public void onAccessibleLogin() {
        onLoginCheck.setTextColor(Color.rgb(8,173,22));
        onLoginCheck.setText("Логин свободен");
        onLoginCheck.setVisibility(View.VISIBLE);
        Log.i(TAG, "Available login");
        loginCheck = true;
        checkButtonEnabled();
    }

    @Override
    public void onSuccessRegistration() {

        startActivity(new Intent(this, MainActivity.class));
    }

    public void checkButtonEnabled() {
        Observable.combineLatest(Observable.just(loginCheck), Observable.just(firstName.getText().toString()),
                Observable.just(lastName.getText().toString()), Observable.just(password.getText().toString()),
                new Function4<Boolean, String, String, String, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull Boolean aBoolean, @NonNull String s, @NonNull String s2, @NonNull String s3) throws Exception {
                        return aBoolean && !s.trim().equals("") && !s2.trim().equals("") && !s3.trim().equals("");
                    }
                }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(new DisposableObserver<Boolean>() {
            @Override
            public void onNext(@NonNull Boolean aBoolean) {
                registry.setEnabled(aBoolean);
            }

            @Override
            public void onError(@NonNull Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
