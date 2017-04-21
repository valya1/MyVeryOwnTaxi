package mihail.development.taxi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import mihail.development.taxi.presenters.MyLoginPresenter;
import mihail.development.taxi.presenters.contracts.LoginContract;

import static mihail.development.taxi.R.id.btLogin;


public class MainActivity extends AppCompatActivity implements LoginContract.View {

    static final String TAG = "Main_Activity";
    private LoginContract.LoginPresenter loginPresenter = new MyLoginPresenter(this);
    private ProgressDialog progressDialog;
    private Button startRegistration;
    private Button loginBtn;
    private EditText loginText;
    private EditText passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(MainActivity.this);
        startRegistration = (Button) findViewById(R.id.btRegister);
        loginBtn = (Button) findViewById(R.id.btLogin);
        loginText = (EditText) findViewById(R.id.tvLogin);
        passwordText = (EditText) findViewById(R.id.tvPassword);

              View.OnClickListener clickBtn = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btRegister:
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
        startRegistration.setOnClickListener(clickBtn);
        loginBtn.setOnClickListener(clickBtn);
    }

    @Override
    public void toMapActivity() {
        Log.i(TAG, "SUcceeess");
        startActivity(new Intent(this, MapActivity.class));
    }
}
