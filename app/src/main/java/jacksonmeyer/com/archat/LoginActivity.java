package jacksonmeyer.com.archat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;
import butterknife.ButterKnife;
import jacksonmeyer.com.archat.Models.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.nameTextField) EditText mNameTextField;
    @Bind(R.id.emailTextField) EditText mEmailTextField;
    @Bind(R.id.passwordTextField) EditText mPasswordTextField;
    @Bind(R.id.registerUserButton) Button mRegisterUserButton;
    @Bind(R.id.loginButton) Button mLoginButton;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mLoginButton.setOnClickListener(this);
        mRegisterUserButton.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in, if they are go to messages screen, if not show login page
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            handleAutoLogin();
        } else {
            //stay on loginScreen
        }
    }

    @Override
    public void onClick(View view) {
        final String name = mNameTextField.getText().toString();
        final String email = mEmailTextField.getText().toString();
        final String password = mPasswordTextField.getText().toString();

        if (view == mRegisterUserButton) {
            if (mNameTextField.length() <= 6 || mEmailTextField.length() <= 6 || mPasswordTextField.length() <= 6 ) {
                Toast.makeText(LoginActivity.this, "make sure you enter a correctly formatted name, email, password",
                        Toast.LENGTH_SHORT).show();
            } else {

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    String uid = user.getUid();
                                    createUser(uid, email, name);
                                    handleAutoLogin();
                                } else {
                                    Log.d("main", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
        } else if (view == mLoginButton) {
            if (mNameTextField.length() <= 6 || mEmailTextField.length() <= 6 || mPasswordTextField.length() <= 6) {
                Toast.makeText(LoginActivity.this, "make sure you enter a correctly formatted name, email, password",
                        Toast.LENGTH_SHORT).show();
            } else {
                Log.d("Main Activity", "login");
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    handleAutoLogin();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("Main", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
    }
    private void createUser(String uid, String email, String name) {
        Log.d("createUser: ", "1");
        User user = new User(email, name);
        Log.d("createUser: ", "2");
        mDatabase.child("users").child(uid).setValue(user);
        Log.d("createUser: ", "3");
    }

    private void handleAutoLogin() {
        Intent intent = new Intent(this, MessagesActivity.class);
        startActivity(intent);
    }
}
