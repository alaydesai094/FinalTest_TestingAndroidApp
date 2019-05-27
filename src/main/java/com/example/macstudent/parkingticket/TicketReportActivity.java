package com.example.macstudent.parkingticket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.macstudent.parkingticket.adapters.TicketReportAdapter;
import com.example.macstudent.parkingticket.db.AppDataBase;
import com.example.macstudent.parkingticket.model.Ticket;
import com.example.macstudent.parkingticket.model.User;

import java.util.List;

public class TicketReportActivity extends AppCompatActivity
{
    private ListView lstTicketReport;
    private TicketReportAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_report);

        AppDataBase database = AppDataBase.getAppDataBase(this);
        User user = ((MyApplication)getApplicationContext()).getUser();

        List<Ticket> ticketList = database.ticketDao().findAll(user.getId());
        adapter = new TicketReportAdapter(this, ticketList);

        lstTicketReport = findViewById(R.id.lstTicketReport);
        lstTicketReport.setAdapter(adapter);
    }
}
