package com.g1ee0k.samplechat;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

// Database Access Object (DAO) creates an interface for the viewmodel to interact with the data
@Dao
public interface ChatDao {
    @Insert()
    void insert(ChatItem chatItem);

    @Query("SELECT * from chat_history_table ORDER BY timestamp ASC")
    LiveData<List<ChatItem>> getAllChats();
}
