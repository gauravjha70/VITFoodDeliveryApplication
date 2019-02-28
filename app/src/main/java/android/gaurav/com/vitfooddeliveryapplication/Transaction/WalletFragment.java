package android.gaurav.com.vitfooddeliveryapplication.Transaction;

import android.gaurav.com.vitfooddeliveryapplication.R;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class WalletFragment extends Fragment {

    TextView walletBalance;
    Button addMoney;
    ListView transactionList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.wallet_fragments,container,false);

        walletBalance = rootView.findViewById(R.id.wallet_balance);
        addMoney = rootView.findViewById(R.id.add_money_button);
        transactionList = rootView.findViewById(R.id.transaction_list);


        addMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return rootView;
    }
}
