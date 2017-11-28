package jacksonmeyer.com.archat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import jacksonmeyer.com.archat.Models.User;
import jacksonmeyer.com.archat.ViewHolders.FirebaseUsersViewHolder;

public class MessagesActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.logoutButton)
    TextView mLogoutButton;
    @Bind(R.id.currentUser)
    TextView mCurrentUser;
    @Bind(R.id.usersRecyclerView)
    RecyclerView mUsersRecyclerView;
    @Bind(R.id.newMessageButton)
    ImageView mNewMessageButton;


    private DatabaseReference mUserReference;
    private FirebaseRecyclerAdapter mUsersFirebaseAdapter;
    private FirebaseAuth mAuth;
    private String currentUserUid;
    private String TAG = "main";

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        ButterKnife.bind(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();

        mLogoutButton.setOnClickListener(this);
        mNewMessageButton.setOnClickListener(this);
        mUserReference = FirebaseDatabase.getInstance().getReference("users");

        getCurrentUserUid();
        setUpFirebaseAdapter();
        setUpCurrentDisplayName();
    }

    public void getCurrentUserUid() {
        mAuth = FirebaseAuth.getInstance();
        currentUserUid = mAuth.getCurrentUser().getUid();

        addToSharedPreferences(currentUserUid);
    }

    private void addToSharedPreferences(String uid) {
        mEditor.putString(Constants.SHARED_PREFERENCES_KEY_LOGGED_IN_UID, uid).apply();
    }

    private void setUpCurrentDisplayName() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users").child(currentUserUid);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCurrentUser.setText(dataSnapshot.child("name").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void setUpFirebaseAdapter() {

        mUsersFirebaseAdapter = new FirebaseRecyclerAdapter<User, FirebaseUsersViewHolder>
                (User.class, R.layout.recycler_view_user_item, FirebaseUsersViewHolder.class,
                        mUserReference) {

            @Override
            protected void populateViewHolder(FirebaseUsersViewHolder viewHolder,
                                              User model, int position) {
                viewHolder.bindUser(model);
            }
        };
        mUsersRecyclerView.setHasFixedSize(false);
        mUsersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUsersRecyclerView.setAdapter(mUsersFirebaseAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view == mLogoutButton) {
            mAuth.signOut();
            Intent intent = new Intent(MessagesActivity.this, LoginActivity.class);
            startActivity(intent);
        } else if (view == mNewMessageButton) {
//            Intent intent = new Intent(MessagesActivity.this, newMessageActivity.class);
//            startActivity(intent);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUsersFirebaseAdapter.cleanup();
    }

    @Override
    public void onBackPressed(){
        Log.d("main", "onBackPressed: ");
    }
}

