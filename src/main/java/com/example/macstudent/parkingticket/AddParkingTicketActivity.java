package com.example.macstudent.parkingticket;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.macstudent.parkingticket.db.AppDataBase;
import com.example.macstudent.parkingticket.db.TicketDao;
import com.example.macstudent.parkingticket.model.Ticket;
import com.example.macstudent.parkingticket.model.User;
import com.example.macstudent.parkingticket.util.Utils;

import java.util.Date;

public class AddParkingTicketActivity extends AppCompatActivity
{
    private TextView txtCurrentDate;
    private TextView txtTotalCost;
    private EditText edtVehicleNumber;
    private ImageView imgVehicleBrand;
    private Spinner spnVehicleBrand;
    private Spinner spnColor;
    private Spinner spnLane;
    private Spinner spnLevel;
    private Spinner spnPayment;

    private String parkingTime = "1/2 Hour";
    private double price = 5.00;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parking_ticket);

        txtCurrentDate = findViewById(R.id.txtCurrentDate);
        txtCurrentDate.setText(Utils.formatDate(new Date()));

        User user =  ((MyApplication)getApplicationContext()).getUser();

        Log.d("TESTING", user.getFullName());
        edtVehicleNumber = findViewById(R.id.edtVehicleNumber);
        edtVehicleNumber.setText(user != null ? user.getVehicleNumber() : "");

        txtTotalCost = findViewById(R.id.txtTotalCost);
        txtTotalCost.setText(String.format("$ %.02f", 5.0));

        imgVehicleBrand = findViewById(R.id.imgVehicleBrand);
        imgVehicleBrand.setBackgroundResource(R.drawable.acura);

        spnVehicleBrand = findViewById(R.id.spnVehicleBrand);
        spnVehicleBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loadVehicleBrandIcon(spnVehicleBrand.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        spnColor = findViewById(R.id.spnColor);
        spnLane = findViewById(R.id.spnLane);
        spnLevel = findViewById(R.id.spnLevel);
        spnPayment = findViewById(R.id.spnPayment);
    }

    public void onRadioButtonClicked(View view)
    {
        if (!((RadioButton)view).isChecked()) {
            return;
        }

        switch(view.getId())
        {
            case R.id.rbHalfHour:
                price = 5.00;
                parkingTime = "1/2 Hour";
                break;

            case R.id.rbOneHour:
                price = 7.25;
                parkingTime = "1 Hour";
                break;

            case R.id.rbTwoHours:
                price = 8.50;
                parkingTime = "2 Hours";
                break;

            case R.id.rbThreeHours:
                price = 10.00;
                parkingTime = "3 Hours";
                break;

            case R.id.rbDayEnds:
                price = 15.00;
                parkingTime = "Ends Day";
                break;
        }

        txtTotalCost.setText(String.format("$ %.02f", price));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_ticket, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_save:
                if (valid()) {
                    showConfirmationDialog();
                }
                break;
            default:
                break;
        }

        return true;
    }

    private void loadVehicleBrandIcon(String filename)
    {
        int id = imgVehicleBrand.getContext().getResources().getIdentifier(
                filename.toLowerCase().replace('-','_'),
                "drawable", getPackageName());
        imgVehicleBrand.setBackgroundResource(id);
    }

    private boolean valid()
    {
        if (Utils.isEmpty(edtVehicleNumber))
        {
            edtVehicleNumber.setError("Please enter a valid vehicle number.");
            return false;
        }

        return true;
    }

    private void showConfirmationDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to print the ticket?");
        builder.setCancelable(false);
        builder.setIcon(android.R.drawable.ic_menu_help);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which){
                addTicket();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which){}
        });

        builder.show();
    }

    public void addTicketPressed(View view) {
        User u = ((MyApplication)getApplicationContext()).getUser();
        System.out.println("User: " + u.getFullName());

        this.addTicket();
    }
    private void addTicket()
    {
        AppDataBase database = AppDataBase.getAppDataBase(this);
        TicketDao dao = database.ticketDao();

        Ticket ticket = new Ticket();
        ticket.setId(dao.findMaxId() + 1);
        ticket.setDate(txtCurrentDate.getText().toString());
        ticket.setParkingLane(spnLane.getSelectedItem().toString());
        ticket.setParkingSpot(spnLevel.getSelectedItem().toString());
        ticket.setParkingTime(parkingTime);
        ticket.setPaymentMethod(spnPayment.getSelectedItem().toString());
        ticket.setPrice(price);
        ticket.setUserId(((MyApplication)getApplicationContext()).getUser().getId());
        ticket.setVehicleNumber(edtVehicleNumber.getText().toString());
        ticket.setVehicleMaker(spnVehicleBrand.getSelectedItem().toString());
        ticket.setVehicleColor(spnColor.getSelectedItem().toString());

        dao.insert(ticket);

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
