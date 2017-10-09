package jacksonmeyer.com.archat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.nameTextField)
    EditText mNameTextField;
    @Bind(R.id.emailTextField)
    EditText mEmailTextField;
    @Bind(R.id.passwordTextField)
    EditText mPasswordTextField;
    @Bind(R.id.registerUserButton)
    Button mRegisterUserButton;
    @Bind(R.id.loginButton)
    Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mLoginButton.setOnClickListener(this);
        mRegisterUserButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mRegisterUserButton) {
            Log.d("Main Activity", "register");
        } else if (view == mLoginButton){
            Log.d("Main Activity", "login");
        }
    }
}
