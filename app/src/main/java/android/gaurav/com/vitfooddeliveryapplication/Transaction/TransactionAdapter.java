package android.gaurav.com.vitfooddeliveryapplication.Transaction;

import android.content.Context;
import android.gaurav.com.vitfooddeliveryapplication.R;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TransactionAdapter extends ArrayAdapter {

    Context context;
    ArrayList<TransactionClass> items;

    public TransactionAdapter(@NonNull Context context, int resource, @NonNull ArrayList<TransactionClass> items) {
        super(context, resource, items);
        this.context = context;
        this.items = items;
    }


    @NonNull
    @Override
    public View getView(int position,@Nullable View convertView,@NonNull ViewGroup parent) {
        if(convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.transaction_adapter,null);
        }

        TextView amount, time, date, name;
        ImageView indicatorIcon;

        amount = convertView.findViewById(R.id.amount);
        time = convertView.findViewById(R.id.time);
        date = convertView.findViewById(R.id.date);
        name = convertView.findViewById(R.id.name);
        indicatorIcon = convertView.findViewById(R.id.indicator_icon);

        TransactionClass transaction = items.get(position);

        if(transaction!=null)
        {
            if(transaction.getToEmail().equals("self"))
            {
                amount.setText("+Rs. ");
                amount.setTextColor(ContextCompat.getColor(context, R.color.green));
                indicatorIcon.setImageResource(R.drawable.ic_arrow_upward_green_24dp);
            }
            else
            {
                amount.setText("-Rs. ");
                amount.setTextColor(ContextCompat.getColor(context, R.color.red));
                indicatorIcon.setImageResource(R.drawable.ic_arrow_downward_red_24dp);
            }
            amount.setText(amount.getText().toString()+transaction.getAmount());
            name.setText(transaction.getName());
            time.setText(transaction.getTime());
            date.setText(transaction.getDate());
        }

        return convertView;
    }
}
