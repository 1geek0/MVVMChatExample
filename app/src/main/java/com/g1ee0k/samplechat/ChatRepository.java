package com.g1ee0k.samplechat;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

class ChatRepository {
    private ChatDao mChatDao;
    private LiveData<List<ChatItem>> mChatHist;

    private final String firebasePath = "nilaytobot"; // Hardcoded path on firebase db. Represents chat initiated between 2 unique ids
    private final String senderId = "nilay"; // Hardcoded sender id
    // Firebase Refs
    private FirebaseDatabase mFirebaseDB;
    private FirebaseStorage mStorage;
    private String[] botResponses = new String[]{"Hello There", "I like talking to you", "Nice to meet you!"};
    private List<ChatItem> holdList = new ArrayList<>();

    ChatRepository(Application application) {
        ChatRoomDB db = ChatRoomDB.getDatabase(application);
        mChatDao = db.chatDao();
        mChatHist = mChatDao.getAllChats();

        mFirebaseDB = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance();
    }


    LiveData<List<ChatItem>> getChatHist() {
        return mChatHist;
    }

    @NonNull
    LiveData<List<ChatItem>> getChatHistLive() {
        FirebaseQueryChat mDataLive = new FirebaseQueryChat(mFirebaseDB.getReference(firebasePath));
        mChatHist = Transformations.map(mDataLive, new Deserializer());
        return mChatHist;
    }

    String getSenderId() {
        return senderId;
    }

    // Must be called on a non-ui thread
    void insert(ChatItem chatItem) {
        ChatRoomDB.dbWriteExec.execute(() -> {
            mChatDao.insert(chatItem);
            mFirebaseDB.getReference(firebasePath).child(String.valueOf(chatItem.getUnixTimestamp())).setValue(chatItem);
            mFirebaseDB.getReference(firebasePath).child(String.valueOf(chatItem.getUnixTimestamp() + 1)).setValue(botChatItem());
        });
    }

    void uploadImage(Uri imgPath) {
        StorageReference imgRef = mStorage.getReference().child(Objects.requireNonNull(imgPath.getLastPathSegment()));
        UploadTask uploadTask = imgRef.putFile(imgPath);
        Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }

            return imgRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                insert(new ChatItem(System.currentTimeMillis() / 1000L, "", Objects.requireNonNull(imgPath.getLastPathSegment()), getSenderId()));
            }
        });
    }

    private ChatItem botChatItem() {
        return new ChatItem((System.currentTimeMillis() / 1000L) + 1, botResponses[randInt(botResponses.length)], null, "bot");
    }

    private int randInt(int max) { // Returns a random integer between 0 and max
        return new Random().nextInt(max);
    }

    private class Deserializer implements Function<DataSnapshot, List<ChatItem>> {
        @Override
        public List<ChatItem> apply(DataSnapshot input) {
            holdList.clear();
            for (DataSnapshot snapshot : input.getChildren()) {
                ChatItem chatItem = new ChatItem(Long.parseLong(String.valueOf(snapshot.child("unixTimestamp").getValue())), String.valueOf(snapshot.child("chatText").getValue()), String.valueOf(snapshot.child("imageRef").getValue()), String.valueOf(snapshot.child("senderId").getValue()));
                holdList.add(chatItem);
            }
            return holdList;
        }
    }
}
