package com.g1ee0k.samplechat;


import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class ChatHistAdapter extends RecyclerView.Adapter {

    private final int MESSAGE_SENDER_ME = 0;
    private final int MESSAGE_SENDER_THEM = 1;
    private final int IMAGE_SENDER_ME = 2;

    List<ChatItem> mChats;
    String sender;

    @Override
    public int getItemViewType(int position) {
        if (mChats.get(position).getSenderId().equals(sender)) {
            if (mChats.get(position).getChatText().equals("")) {
                return IMAGE_SENDER_ME;
            } else {
                return MESSAGE_SENDER_ME;
            }
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
        } else if (viewType == IMAGE_SENDER_ME) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_img_sent, parent, false);
            return new ImageSentViewHolder(view);
        } else if (viewType == MESSAGE_SENDER_THEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg_rcv, parent, false);
            return new ChatReceivedViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg_rcv, parent, false);
            return new ChatReceivedViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (mChats != null) {
            ChatItem currentChatItem = mChats.get(position);

            switch (holder.getItemViewType()) {
                case MESSAGE_SENDER_ME:
                    ((ChatSentViewHolder) holder).bindText(currentChatItem.getChatText());
                    break;
                case IMAGE_SENDER_ME:
                    ((ImageSentViewHolder) holder).bindImage(mChats.get(position).getImageRef());
                    break;
                case MESSAGE_SENDER_THEM:
                    ((ChatReceivedViewHolder) holder).bind(currentChatItem.getChatText());
                    break;
            }
        }
    }

    void setSender(String sender) {
        this.sender = sender;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (mChats != null)
            return mChats.size();
        else return 0;
    }

    void setChats(List<ChatItem> chats) {
        mChats = chats;
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

    class ChatSentViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView chatTextview;

        private ChatSentViewHolder(View itemView) {
            super(itemView);
            chatTextview = itemView.findViewById(R.id.msg_sent_txt);
        }

        void bindText(String message) {
            chatTextview.setText(message);
        }
    }

    class ImageSentViewHolder extends RecyclerView.ViewHolder {
        private AppCompatImageView chatImageView;

        private ImageSentViewHolder(View itemView) {
            super(itemView);
            chatImageView = itemView.findViewById(R.id.msg_sent_img);
        }

        void bindImage(String imgRef) {
            Log.d("MYTAG", "Loading Image: " + imgRef);
            chatImageView.setVisibility(View.VISIBLE);
            GlideApp.with(chatImageView.getContext())
                    .load(FirebaseStorage.getInstance().getReference().child(imgRef))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            e.printStackTrace();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            Log.d("MYTAG", "Download Done");
                            return false;
                        }
                    })
                    .into(chatImageView);
        }
    }
}
