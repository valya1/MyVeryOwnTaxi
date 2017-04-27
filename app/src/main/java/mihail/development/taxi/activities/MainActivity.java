package mihail.development.taxi.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import mihail.development.taxi.R;
import mihail.development.taxi.data.User;
import mihail.development.taxi.presenters.MyLoginPresenter;
import mihail.development.taxi.presenters.contracts.LoginContract;

import static mihail.development.taxi.R.id.btLogin;


public class MainActivity extends AppCompatActivity implements LoginContract.View {

    static final String TAG = "Main_Activity";
    private LoginContract.LoginPresenter loginPresenter = new MyLoginPresenter(this);
    private ProgressDialog progressDialog;
    private TextView startRegistration;
    private Button loginBtn;
    private EditText loginText;
    private EditText passwordText;
    private TextView errotMessage;
    public static final String APP_PREFERENCES = "mysettings";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(MainActivity.this);
        startRegistration = (TextView) findViewById(R.id.text2);
        loginBtn = (Button) findViewById(R.id.btLogin);
        loginText = (EditText) findViewById(R.id.tvLogin);
        passwordText = (EditText) findViewById(R.id.tvPassword);
        errotMessage = (TextView) findViewById(R.id.tvErrorMessage);

              View.OnClickListener click = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.text2:
                        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                        break;
                    case btLogin:
                        String login = loginText.getText().toString();
                        String password = passwordText.getText().toString();
                        loginPresenter.onLogin(login, password);
                        break;
                }
            }
        };
        startRegistration.setOnClickListener(click);
        loginBtn.setOnClickListener(click);
    }

    @Override
    public void toMapActivity(User user) {
        Log.i(TAG, "SUcceeess");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prfEdit = preferences.edit();
        prfEdit
                .putString("user_login", user.getLogin())
                .putString("avatar", user.getPhoto())
                .apply();
        startActivity(new Intent(this, MapActivity.class));

    }

    @Override
    public void showErrorMessage() {
        errotMessage.setText("Неправильный логин или пароль");
    }
}
