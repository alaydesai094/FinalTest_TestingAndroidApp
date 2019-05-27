package com.example.macstudent.parkingticket.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.macstudent.parkingticket.model.Ticket;
import com.example.macstudent.parkingticket.model.User;

/**
 * Created by C0724671/C0727631 on 2018-04-12.
 */

@Database(entities = {User.class, Ticket.class}, version = 2)
public abstract class AppDataBase extends RoomDatabase
{
    private static AppDataBase instance;

    public abstract UserDao userDao();
    public abstract TicketDao ticketDao();

    /**
     * Singleton design pattern to instantiating an AppDatabase object.
     *
     * @param context
     * @return
     */
    public static AppDataBase getAppDataBase(Context context)
    {
        if (instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDataBase.class,
                    "parking-ticked-db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }
}
