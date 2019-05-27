package com.example.macstudent.parkingticket.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.macstudent.parkingticket.model.Ticket;

import java.util.List;

/**
 * Created by C0724671/C0727631 on 2018-04-16.
 */

@Dao
public interface TicketDao
{
    @Query("SELECT * FROM ticket WHERE user_id = :userId")
    public List<Ticket> findAll(int userId);

    @Query("SELECT MAX(id) FROM ticket")
    public int findMaxId();

    @Insert
    public void insert(Ticket ticket);

    @Update
    public void update(Ticket ticket);

    @Delete
    public void delete(Ticket ticket);
}
