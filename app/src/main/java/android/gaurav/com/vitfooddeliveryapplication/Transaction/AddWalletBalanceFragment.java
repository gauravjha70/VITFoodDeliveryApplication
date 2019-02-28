package android.gaurav.com.vitfooddeliveryapplication.Transaction;

import android.content.DialogInterface;
import android.gaurav.com.vitfooddeliveryapplication.R;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddWalletBalanceFragment extends Fragment {

    EditText amount, cardNo, name, cvv;
    Button payNowButton, addAmountButton;
    LinearLayout cardDetails;
    ProgressBar progressBar;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_wallet_balance_fragment,container,false);

        amount = rootView.findViewById(R.id.amount);
        cardNo = rootView.findViewById(R.id.card_number);
        name = rootView.findViewById(R.id.card_holder_name);
        cvv = rootView.findViewById(R.id.cvv);
        payNowButton = rootView.findViewById(R.id.pay_button);
        cardDetails = rootView.findViewById(R.id.card_details);
        addAmountButton = rootView.findViewById(R.id.add_amount_button);
        progressBar = rootView.findViewById(R.id.progress_bar);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        user = firebaseAuth.getCurrentUser();

        addAmountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount.getText().toString().isEmpty()) {
                    amount.setError("Enter an amount");
                    amount.requestFocus();
                    return;
                }
                amount.setEnabled(false);
                addAmountButton.setVisibility(View.GONE);
                cardDetails.setVisibility(View.VISIBLE);
            }
        });

        payNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment();
            }
        });

        return rootView;
    }

    private void payment()
    {
        if (cardNo.getText().toString().length()!=16) {
            cardNo.setError("Enter a valid Card Number");
            cardNo.requestFocus();
            return;
        }

        if (name.getText().toString().isEmpty()) {
            name.setError("Enter the card holder name");
            name.requestFocus();
            return;
        }

        if (cardNo.getText().toString().length()!=3) {
            cardNo.setError("Enter a valid CVV");
            cardNo.requestFocus();
            return;
        }

        payNowButton.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        firebaseFirestore.collection("CARDS").document(cardNo.getText().toString())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        String n = documentSnapshot.getString("name");
                        String c = documentSnapshot.getString("cvv");

                        if(name.getText().toString().equals(n) && cvv.getText().toString().equals(c))
                        {
                            addTransactionDetails();
                        }
                    }
                });


    }

    public void addTransactionDetails()
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY");
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mmaa");
        Date d = new Date();
        String date = simpleDateFormat.format(d);
        String time = simpleTimeFormat.format(d);

        TransactionClass transaction = new TransactionClass(Double.parseDouble(amount.getText().toString()),"self","",date,time,"Wallet");

        firebaseFirestore.collection("TRANSACTIONS").document(user.getEmail()).collection("transaction")
                .add(transaction).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                progressBar.setVisibility(View.GONE);
                new AlertDialog.Builder(getContext())
                        .setTitle("PAYMENT SUCCESSFUL")
                        .setMessage("An amount of Rs. "+amount.getText().toString()+" is added to your wallet")
                        .setIcon(R.drawable.correct_tick_green)
                        .show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),"Error while processing the payment",Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}
