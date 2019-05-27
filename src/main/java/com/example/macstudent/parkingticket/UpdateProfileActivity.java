package com.example.macstudent.parkingticket;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.macstudent.parkingticket.db.AppDataBase;
import com.example.macstudent.parkingticket.model.User;
import com.example.macstudent.parkingticket.util.Utils;

public class UpdateProfileActivity extends AppCompatActivity
{
    private TextView txtEmail;
    private EditText edtFullName;
    private EditText edtVehicleNumber;
    private EditText edtPhone;
    private EditText edtOldPassword;
    private EditText edtNewPassword;
    private EditText edtConfirmPassword;

    private Button btnUpdate;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        //user = LoginActivity.getAuthenticatedUser();

        user = ((MyApplication)getApplicationContext()).getUser();

        txtEmail = findViewById(R.id.txtEmail);
        txtEmail.setText(user.getEmail());

        edtFullName = findViewById(R.id.edtFullName);
        edtFullName.setText(user.getFullName());

        edtVehicleNumber = findViewById(R.id.edtVehicleNumber);
        edtVehicleNumber.setText(user.getVehicleNumber());

        edtPhone = findViewById(R.id.edtPhone);
        edtPhone.setText(user.getPhone());

        edtOldPassword = findViewById(R.id.edtOldPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);

        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });
    }

    private void updateProfile()
    {
        if (Utils.isEmpty(edtFullName))
        {
            edtFullName.setError("Please enter the Full Name.");
            return;
        }

        if (Utils.isEmpty(edtVehicleNumber))
        {
            edtVehicleNumber.setError("Please enter a valid vehicle number.");
            return;
        }

        if (Utils.isEmpty(edtPhone))
        {
            edtPhone.setError("Please enter the Phone Number.");
            return;
        }

        if (!Utils.isEmpty(edtOldPassword) || !Utils.isEmpty(edtNewPassword) || !Utils.isEmpty(edtConfirmPassword))
        {
            if (Utils.isEmpty(edtOldPassword))
            {
                edtOldPassword.setError("Please enter the old password.");
                return;
            }

            if (Utils.isEmpty(edtNewPassword))
            {
                edtNewPassword.setError("Please enter the new password.");
                return;
            }

            if (Utils.isEmpty(edtConfirmPassword))
            {
                edtConfirmPassword.setError("Please enter the confirmation password.");
                return;
            }

            String oldPassword = edtOldPassword.getText().toString();

            if (!oldPassword.equals(user.getPassword()))
            {
                edtOldPassword.setError("Invalid password.");
                return;
            }

            String newPassword = edtNewPassword.getText().toString();
            String confirmPassword = edtConfirmPassword.getText().toString();

            if (!confirmPassword.equals(newPassword))
            {
                edtConfirmPassword.setError("New password and confirmation don't match");
                return;
            }

            user.setPassword(newPassword);
        }

        user.setFullName(edtFullName.getText().toString());
        user.setPhone(edtPhone.getText().toString());
        user.setVehicleNumber(edtVehicleNumber.getText().toString());

        AppDataBase database = AppDataBase.getAppDataBase(this);
        database.userDao().update(user);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Profile Update");
        builder.setMessage("User information successfully updated.");
        builder.setCancelable(false);
        builder.setIcon(android.R.drawable.ic_menu_info_details);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which){
                startActivity(new Intent(UpdateProfileActivity.this, HomeActivity.class));
            }
        });

        builder.show();
    }
}
