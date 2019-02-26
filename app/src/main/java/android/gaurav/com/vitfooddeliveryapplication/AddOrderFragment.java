package android.gaurav.com.vitfooddeliveryapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class AddOrderFragment extends Fragment {

    EditText service, time, price, message, address;
    Button continueButton;
    ConfirmOrderBottomFragment confirmOrderBottomFragment;

    public AddOrderFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_order_fragment,container,false);

        service = rootView.findViewById(R.id.service);
        time = rootView.findViewById(R.id.time);
        price = rootView.findViewById(R.id.price);
        message = rootView.findViewById(R.id.message);
        continueButton = rootView.findViewById(R.id.continue_button);
        address= rootView.findViewById(R.id.delivery_address);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("service",service.getText().toString());
                bundle.putString("time", time.getText().toString());
                bundle.putDouble("price",Double.parseDouble(price.getText().toString()));
                bundle.putString("message",message.getText().toString());
                bundle.putString("address",address.getText().toString());
                bundle.putString("orderType", "orderAdd");

                confirmOrderBottomFragment = new ConfirmOrderBottomFragment();
                confirmOrderBottomFragment.setArguments(bundle);
                confirmOrderBottomFragment.show(getChildFragmentManager(),confirmOrderBottomFragment.getTag());
            }
        });

        return rootView;
    }
}
