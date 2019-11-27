package com.g1ee0k.samplechat;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatHistAdapter extends RecyclerView.Adapter {

    private final int MESSAGE_SENDER_ME = 0;
    private final int MESSAGE_SENDER_THEM = 1;

    List<ChatItem> mChats;

    class ChatSentViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView chatTextview;

        private ChatSentViewHolder(View itemView) {
            super(itemView);
            chatTextview = itemView.findViewById(R.id.msg_sent_txt);
        }

        void bind(String message) {
            chatTextview.setText(message);
        }
    }

    class ChatReceivedViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView chatTextview;

        private ChatReceivedViewHolder(View itemView) {
            super(itemView);
            chatTextview = itemView.findViewById(R.id.msg_rcv_txt);
        }

        void bind(String message) {
            chatTextview.setText(message);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mChats.get(position).isSenderMe()){
            return MESSAGE_SENDER_ME;
        } else {
            return MESSAGE_SENDER_THEM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == MESSAGE_SENDER_ME) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg_sent, parent, false);
            return new ChatSentViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg_rcv, parent, false);
            return new ChatReceivedViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (mChats != null) {
            String message = mChats.get(position).getChatText();

            switch (holder.getItemViewType()) {
                case MESSAGE_SENDER_ME:
                    ((ChatSentViewHolder) holder).bind(message);
                    break;
                case MESSAGE_SENDER_THEM:
                    ((ChatReceivedViewHolder) holder).bind(message);
                    break;
            }
        }
    }

    void setChats(List<ChatItem> chats) {
        mChats = chats;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mChats != null)
            return mChats.size();
        else return 0;
    }
}
