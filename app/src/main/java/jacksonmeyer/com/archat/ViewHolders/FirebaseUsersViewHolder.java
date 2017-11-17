package jacksonmeyer.com.archat.ViewHolders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import jacksonmeyer.com.archat.Models.User;
import jacksonmeyer.com.archat.R;
import jacksonmeyer.com.archat.SingleContactMessageActivity;

public class FirebaseUsersViewHolder extends RecyclerView.ViewHolder {
    private static final int MAX_WIDTH = 200;
    private static final int MAX_HEIGHT = 200;
    String TAG = "here";

    View mView;
    Context mContext;
    private FirebaseStorage mStorage;
    public FirebaseUsersViewHolder(View itemView) {

        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        itemView.setOnClickListener((View.OnClickListener) mContext);
    }

    public void bindUser(final User user) {
        TextView nameTextView = (TextView) mView.findViewById(R.id.nameTextView);
        nameTextView.setText(user.getName());
        FirebaseStorage mStorage = FirebaseStorage.getInstance();
        final StorageReference storageRef = mStorage.getReference();
        StorageReference pathName = storageRef.child("profileImages/" + user.getImageName() + ".png");



        final long ONE_MEGABYTE = 1024 * 1024;
        pathName.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ImageView image = (ImageView) mView.findViewById(R.id.contactProfileImageView);

                image.setImageBitmap(Bitmap.createScaledBitmap(bmp, image.getWidth(),
                        image.getHeight(), false));
                Log.d("main", "onSuccess: ");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.d("fail", "onSuccess: ");
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getAdapterPosition();
                Log.d("main", user.getEmail());
                Log.d("main", user.getUserUid());
                Intent intent = new Intent(mContext, SingleContactMessageActivity.class);
                intent.putExtra("uid", user.getUserUid());
                    mContext.startActivity(intent);
            }
        });
    }
}


