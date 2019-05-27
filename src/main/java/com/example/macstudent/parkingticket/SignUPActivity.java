package com.example.macstudent.parkingticket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.macstudent.parkingticket.db.AppDataBase;
import com.example.macstudent.parkingticket.model.User;
import com.example.macstudent.parkingticket.util.Utils;

public class SignUPActivity extends AppCompatActivity
{
    private Button btnLoginPage;
    private Button btnCreateAccount;

    private EditText edtFullName;
    private EditText edtEmail;
    private EditText edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnLoginPage = findViewById(R.id.btnLoginPage);
        btnLoginPage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUPActivity.this, LoginActivity.class));
            }
        });

        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        btnCreateAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });

        edtFullName = findViewById(R.id.edtFullName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
    }

    /**
     * Create a new User information in the database
     */
    private void createAccount()
    {
        // check for blank or invalid inputs
        if (Utils.isEmpty(edtFullName))
        {
            edtFullName.setError("Please enter your full name.");
            return;
        }

        if (Utils.isEmpty(edtEmail) || !Utils.isValidEmail(edtEmail.getText().toString()))
        {
            edtEmail.setError("Please enter a valid email.");
            return;
        }

        if (Utils.isEmpty(edtPassword))
        {
            edtPassword.setError("Please enter a valid password.");
            return;
        }

        // check for existing user
        AppDataBase database = AppDataBase.getAppDataBase(this);
        User user = database.userDao().findByEmail(edtEmail.getText().toString());

        if (user != null)
        {
            edtEmail.setError("Email already registered.");
            return;
        }

        user = new User();
        user.setId(database.userDao().findMaxId() + 1);
        user.setFullName(edtFullName.getText().toString());
        user.setEmail(edtEmail.getText().toString());
        user.setPassword(edtPassword.getText().toString());

        database.userDao().insert(user);

        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }
}
