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
import android.widget.ProgressBar;
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
    ListView transactionList;
    ArrayList<TransactionClass> itemList;
    TransactionAdapter adapter;

    FirebaseFirestore firebaseFirestore;
    FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.wallet_fragments,container,false);

        walletBalance = rootView.findViewById(R.id.wallet_balance);
        addMoney = rootView.findViewById(R.id.add_money_button);
        transactionList = rootView.findViewById(R.id.transaction_list);

        firebaseFirestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        itemList = new ArrayList<TransactionClass>();
        adapter = new TransactionAdapter(getContext(),R.layout.transaction_adapter,itemList);

        transactionList.setAdapter(adapter);

        progressBar.setVisibility(View.VISIBLE);
        firebaseFirestore.collection("TRANSACTION").document(user.getEmail()).collection("transaction")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                progressBar.setVisibility(View.GONE);
                for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges())
                {
                    TransactionClass obj = doc.getDocument().toObject(TransactionClass.class);
                    itemList.add(obj);
                }
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

            }
        });

        return rootView;
    }
}
