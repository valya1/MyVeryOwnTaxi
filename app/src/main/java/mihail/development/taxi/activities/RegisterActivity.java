package mihail.development.taxi.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import mihail.development.taxi.R;
import mihail.development.taxi.activities.MapActivity;
import mihail.development.taxi.data.User;
import mihail.development.taxi.presenters.MyCheckLoginPresenter;
import mihail.development.taxi.presenters.MyRegistrationPresenter;
import mihail.development.taxi.presenters.contracts.CheckLoginContract;
import mihail.development.taxi.presenters.contracts.RegistrationContract;


public class RegisterActivity extends AppCompatActivity implements RegistrationContract.View, CheckLoginContract.View {

    final RegistrationContract.RegistrationPresenter registrationPresenter = new MyRegistrationPresenter(this);
    final CheckLoginContract.CheckLoginPresenter checkLoginPresenter = new MyCheckLoginPresenter(this);
    private static final String TAG = "RegisterActivity";

    private Button registry;
    private EditText login;
    private EditText password;
    private EditText firstName;
    private EditText lastName;
    private Spinner faculty;
    private String facultyStr;
    private EditText avatar;
    private ProgressBar progressBar;
    private TextView onLoginCheck;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registry);

        registry = (Button) findViewById(R.id.btRegistry);
        login = (EditText) findViewById(R.id.tvLogin);
        password = (EditText) findViewById(R.id.tvPassword);
        firstName = (EditText) findViewById(R.id.tvLFirstName);
        lastName = (EditText) findViewById(R.id.tvLastName);
        faculty = (Spinner) findViewById(R.id.typesSpinner);
        onLoginCheck = (TextView) findViewById(R.id.tvCheckLogin);
        avatar = (EditText) findViewById(R.id.tvAvatar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        final String[] data = {"ВШЭКН", "Не ВШЭКН"};
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

//        final Observable<CharSequence> loginObs = RxTextView.textChanges(login);

//        final Observable<Boolean> usedLogin = loginObs.map(new Function<CharSequence, Boolean>() {
//            @Override
//            public Boolean apply(@NonNull CharSequence charSequence) throws Exception {
//                return isExistingLogin(charSequence.toString());
//            }
//        }).debounce(500, TimeUnit.MILLISECONDS)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread());
//
//
//        login.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                usedLogin.subscribeWith(new DisposableObserver<Boolean>() {
//                    @Override
//                    public void onNext(Boolean aBoolean) {
//                        if (aBoolean)
//                            Log.i(TAG, "TRUE");
//                        else
//                            Log.i(TAG,"FALSE");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//                return false;
//            }
//        });

                login.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        checkLoginPresenter.onCheckLogin(login.getText().toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

        registry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //    http://89.223.29.6:8080/taxi/users/register
//                final Runnable runnable = new Runnable() {
//                    @Override
//                        public void run() {
//                        handler.sendEmptyMessage(STATUS_CONNECTION);
//                        FormBody body = new FormBody.Builder()
//                                .add("f_name", firstName.getText().toString())
//                                .add("l_name", lastName.getText().toString())
//                                .add("faculty", faculty.getText().toString())
//                                .add("login", login.getText().toString())
//                                .add("password", lastName.getText().toString())
//                                .add("photo", avatar.getText().toString())
//                                .build();
//                        AddUser("http://89.223.29.6:8080/taxi/users/register", body);
//                        handler.sendEmptyMessage(STATUS_NONE);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(RegisterActivity.this, "Loading...", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                };
//                Thread thread = new Thread(runnable);
//                thread.start();

                User user = new User(firstName.getText().toString(),lastName.getText().toString(),
                        facultyStr, login.getText().toString(),
                        password.getText().toString(), avatar.getText().toString());
                registrationPresenter.onRegistration(user);
            }
        });
    }

    @Override
    public void onInAccessibleLogin() {
        onLoginCheck.setText("Логин занят");
        onLoginCheck.setVisibility(View.VISIBLE);
        onLoginCheck.setTextColor(Color.rgb(244,66,66));
        Log.i(TAG, "Not Available login");
    }

    @Override
    public void onAccessibleLogin() {
        onLoginCheck.setTextColor(Color.rgb(8,173,22));
        onLoginCheck.setText("Логин свободен");
        onLoginCheck.setVisibility(View.VISIBLE);
        Log.i(TAG, "Available login");
    }

    @Override
    public void onSuccessRegistration() {

        startActivity(new Intent(this, MapActivity.class));
    }
}
