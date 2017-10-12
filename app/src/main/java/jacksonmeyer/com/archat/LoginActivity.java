package jacksonmeyer.com.archat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import jacksonmeyer.com.archat.Models.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.nameTextField) EditText mNameTextField;
    @Bind(R.id.emailTextField) EditText mEmailTextField;
    @Bind(R.id.passwordTextField) EditText mPasswordTextField;
    @Bind(R.id.registerUserButton) Button mRegisterUserButton;
    @Bind(R.id.loginButton) Button mLoginButton;
    @Bind(R.id.profileImageView) ImageView mProfileImageView;

    public static final int PICK_IMAGE = 1;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseStorage mStorage;

    String name = "";
    String email = "";
    String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_actvity);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance();

        mLoginButton.setOnClickListener(this);
        mRegisterUserButton.setOnClickListener(this);
        mProfileImageView.setOnClickListener(this);
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

    //.child(profileImages)

    @Override
    public void onClick(View view) {



        //handling of the
        if (view == mRegisterUserButton) {
            if (mNameTextField.length() <= 6 || mEmailTextField.length() <= 6 || mPasswordTextField.length() <= 6 ) {
                Toast.makeText(LoginActivity.this, "make sure you enter a correctly formatted name, email, password",
                        Toast.LENGTH_SHORT).show();
            } else {
                name = mNameTextField.getText().toString();
                email = mEmailTextField.getText().toString();
                password = mPasswordTextField.getText().toString();

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    createDatabaseImageRef();
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
        } else if (view == mProfileImageView) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        }
    }

    private void createDatabaseImageRef() {
        Log.d("Login", "1");
        mProfileImageView.setDrawingCacheEnabled(true);
        mProfileImageView.buildDrawingCache();
        Bitmap bitmap = mProfileImageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        Log.d("Login", "2");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        String ImageName = UUID.randomUUID().toString();
        Log.d("Login Image Name", ImageName);
        StorageReference profileImageRef = storage.getReference().child("profileImages").child(ImageName + ".png");
        Log.d("Login", "3");
        UploadTask uploadTask = profileImageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d("Login", "unsuccessful image upload");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Log.d("Login", "4");
                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                //saving user into the database with a reference to the storage instance
                saveUser(downloadUrl);
            }
        });



    }

    private void saveUser(Uri downloadUrl) {
        Log.d("Login", "5");
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        Log.d("Login", "6");
        createUser(uid, name, email, downloadUrl);
        handleAutoLogin();
        Log.d("Login", "11");
    }

    private void createUser(String uid, String name, String email, Uri profileImageUrl) {
        Log.d("Login", "7");
        //save user to datbase with image url instance
        User user = new User(name, email, profileImageUrl);
        mDatabase.child("users").child(uid).setValue(user);
        Log.d("Login", "8");
    }

    private void handleAutoLogin() {
        Log.d("Login", "9");
        Intent intent = new Intent(this, MessagesActivity.class);
        startActivity(intent);
        Log.d("Login", "10");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE) {
           try  {
               Uri uri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                mProfileImageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


