package com.example.macstudent.parkingticket.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.macstudent.parkingticket.model.User;

import java.util.List;

/**
 * Created by C0724671/C0727631 on 2018-04-12.
 */

@Dao
public interface UserDao
{
    @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
    public User findByEmail(String email);

    @Query("SELECT MAX(id) FROM user")
    public int findMaxId();

    @Insert
    public void insert(User user);

    @Update
    public void update(User user);

    @Delete
    public void delete(User user);
}
