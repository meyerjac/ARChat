package jacksonmeyer.com.archat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MessagesActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.logoutButton)
    Button mLogoutButton;
    @Bind(R.id.newMessageButton)
    Button mNewMessageButton;
    @Bind(R.id.centerNavigationTitle)
    TextView mCenterNavigationTitle;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mLogoutButton.setOnClickListener(this);
        mNewMessageButton.setOnClickListener(this);
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
}
