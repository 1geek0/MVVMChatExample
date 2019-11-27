package com.g1ee0k.samplechat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "chat_history_table")
public class ChatItem {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "firebase_id")
    private String mFireBaseRef; // The firebase id for the chat item

    @ColumnInfo(name = "timestamp")
    private long mUnixTimestamp;

    @ColumnInfo(name = "chat_text")
    private String mChatText; // The chat text (if any)

    @ColumnInfo(name = "image_ref")
    private String mImageRef; // The Firebase image id/reference (if any)

    @ColumnInfo(name = "SenderIsMe")
    private boolean mIsSenderMe;

    public ChatItem(@NonNull String fireBaseRef, @Nullable String chatText, @Nullable String imageRef, boolean isSenderMe, long unixTimestamp) {
        this.mFireBaseRef = fireBaseRef;
        this.mChatText = chatText;
        this.mImageRef = imageRef;
        this.mUnixTimestamp = unixTimestamp;
        this.mIsSenderMe = isSenderMe;
    }

    public boolean isSenderMe() {
        return mIsSenderMe;
    }

    public long getUnixTimestamp() {
        return this.mUnixTimestamp;
    }

    public String getChatText() {
        return this.mChatText;
    }

    public String getImageRef() {
        return this.mImageRef;
    }

    public String getFireBaseRef() {
        return this.mFireBaseRef;
    }
}
