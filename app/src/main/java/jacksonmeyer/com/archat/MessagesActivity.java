package jacksonmeyer.com.archat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;
import butterknife.ButterKnife;
import jacksonmeyer.com.archat.Models.User;

public class MessagesActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.logoutButton)
    Button mLogoutButton;
    @Bind(R.id.newMessageButton)
    Button mNewMessageButton;
    @Bind(R.id.centerNavigationTitle)
    TextView mCenterNavigationTitle;
    @Bind(R.id.usersRecyclerView)
    RecyclerView mUsersRecyclerView;

    private DatabaseReference mUserReference;
    private FirebaseRecyclerAdapter mUsersFirebaseAdapter;
    private FirebaseAuth mAuth;
//    public ArrayList<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        ButterKnife.bind(this);


        mAuth = FirebaseAuth.getInstance();
        mLogoutButton.setOnClickListener(this);
        mNewMessageButton.setOnClickListener(this);

        mUserReference = FirebaseDatabase.getInstance().getReference("users");
        setUpFirebaseAdapter();
    }

    private void setUpFirebaseAdapter() {
        mUsersFirebaseAdapter = new FirebaseRecyclerAdapter<User, FirebaseUsersViewHolder>
                (User.class, R.layout.recycler_view_item, FirebaseUsersViewHolder.class,
                        mUserReference) {

            @Override
            protected void populateViewHolder(FirebaseUsersViewHolder viewHolder,
                                              User model, int position) {
                viewHolder.bindUser(model);
            }
        };
        mUsersRecyclerView.setHasFixedSize(true);
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
            Intent intent = new Intent(MessagesActivity.this, NewMessageActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUsersFirebaseAdapter.cleanup();
    }
}

