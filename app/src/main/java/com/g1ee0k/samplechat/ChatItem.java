package com.g1ee0k.samplechat;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "chat_history_table")
public class ChatItem {

    @PrimaryKey
    @ColumnInfo(name = "timestamp")
    private long mUnixTimestamp;

    @ColumnInfo(name = "chat_text")
    private String mChatText; // The chat text (if any)

    @ColumnInfo(name = "image_ref")
    private String mImageRef; // The Firebase image id/reference (if any)

    @ColumnInfo(name = "Sender")
    private String mSenderId;

    @Ignore
    public ChatItem() {
    }

    public ChatItem(long unixTimestamp, @Nullable String chatText, @Nullable String imageRef, String senderId) {
        this.mChatText = chatText;
        this.mImageRef = imageRef;
        this.mUnixTimestamp = unixTimestamp;
        this.mSenderId = senderId;
    }

    public void setUnixTimestamp(long mUnixTimestamp) { // This had to be put due to a random error
        this.mUnixTimestamp = mUnixTimestamp;
    }

    public String getSenderId() {
        return this.mSenderId;
    }

    public long getUnixTimestamp() {
        return this.mUnixTimestamp;
    }

    public String getChatText() {
        return this.mChatText;
    }

    public String getImageRef() {
        if (mImageRef != null)
            return this.mImageRef;
        else return "";
    }
}
