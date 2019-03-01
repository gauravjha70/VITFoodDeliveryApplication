package android.gaurav.com.vitfooddeliveryapplication.Orders;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.gaurav.com.vitfooddeliveryapplication.ConfirmOrderBottomFragment;
import android.gaurav.com.vitfooddeliveryapplication.OrdersClass;
import android.gaurav.com.vitfooddeliveryapplication.R;
import android.gaurav.com.vitfooddeliveryapplication.Transaction.TransactionClass;
import android.gaurav.com.vitfooddeliveryapplication.UserClass;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import static android.app.Activity.RESULT_OK;

public class AcceptedFragment extends Fragment {

    TextView service, address, time, price, message, name;
    TextView regNo, phNo;
    Button completeButton;
    OrdersClass ordersClass;
    ImageView profileImage;
    RelativeLayout detailSection;
    ProgressBar progressBar;

    String orderType="", orderID = "";

    int reqCode = 100;

    FirebaseFirestore firebaseFirestore;
    FirebaseUser user;


    String scanResult = "";

    int cashback = 0;
    double totalAmount = 0;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.accepted_fragment,container,false);

        service = rootView.findViewById(R.id.service);
        address = rootView.findViewById(R.id.delivery_address);
        time = rootView.findViewById(R.id.time);
        price = rootView.findViewById(R.id.price);
        message = rootView.findViewById(R.id.message);
        name = rootView.findViewById(R.id.name);
        completeButton = rootView.findViewById(R.id.complete_button);
        profileImage = rootView.findViewById(R.id.profile_img);
        detailSection = rootView.findViewById(R.id.details_section);
        progressBar = rootView.findViewById(R.id.progress_bar);
        regNo = rootView.findViewById(R.id.reg_number);
        phNo = rootView.findViewById(R.id.phone_number);

        firebaseFirestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        Bundle bundle = getArguments();
        ordersClass = (OrdersClass) bundle.getSerializable("object");
        orderID = bundle.getString("ID");

        service.setText(ordersClass.getService());
        address.setText(ordersClass.getDeliveryAddress());
        time.setText(ordersClass.getTiming());
        price.setText("Rs. "+ordersClass.getPrice());
        message.setText(ordersClass.getMessage());
        name.setText(ordersClass.getName());

        getUserdetails();

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(),Scanner.class),reqCode);
            }
        });

        return rootView;
    }


    void getUserdetails(){
        firebaseFirestore.collection("USERS").whereEqualTo("email",ordersClass.getEmail())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges())
                {
                    UserClass userClass = doc.getDocument().toObject(UserClass.class);
                    name.setText(userClass.getName());
                    regNo.setText(userClass.getRegNo());
                    phNo.setText(userClass.getMobileNumber());

                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == reqCode && resultCode == RESULT_OK)
        {
            if(data!=null)
            {
                Barcode barcode = data.getParcelableExtra("barcode");
                scanResult = barcode.displayValue;
                if(orderID.equals(scanResult)) {
                    verifyOrder();
                }
                else
                {
                    new AlertDialog.Builder(getContext())
                            .setTitle("WRONG QR CODE")
                            .setMessage("You scanned an invalid QR Code")
                            .setIcon(R.drawable.wrong_red_cross)
                            .show();

                }

            }
        }
    }


    void verifyOrder(){
        progressBar.setVisibility(View.VISIBLE);
        ordersClass.setOrderStatus("Complete");
        firebaseFirestore.collection("REQUESTS").document(orderID)
                .set(ordersClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                addWalletBalance();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    void addWalletBalance()
    {
        cashback = new Random().nextInt(5);

        firebaseFirestore.collection("USERS")
                .whereEqualTo("email",user.getEmail()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(DocumentChange doc : task.getResult().getDocumentChanges())
                            {
                                HashMap<String,Object> updator = new HashMap<>();
                                double updatedWalletBalance = doc.getDocument().getDouble("credits") + ordersClass.getPrice() + cashback;
                                totalAmount = ordersClass.getPrice();
                                updator.put("credits",updatedWalletBalance);
                                Log.e("ID",doc.getDocument().getId()+"");
                                firebaseFirestore.collection("USERS").document(doc.getDocument().getId()+"").update(updator)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
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
                        }
                        else
                        {
                            Toast.makeText(getContext(),"Error while processing the payment",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });
    }

    void addTransactionDetails()
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY");
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mmaa");
        Date d = new Date();
        String date = simpleDateFormat.format(d);
        String time = simpleTimeFormat.format(d);

        TransactionClass transaction = new TransactionClass(totalAmount-cashback,"self",orderID,date,time,"Order Complete");

        firebaseFirestore.collection("TRANSACTIONS").document(user.getEmail()).collection("transaction")
                .add(transaction).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY");
                SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mmaa");
                Date d = new Date();
                String date = simpleDateFormat.format(d);
                String time = simpleTimeFormat.format(d);
                TransactionClass cashbackTransaction = new TransactionClass(0.0+ cashback,"self","cashback",date,time,"Cashback");
                firebaseFirestore.collection("TRANSACTIONS").document(user.getEmail()).collection("transaction")
                        .add(cashbackTransaction).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progressBar.setVisibility(View.GONE);
                        new AlertDialog.Builder(getContext())
                                .setTitle("ORDER COMPLETED")
                                .setMessage("An amount of Rs. "+(totalAmount - cashback)+" is added to your wallet with a cashback of Rs. "+cashback)
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
