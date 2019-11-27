package com.g1ee0k.samplechat;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

class ChatRepository {
    private ChatDao mChatDao;
    private LiveData<List<ChatItem>> mChatHist;

    ChatRepository(Application application) {
        ChatRoomDB db = ChatRoomDB.getDatabase(application);
        mChatDao = db.chatDao();
        mChatHist = mChatDao.getAllChats();
    }

    LiveData<List<ChatItem>> getChatHist() {
        return mChatHist;
    }

    // Must be called on a non-ui thread
    void insert(ChatItem chatItem) {
        ChatRoomDB.dbWriteExec.execute(() -> mChatDao.insert(chatItem));
    }
}
