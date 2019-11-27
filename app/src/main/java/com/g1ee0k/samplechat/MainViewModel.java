package com.g1ee0k.samplechat;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.g1ee0k.samplechat.ChatItem;
import com.g1ee0k.samplechat.ChatRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private ChatRepository mRepo;

    private LiveData<List<ChatItem>> mChatHist;

    public MainViewModel(Application application) {
        super(application);
        mRepo = new ChatRepository(application);
        mChatHist = mRepo.getChatHist();
    }

    public LiveData<List<ChatItem>> getChatHist() {return mChatHist;}

    public void insert(ChatItem chatItem) { mRepo.insert(chatItem);}
}
