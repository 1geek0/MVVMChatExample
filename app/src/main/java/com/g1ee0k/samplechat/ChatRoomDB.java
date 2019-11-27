package com.g1ee0k.samplechat;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Room Database object on top of the actual sqlite db
@Database(entities = {ChatItem.class}, version = 1, exportSchema = false)
public abstract class ChatRoomDB extends RoomDatabase {

    public abstract ChatDao chatDao();

    private static volatile ChatRoomDB INSTANCE;
    static final ExecutorService dbWriteExec = Executors.newSingleThreadExecutor();

    static ChatRoomDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ChatRoomDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ChatRoomDB.class, "chat_database").build();
                }
            }
        }
        return INSTANCE;
    }
}
