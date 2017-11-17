package jacksonmeyer.com.archat.ViewHolders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import jacksonmeyer.com.archat.Models.chatMessage;
import jacksonmeyer.com.archat.R;

/**
 * Created by jacksonmeyer on 11/16/17.
 */

public class messagesViewHolder extends RecyclerView.ViewHolder {
        String TAG = "here";
        View mView;
        Context mContext;
        public messagesViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mContext = itemView.getContext();
            itemView.setOnClickListener((View.OnClickListener) mContext);
        }

        public void bindMessages(final chatMessage message) {
            TextView mMessageBody= (TextView) mView.findViewById(R.id.messageBody);
            mMessageBody.setText("yo buddy");

            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    getAdapterPosition();
                    Log.d("main", "clicked on" +message.getMessage());
                }
            });
        }
    }
