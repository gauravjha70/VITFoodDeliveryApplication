package android.gaurav.com.vitfooddeliveryapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class RequestListAdapter extends ArrayAdapter<OrdersClass> {

    ArrayList<OrdersClass> items = new ArrayList<>();
    Context context;
    OrdersClass order;

    public RequestListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<OrdersClass> items) {
        super(context, resource, items);
        this.context = context;
        this.items = items;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup itemView) {
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.orders_list_adapter, null);
        }

        TextView service, name, deliveryAddress, time, price;

        service = convertView.findViewById(R.id.service);
        name = convertView.findViewById(R.id.name);
        deliveryAddress = convertView.findViewById(R.id.delivery_address);
        time = convertView.findViewById(R.id.time);
        price = convertView.findViewById(R.id.price);

        order = getItem(position);
        Log.e("service",order.getService()+"");

        if(order!=null) {
            service.setText(order.getService());
            name.setText(order.getName());
            deliveryAddress.setText(order.getDeliveryAddress());
            time.setText(order.getTiming());
            price.setText("Rs. " + order.getPrice());
        }

        return convertView;
    }

}
