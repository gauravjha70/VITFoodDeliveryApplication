package android.gaurav.com.vitfooddeliveryapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderDetailFragment extends Fragment {

    TextView service, address, time, price, message, name;
    Button acceptButton;
    OrdersClass ordersClass;
    ImageView profileImage;

    String orderType="", orderID = "";

    ConfirmOrderBottomFragment confirmOrderBottomFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.order_detail_fragment,container,false);

        service = rootView.findViewById(R.id.service);
        address = rootView.findViewById(R.id.delivery_address);
        time = rootView.findViewById(R.id.time);
        price = rootView.findViewById(R.id.price);
        message = rootView.findViewById(R.id.message);
        name = rootView.findViewById(R.id.name);
        acceptButton = rootView.findViewById(R.id.accept_button);
        profileImage = rootView.findViewById(R.id.profile_img);

        Bundle bundle = getArguments();
        ordersClass = (OrdersClass) bundle.getSerializable("object");
        orderType = bundle.getString("orderType","");

        if(orderType.equals("orderCancel"))
        {
            service.setText(ordersClass.getService());
            address.setText(ordersClass.getDeliveryAddress());
            time.setText(ordersClass.getTiming());
            price.setText("Rs. "+ordersClass.getPrice());
            message.setText(ordersClass.getMessage());
            name.setText(ordersClass.getOrderStatus());
            profileImage.setImageResource(R.drawable.groceries);
            acceptButton.setText("CANCEL ORDER");
            orderID = bundle.getString("orderID","");
            if(ordersClass.getService().equals("Cancelled"))
            {
                acceptButton.setVisibility(View.GONE);
            }

        }
        else{
            service.setText(ordersClass.getService());
            address.setText(ordersClass.getDeliveryAddress());
            time.setText(ordersClass.getTiming());
            price.setText("Rs. "+ordersClass.getPrice());
            message.setText(ordersClass.getMessage());
            name.setText(ordersClass.getName());
            orderID = bundle.getString("orderID","");
        }



        //OnAccept
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(orderType.equals("orderCancel"))
                {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("object",ordersClass);
                    bundle.putString("orderID",orderID);
                    bundle.putString("orderType", "orderCancel");

                    confirmOrderBottomFragment = new ConfirmOrderBottomFragment();
                    confirmOrderBottomFragment.setArguments(bundle);
                    confirmOrderBottomFragment.show(getChildFragmentManager(),confirmOrderBottomFragment.getTag());
                }
                else{

                    //Starting Delivery Process
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("object",ordersClass);
                    bundle.putString("orderType", "orderAccept");
                    bundle.putString("service",service.getText().toString());
                    bundle.putString("time", time.getText().toString());
                    bundle.putDouble("price",ordersClass.getPrice());
                    bundle.putString("message",message.getText().toString());
                    bundle.putString("address",address.getText().toString());
                    bundle.putString("name",name.getText().toString());
                    bundle.putString("orderID",orderID);

                    confirmOrderBottomFragment = new ConfirmOrderBottomFragment();
                    confirmOrderBottomFragment.setArguments(bundle);
                    confirmOrderBottomFragment.show(getChildFragmentManager(),confirmOrderBottomFragment.getTag());
                }


            }
        });

        return rootView;
    }
}
