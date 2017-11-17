package jacksonmeyer.com.archat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    Button mLogoutButton;
    @Bind(R.id.currentUser)
    TextView mCurrentUser;
    @Bind(R.id.usersRecyclerView)
    RecyclerView mUsersRecyclerView;

    private DatabaseReference mUserReference;
    private FirebaseRecyclerAdapter mUsersFirebaseAdapter;
    private FirebaseAuth mAuth;
    private String currentUserUid;
    private String TAG = "main";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        currentUserUid = mAuth.getCurrentUser().getUid();
        mLogoutButton.setOnClickListener(this);
        mUserReference = FirebaseDatabase.getInstance().getReference("users");

        setUpFirebaseAdapter();
        setUpCurrentDisplayName();
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

