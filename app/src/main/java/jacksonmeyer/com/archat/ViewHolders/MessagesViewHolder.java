package jacksonmeyer.com.archat.ViewHolders;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jacksonmeyer.com.archat.Constants;
import jacksonmeyer.com.archat.Models.ChatMessage;
import jacksonmeyer.com.archat.R;

/**
 * Created by jacksonmeyer on 11/16/17.
 */

public class MessagesViewHolder extends RecyclerView.ViewHolder {
    private SharedPreferences mSharedPreferences;
    private String mLoggedInUserUid;
        String TAG = "here";
        View mView;
        Context mContext;
        String String1 = "clicked";

    private List<String> primaryLanguage = new ArrayList<String>();
    private List<String> secondaryLanguage = new ArrayList<String>();

        public MessagesViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mContext = itemView.getContext();
            itemView.setOnClickListener((View.OnClickListener) mContext);
            getUid();
        }

        public void bindMessages(final ChatMessage message) {
            final TextView mMessageBody = (TextView) mView.findViewById(R.id.messageBody);
            ConstraintLayout mChatBubble = (ConstraintLayout) mView.findViewById(R.id.chatBubble);
            final String messageText = message.getSecMessage();
            String messageOwnerUid = message.getMessageOwnerUid();
            mMessageBody.setText(messageText);
            final String primaryString = message.getPrimMessage();
            final String secondaryString = message.getSecMessage();

            if (mLoggedInUserUid.equals(messageOwnerUid)) {
                mChatBubble.setBackgroundResource(R.drawable.shape_bg_outgoing_bubble);
            } else {
                mChatBubble.setBackgroundResource(R.drawable.shape_bg_incoming_bubble);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (mMessageBody.getText().toString().equals(primaryString)) {
                        mMessageBody.setText(secondaryString);
                    } else {
                        mMessageBody.setText(primaryString);
                    }

                }
            });
        }

    private void getUid() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mLoggedInUserUid = mSharedPreferences.getString(Constants.SHARED_PREFERENCES_KEY_LOGGED_IN_UID, null);
    }
}
