package com.example.macstudent.parkingticket.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.security.Policy;

/**
 * Created by C0724671/C0727631 on 2018-04-12.
 */

@Entity
public class User implements Parcelable
{
    @PrimaryKey
    private int id;

    private String fullName;
    private String email;
    private String phone;
    private String vehicleNumber;
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void writeToParcel(Parcel out, int i)
    {
        out.writeInt(id);
        out.writeString(fullName);
        out.writeString(email);
        out.writeString(phone);
        out.writeString(vehicleNumber);
        out.writeString(password);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>()
    {
        @Override
        public User createFromParcel(Parcel in)
        {
            User user = new User();
            user.id = in.readInt();
            user.fullName = in.readString();
            user.email = in.readString();
            user.phone = in.readString();
            user.vehicleNumber = in.readString();
            user.password = in.readString();

            return user;
        }

        @Override
        public User[] newArray(int i) {
            return new User[0];
        }
    };
}
