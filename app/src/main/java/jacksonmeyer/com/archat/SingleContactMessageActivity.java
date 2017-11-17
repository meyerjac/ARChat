package jacksonmeyer.com.archat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import jacksonmeyer.com.archat.Models.chatMessage;
import jacksonmeyer.com.archat.ViewHolders.messagesViewHolder;

public class SingleContactMessageActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.contactNameTextView) TextView mContactNameTextView;
    @Bind(R.id.backButton) TextView mBackButton;
    @Bind(R.id.contactProfileImageView) ImageView mContactProfileImageView;
    @Bind(R.id.sendButton) Button mSendButton;
    @Bind(R.id.messagesRecyclerView) RecyclerView mMessagesRecyclerView;
    @Bind(R.id.messageEditText) EditText mMessageEditText;


    String ContactUid;
    private String TAG = "main";
    private FirebaseAuth mAuth;
    private FirebaseRecyclerAdapter mMessagesFirebaseAdapter;
    private DatabaseReference mUserMessagesReference;
    private DatabaseReference specificUserMessageReference;
    private String loggedInUserUid;
    private ArrayList<chatMessage> imessages = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_contact_message);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        ContactUid = intent.getStringExtra("uid");

        mAuth = FirebaseAuth.getInstance();
        loggedInUserUid = mAuth.getCurrentUser().getUid();
        mUserMessagesReference = FirebaseDatabase.getInstance().getReference("users").child(loggedInUserUid).child("messages");
        specificUserMessageReference = FirebaseDatabase.getInstance().getReference("users").child(loggedInUserUid)
                .child("messages").child(ContactUid);


        loadProfilePicAndData();
        setUpMessagesAdapter();


        mBackButton.setOnClickListener(this);
        mSendButton.setOnClickListener(this);
    }

    private void setUpMessagesAdapter() {
        mMessagesFirebaseAdapter = new FirebaseRecyclerAdapter<chatMessage, messagesViewHolder>
                (chatMessage.class, R.layout.recycler_view_imessage_item, messagesViewHolder.class,
                        specificUserMessageReference) {
            @Override
            protected void populateViewHolder(messagesViewHolder viewHolder, chatMessage model, int position) {
                viewHolder.bindMessages(model);
            }
        };
        mMessagesRecyclerView.setHasFixedSize(false);
        mMessagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMessagesRecyclerView.setAdapter(mMessagesFirebaseAdapter);
    }

    private void loadProfilePicAndData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users").child(ContactUid);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mContactNameTextView.setText(dataSnapshot.child("name").getValue().toString());
                String profileImageName = dataSnapshot.child("imageName").getValue().toString();
                loadImage(profileImageName);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: ");
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

    private void loadImage(String profileImageName) {
        FirebaseStorage mStorage = FirebaseStorage.getInstance();
        final StorageReference storageRef = mStorage.getReference();
        StorageReference pathName = storageRef.child("profileImages/" + profileImageName + ".png");



        final long ONE_MEGABYTE = 1024 * 1024;
        pathName.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                mContactProfileImageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, mContactProfileImageView.getWidth(),
                        mContactProfileImageView.getHeight(), false));
                Log.d("main", "onSuccess: ");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.d("fail", "onSuccess: ");
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == mSendButton) {
            String imessage = mMessageEditText.getText().toString();
            //static date, to just get logic down
            String date = "Thursday 1:39pm";
            String messageOwnerUid = loggedInUserUid;
            final chatMessage message= new chatMessage(imessage, date, messageOwnerUid);

            specificUserMessageReference.push().setValue(message);
            mMessageEditText.setText("");
        } else if
                (view == mBackButton) {
            Intent intent = new Intent(SingleContactMessageActivity.this, MessagesActivity.class);
            startActivity(intent);
        }
    }
}
