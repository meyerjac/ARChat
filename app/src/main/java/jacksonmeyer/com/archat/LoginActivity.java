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
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import jacksonmeyer.com.archat.Models.ChatMessage;
import jacksonmeyer.com.archat.Models.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.registerNameTextField) EditText mNameTextField;
    @Bind(R.id.registerEmailTextField) EditText mEmailTextField;
    @Bind(R.id.registerPasswordTextField) EditText mPasswordTextField;
    @Bind(R.id.registerLoginUserButton) Button mRegisterLoginUserButton;
    @Bind(R.id.loginEmailTextField) EditText mLoginEmailTextField;
    @Bind(R.id.loginPasswordTextField) EditText mLoginPasswordTextField;
    @Bind(R.id.contactProfileImageView) ImageView mProfileImageView;
    @Bind(R.id.clickHereText) TextView mClickHereText;
    @Bind(R.id.alreadyHaveAccountText) TextView mAlreadyHaveAccountText;
    @Bind(R.id.loginRelativeTextFieldlayout) RelativeLayout mLoginRelativeTextFieldlayout;
    @Bind(R.id.registerTextFieldlayout) RelativeLayout mRegisterTextFieldlayout;

    public static final int PICK_IMAGE = 1;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseStorage mStorage;
    private Map<String, ChatMessage> initialMessages = new HashMap<>();

    String name = "";
    String email = "";
    String password = "";
    String imageName = "";
    String learningLanguageISO = "en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance();

        mClickHereText.setOnClickListener(this);
        mRegisterLoginUserButton.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        //handling of the
        if (view == mRegisterLoginUserButton) {
            if (mRegisterLoginUserButton.getText().toString().equals("Register")) {
                if (mNameTextField.length() <= 6 || mEmailTextField.length() <= 6 || mPasswordTextField.length() <= 6 ) {
                    Toast.makeText(LoginActivity.this, R.string.toast_register_failed_fields_message,
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
                                        Toast.makeText(LoginActivity.this,task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            } else {
                if (mLoginEmailTextField.length() <= 6 || mLoginPasswordTextField.length() <= 6) {
                    Toast.makeText(LoginActivity.this, R.string.toast_login_failed_fields_message,
                            Toast.LENGTH_SHORT).show();
                } else {
                    email = mLoginEmailTextField.getText().toString();
                    password = mLoginPasswordTextField.getText().toString();
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
                                        Toast.makeText(LoginActivity.this,task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        } else if (view == mProfileImageView) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        } else if (view == mClickHereText) {
            Log.d("main", mRegisterLoginUserButton.getText().toString());

            if (mRegisterLoginUserButton.getText().toString().equals("Register")) {
                Log.d("main", "onClick: 2");
                mLoginRelativeTextFieldlayout.setVisibility(View.VISIBLE);
                mRegisterTextFieldlayout.setVisibility(View.INVISIBLE);
                mRegisterLoginUserButton.setText(R.string.login);
                mAlreadyHaveAccountText.setText(R.string.need_to_create_account);
                Log.d("main", "onClick: 3");
            } else {
                Log.d("main", "onClick: 4");
                mLoginRelativeTextFieldlayout.setVisibility(View.INVISIBLE);
                mRegisterTextFieldlayout.setVisibility(View.VISIBLE);
                mRegisterLoginUserButton.setText(R.string.register);
                mAlreadyHaveAccountText.setText(R.string.already_have_account);
            }
        }
    }

    //this code black is just for the profile imageView
    private void createDatabaseImageRef() {
        mProfileImageView.setDrawingCacheEnabled(true);
        mProfileImageView.buildDrawingCache();
        Bitmap bitmap = mProfileImageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        imageName = UUID.randomUUID().toString();
        StorageReference profileImageRef = mStorage.getReference().child("profileImages").child(imageName + ".png");
        UploadTask uploadTask = profileImageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                String downloadUrl = taskSnapshot.getDownloadUrl().toString();

                //saving user into the database with a reference to the storage instance
                saveUser();
            }
        });



    }

    private void saveUser() {
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        createUser(name, email, imageName, uid, initialMessages);
    }

    private void createUser(String name, String email, String imageName, String uid, Map<String, ChatMessage> messages) {
        //save user to datbase with image name
        User user = new User(name, email, imageName, uid, learningLanguageISO, messages);
        //something bugs up when I try and set the users in firebase
        mDatabase.child("users").child(uid).setValue(user);
        handleAutoLogin();
    }

    private void handleAutoLogin() {
        Intent intent = new Intent(this, ChooseLanguageActivity.class);
//        intent.putExtra(uid, "currentUserUID");
        startActivity(intent);
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


