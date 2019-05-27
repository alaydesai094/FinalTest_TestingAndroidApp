package com.example.macstudent.parkingticket.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.macstudent.parkingticket.R;
import com.example.macstudent.parkingticket.model.Ticket;

import java.util.List;

public class TicketReportAdapter extends ArrayAdapter<Ticket>
{
    public TicketReportAdapter(Context context, List<Ticket> ticketList) {
        super(context, 0, ticketList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        // Get the data item for this position
        Ticket ticket = getItem(position);
        ViewHolder viewHolder;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_ticket, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.set(ticket);
        return convertView;
    }

    private class ViewHolder
    {
        private ImageView imgBrand;
        private TextView txtCurrentDate;
        private TextView txtPrice;
        private TextView txtVehicleNumber;
        private TextView txtVehicleMaker;
        private TextView txtColor;
        private TextView txtTime;
        private TextView txtLane;
        private TextView txtSpot;
        private TextView txtPayment;

        ViewHolder(View convertView)
        {
            imgBrand = (ImageView) convertView.findViewById(R.id.imgBrand);
            txtCurrentDate = (TextView) convertView.findViewById(R.id.txtCurrentDate);
            txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
            txtVehicleNumber = (TextView) convertView.findViewById(R.id.txtVehicleNumber);
            txtVehicleMaker = (TextView) convertView.findViewById(R.id.txtVehicleMaker);
            txtColor = (TextView) convertView.findViewById(R.id.txtColor);
            txtTime = (TextView) convertView.findViewById(R.id.txtTime);
            txtLane = (TextView) convertView.findViewById(R.id.txtLane);
            txtSpot = (TextView) convertView.findViewById(R.id.txtSpot);
            txtPayment = (TextView) convertView.findViewById(R.id.txtPayment);
        }

        void set(Ticket ticket)
        {
            int id = imgBrand.getContext().getResources().getIdentifier(
                    ticket.getVehicleMaker().toLowerCase().replace('-','_'), "drawable",
                    getContext().getPackageName());
            imgBrand.setBackgroundResource(id);

            txtCurrentDate.setText(ticket.getDate());
            txtPrice.setText(String.format("$ %.02f", ticket.getPrice()));
            txtVehicleNumber.setText(ticket.getVehicleNumber());
            txtVehicleMaker.setText(ticket.getVehicleMaker());
            txtColor.setText(ticket.getVehicleColor());
            txtLane.setText(ticket.getParkingLane());
            txtSpot.setText(ticket.getParkingSpot());
            txtPayment.setText(ticket.getPaymentMethod());
        }
    }
}
