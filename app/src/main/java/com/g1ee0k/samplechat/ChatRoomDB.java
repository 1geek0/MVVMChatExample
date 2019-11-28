package com.g1ee0k.samplechat;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

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
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ChatRoomDB.class, "chat_database")
                            .addCallback(mRoomDBCallback).build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback mRoomDBCallback = new Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            dbWriteExec.execute(()->{
                ChatDao dao = INSTANCE.chatDao();
                dao.clearChat();
                ChatItem mItem = new ChatItem(System.currentTimeMillis() / 1000L, "Hi Jini", null, "nilay"); // couldn't use the hardcoded value here
                dao.insert(mItem);
            });
        }
    };
}
