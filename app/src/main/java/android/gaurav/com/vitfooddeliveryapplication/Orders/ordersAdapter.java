package android.gaurav.com.vitfooddeliveryapplication.Orders;

import android.content.Context;
import android.gaurav.com.vitfooddeliveryapplication.OrdersClass;
import android.gaurav.com.vitfooddeliveryapplication.R;
import android.gaurav.com.vitfooddeliveryapplication.Transaction.TransactionClass;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ordersAdapter extends ArrayAdapter {
    Context context;
    ArrayList<OrdersClass> items;

    public ordersAdapter(@NonNull Context context, int resource, @NonNull ArrayList<OrdersClass> items) {
        super(context, resource, items);
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position,@Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.order_adatepter,null);
        }

        TextView service, time;
        service = convertView.findViewById(R.id.service);
        time = convertView.findViewById(R.id.time);

        OrdersClass order = items.get(position);

        if(order!=null)
        {
            service.setText(order.getService());
            time.setText(order.getTiming());
        }

        return convertView;
    }
}
