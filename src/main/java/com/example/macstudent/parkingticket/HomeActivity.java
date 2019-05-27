package com.example.macstudent.parkingticket;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macstudent.parkingticket.db.AppDataBase;
import com.example.macstudent.parkingticket.model.Ticket;
import com.example.macstudent.parkingticket.model.User;

import java.util.List;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    TextView txtUserName;
    private TextView txtNumTickets;
    private TextView textFullName;
    private TextView textUserEmail;


    // Make a test user
    final String NAME = "Emad Nasrallah";
    final String EMAIL = "emad@gmail.com";
    final String PASSWORD = "1234";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        txtUserName = (TextView)findViewById(R.id.edtEmail);

        SharedPreferences sharedPreferences = getSharedPreferences("mypref", MODE_PRIVATE);
        String userName = sharedPreferences.getString("userid", null);


        // IF no user in shared preferences, then create a new test user
        if (userName == null) {
            User u = this.createTestAccount();
            if (u == null) {
                // check if emad is already in the system
                AppDataBase database = AppDataBase.getAppDataBase(this);
                User u2 = database.userDao().findByEmail(EMAIL);
                if (u2 == null) {
                    Log.d("TEST", "Error, cannot create a test user -> viewDidLoad()");
                    return;
                }
                else {
                    ((MyApplication)getApplicationContext()).setUser(u2);
                }

            }
            else {
                ((MyApplication)getApplicationContext()).setUser(u);
                this.updateSavedPreferences(u);
            }
        }

        if(userName != null)
        {
            if(drawer.isDrawerOpen(GravityCompat.START)) {
                txtUserName.setText("Welcome, " + userName);
            }
        }

        User user =  ((MyApplication)getApplicationContext()).getUser();
        AppDataBase database = AppDataBase.getAppDataBase(this);
        List<Ticket> tickets = database.ticketDao().findAll(user.getId());

        txtNumTickets = findViewById(R.id.txtNumTickets);
        txtNumTickets.setText(String.valueOf(tickets.size()));

        View header = navigationView.getHeaderView(0);
        textFullName = header.findViewById(R.id.textFullName);
        textFullName.setText(user.getFullName());

        textUserEmail = header.findViewById(R.id.textUserEmail);
        textUserEmail.setText(user.getEmail());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_home) {
            startActivity(new Intent(HomeActivity.this, HomeActivity.class));
        } else if (id == R.id.nav_addticket) {

            startActivity(new Intent(HomeActivity.this, AddParkingTicketActivity.class));

        } else if (id == R.id.nav_location) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("geo:0,0?q=43.6497688362,-79.38952512778(" + getString(R.string.app_name) + ")"));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
            else
            {
                Toast.makeText(this, "Maps application is not available.", Toast.LENGTH_LONG).show();
            }

        } else if (id == R.id.nav_report) {
            startActivity(new Intent(HomeActivity.this, TicketReportActivity.class));

        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(HomeActivity.this, UpdateProfileActivity.class));

        } else if (id == R.id.nav_instruction) {
            startActivity(new Intent(HomeActivity.this, InstructionActivity.class));

        }else if (id == R.id.nav_contact) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
            alertDialogBuilder.setTitle("Parking Ticket");
            alertDialogBuilder.setMessage("Contact Information:\nPhone: +1123456789\nemail: contact@aslan.com");
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
            alertDialogBuilder.setPositiveButton("DISCARD", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    Crouton.showText(HomeActivity.this, "Success", Style.ALERT);
                }
            });
            alertDialogBuilder.show();
        } else if (id == R.id.nav_logout) {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    // Returns a user if user is created, null otherwise
    private User createTestAccount()
    {
        // check for existing user
        AppDataBase database = AppDataBase.getAppDataBase(this);
        User user = database.userDao().findByEmail(EMAIL);

        if (user != null)
        {
            Log.d("TEST","Email already registered.");
            return null;
        }

        user = new User();
        user.setId(database.userDao().findMaxId() + 1);
        user.setFullName(NAME);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);

        database.userDao().insert(user);


        return user;
    }


    private void updateSavedPreferences(User u) {
        // create shared preferences object.

        SharedPreferences preferences = getSharedPreferences("parking-ticket-prefs", Context.MODE_PRIVATE);
        // get editor object.
        SharedPreferences.Editor editor = preferences.edit();

        // save value to shared preferences using editor object.
        editor.putString("user-email", u.getEmail());
        editor.putString("user-password", u.getPassword());

        // save changes permanently into shared preferences.
        editor.apply();
    }



}

