package android.gaurav.com.vitfooddeliveryapplication.Transaction;

import android.gaurav.com.vitfooddeliveryapplication.ProfileFragment;
import android.gaurav.com.vitfooddeliveryapplication.R;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class WalletFragment extends Fragment {

    TextView walletBalance;
    Button addMoney;
    ProgressBar progressBar;
    RelativeLayout fragmentContainer;
    ListView transactionList;
    ArrayList<TransactionClass> itemList;
    TransactionAdapter adapter;

    FirebaseFirestore firebaseFirestore;
    FirebaseUser user;

    FragmentManager fragmentManager;

    AddWalletBalanceFragment addWalletBalanceFragment;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.wallet_fragments,container,false);

        walletBalance = rootView.findViewById(R.id.wallet_balance);
        addMoney = rootView.findViewById(R.id.add_money_button);
        transactionList = rootView.findViewById(R.id.transaction_list);
        fragmentContainer = rootView.findViewById(R.id.fragment_container);
        progressBar = rootView.findViewById(R.id.progress_bar);

        firebaseFirestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        itemList = new ArrayList<TransactionClass>();
        adapter = new TransactionAdapter(getContext(),R.layout.transaction_adapter,itemList);

        transactionList.setAdapter(adapter);

        progressBar.setVisibility(View.VISIBLE);
        firebaseFirestore.collection("TRANSACTIONS").document(user.getEmail()).collection("transaction")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                progressBar.setVisibility(View.GONE);
                for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges())
                {
                    Log.e("names",doc.getDocument().getString("orderID"));
                    TransactionClass obj = doc.getDocument().toObject(TransactionClass.class);
                    itemList.add(obj);
                    Log.e("names",obj.getOrderID());

                }
                adapter = new TransactionAdapter(getContext(),R.layout.transaction_adapter,itemList);
                transactionList.setAdapter(adapter);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });


        addMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getChildFragmentManager();
                fragmentContainer.setClickable(true);

                addWalletBalanceFragment = new AddWalletBalanceFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_from_right);
                transaction.add(R.id.fragment_container,addWalletBalanceFragment).commit();
            }
        });

        return rootView;
    }
}
