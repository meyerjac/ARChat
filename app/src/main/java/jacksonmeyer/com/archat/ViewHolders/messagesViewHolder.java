package jacksonmeyer.com.archat.ViewHolders;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import jacksonmeyer.com.archat.Constants;
import jacksonmeyer.com.archat.Models.chatMessage;
import jacksonmeyer.com.archat.R;

/**
 * Created by jacksonmeyer on 11/16/17.
 */

public class messagesViewHolder extends RecyclerView.ViewHolder {
    private SharedPreferences mSharedPreferences;
    private String mLoggedInUserUid;
        String TAG = "here";
        View mView;
        Context mContext;

        public messagesViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mContext = itemView.getContext();
            itemView.setOnClickListener((View.OnClickListener) mContext);
            getUid();
        }

        public void bindMessages(final chatMessage message) {
            TextView mMessageBody = (TextView) mView.findViewById(R.id.messageBody);
            ConstraintLayout mChatBubble = (ConstraintLayout) mView.findViewById(R.id.chatBubble);
                String messageText = message.getMessage();
                String messageOwnerUid = message.getMessageOwnerUid();
                mMessageBody.setText(messageText);
            if (mLoggedInUserUid.equals(messageOwnerUid)) {
                mChatBubble.setBackgroundResource(R.drawable.shape_bg_outgoing_bubble);
            } else {
                mChatBubble.setBackgroundResource(R.drawable.shape_bg_incoming_bubble);
            }


            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    getAdapterPosition();
                    Log.d("main", "clicked on" +message.getMessage());
                }
            });
        }

    private void getUid() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mLoggedInUserUid = mSharedPreferences.getString(Constants.LOGGED_IN_UID, null);
    }
}
