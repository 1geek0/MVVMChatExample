package com.g1ee0k.samplechat;

import android.app.Application;
import android.net.Uri;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private ChatRepository mRepo;

    private LiveData<List<ChatItem>> mChatHist;

    public MainViewModel(Application application) {
        super(application);
        mRepo = new ChatRepository(application);
        mChatHist = mRepo.getChatHistLive();
    }

    public String getSender() {
        return mRepo.getSenderId();
    }

    public LiveData<List<ChatItem>> getChatHist() {return mChatHist;}

    public void uploadImage(Uri imgPath) {
        mRepo.uploadImage(imgPath);
    }

    public void insert(String chatTxt, String imgRef, String sender) {
        mRepo.insert(new ChatItem(System.currentTimeMillis() / 1000L, chatTxt, imgRef, sender));
    }
}
