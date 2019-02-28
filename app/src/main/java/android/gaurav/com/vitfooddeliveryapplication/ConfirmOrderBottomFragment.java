package android.gaurav.com.vitfooddeliveryapplication;

import android.gaurav.com.vitfooddeliveryapplication.Transaction.TransactionClass;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ConfirmOrderBottomFragment extends BottomSheetDialogFragment {

    TextView totalAmount, price, time , walletBalance, walletBalanceText;
    String  timeS, service, message, address, name="";
    Double priceS, updatedWalletBalance;
    String orderType = "", orderID = "";

    Button payButton;
    ProgressBar progressBar;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    String email_user;

    OrdersClass ordersClass;

    public ConfirmOrderBottomFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.confirm_order_fragment,container,false);

        totalAmount = rootView.findViewById(R.id.total_amount);
        price = rootView.findViewById(R.id.price);
        time = rootView.findViewById(R.id.time);
        walletBalance = rootView.findViewById(R.id.wallet_balance);
        payButton = rootView.findViewById(R.id.pay_button);
        progressBar = rootView.findViewById(R.id.progress_bar);
        walletBalanceText = rootView.findViewById(R.id.wallet_balance_text);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        user = firebaseAuth.getCurrentUser();
        email_user = user.getEmail();

        priceS = getArguments().getDouble("price") + 5;
        timeS = getArguments().getString("time");
        service = getArguments().getString("service");
        message = getArguments().getString("message");
        address = getArguments().getString("address");
        orderType = getArguments().getString("orderType","");

        progressBar.setVisibility(View.VISIBLE);
        firebaseFirestore.collection("USERS")
                .whereEqualTo("email",email_user).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(DocumentChange doc : task.getResult().getDocumentChanges())
                            {
                                walletBalance.setText("Rs. " + doc.getDocument().getDouble("credits"));
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                        else
                        {
                            Toast.makeText(getContext(),"Error while processing the payment",Toast.LENGTH_LONG).show();
                        }

                    }
                });


        switch(orderType)
        {
            case "orderAccept" : name = getArguments().getString("name");
                                //Inflating the values to the field
                                price.setText("Rs. "+(priceS-5));
                                time.setText(timeS);        //Inflating the values to the field
                                payButton.setText("ACCEPT ORDER");
                                walletBalance.setVisibility(View.GONE);
                                walletBalanceText.setVisibility(View.GONE);
                                break;
            case "orderAdd": //Inflating the values to the field
                                price.setText("Rs. "+priceS);
                                time.setText(timeS);        //Inflating the values to the field
                                break;


            case "orderCancel": orderID = getArguments().getString("orderID");
                                //Inflating the values to the field
                                totalAmount.setText("CANCEL ORDER");
                                priceS = ((OrdersClass)getArguments().getSerializable("object")).getPrice() + 5;
                                price.setText("Rs. "+priceS);
                                time.setText(timeS);        //Inflating the values to the field

                                payButton.setText("CANCEL ORDER");
                                walletBalance.setVisibility(View.GONE);
                                walletBalanceText.setText("Are you sure you wan't to cancel the order?");

                                String orderStatus = ((OrdersClass)getArguments().getSerializable("object")).getService();
                                if(orderStatus.equals("Cancelled"))
                                {
                                    payButton.setVisibility(View.GONE);
                                }
                                break;
        }


        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(orderType.equals("orderAccept"))
                {

                }
                else if(orderType.equals("orderAdd"))
                {
                    processPayment();
                }
                else if(orderType.equals("orderCancel"))
                {
                    updateRequestList();
                }

            }
        });

        return rootView;
    }

    private void processPayment()
    {
        progressBar.setVisibility(View.VISIBLE);
        firebaseFirestore.collection("USERS")
                .whereEqualTo("email",email_user).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(DocumentChange doc : task.getResult().getDocumentChanges())
                            {
                                HashMap<String,Object> updator = new HashMap<>();
                                name = doc.getDocument().getString("name");
                                updatedWalletBalance = doc.getDocument().getDouble("credits") - priceS;
                                updator.put("credits",updatedWalletBalance);
                                Log.e("ID",doc.getDocument().getId()+"");
                                firebaseFirestore.collection("USERS").document(doc.getDocument().getId()+"").update(updator)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                addRequest();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(),"Error while processing the payment",Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        }
                        else
                        {
                            Toast.makeText(getContext(),"Error while processing the payment",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });
    }

    public void addRequest()
    {
        ordersClass = new OrdersClass();
        ordersClass.setService(service);
        ordersClass.setPrice(priceS - 5);
        ordersClass.setTiming(timeS);
        ordersClass.setEmail(user.getEmail());
        ordersClass.setDeliveryTimming("");
        ordersClass.setOrderStatus("Pending");
        ordersClass.setDeliveryTimming("");
        ordersClass.setDeliveryAddress(address);
        ordersClass.setName(name);
        ordersClass.setMessage(message);

        firebaseFirestore.collection("REQUESTS")
                .add(ordersClass).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                orderID = documentReference.getId();
                addTransactionDetails();
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

    public void addTransactionDetails()
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY");
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mmaa");
        Date d = new Date();
        String date = simpleDateFormat.format(d);
        String time = simpleTimeFormat.format(d);

        TransactionClass transaction = new TransactionClass(priceS,"deliVerIT",orderID,date,time,service);

        firebaseFirestore.collection("TRANSACTIONS").document(user.getEmail()).collection("transaction")
                .add(transaction).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                progressBar.setVisibility(View.GONE);
                totalAmount.setText("Order Successful");
                payButton.setVisibility(View.GONE);
                walletBalance.setText(updatedWalletBalance+"");
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


    //Order Cancellation
    public  void updateRequestList() {
        progressBar.setVisibility(View.VISIBLE);

        HashMap<String, Object> updator = new HashMap<>();
        updator.put("orderStatus", "Cancelled");
        firebaseFirestore.collection("REQUESTS").document(orderID).update(updator)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        updateBalanceAfterCancellation();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error while processing the payment", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    public void updateBalanceAfterCancellation()
    {
        firebaseFirestore.collection("USERS")
                .whereEqualTo("email",email_user).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(DocumentChange doc : task.getResult().getDocumentChanges())
                            {
                                HashMap<String,Object> updator = new HashMap<>();
                                name = doc.getDocument().getString("name");
                                updatedWalletBalance = doc.getDocument().getDouble("credits") + priceS;
                                updator.put("credits",updatedWalletBalance);
                                Log.e("ID",doc.getDocument().getId()+"");
                                firebaseFirestore.collection("USERS").document(doc.getDocument().getId()+"").update(updator)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                totalAmount.setText("Order Canceled");
                                                progressBar.setVisibility(View.GONE);
                                                payButton.setVisibility(View.GONE);
                                                walletBalanceText.setText("Your oder is cancelled successfully!");
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
                        else
                        {
                            Toast.makeText(getContext(),"Error while processing the payment",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });
    }

}
