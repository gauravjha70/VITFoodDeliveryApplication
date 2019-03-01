package android.gaurav.com.vitfooddeliveryapplication.Orders;

import android.gaurav.com.vitfooddeliveryapplication.OrderDetailFragment;
import android.gaurav.com.vitfooddeliveryapplication.OrdersClass;
import android.gaurav.com.vitfooddeliveryapplication.R;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.StructuredQuery;

import java.util.ArrayList;

public class OrdersActivity extends AppCompatActivity {

    ListView pendingList, acceptedList;

    ArrayList<OrdersClass> pending,accepted;

    FirebaseFirestore firebaseFirestore;
    FirebaseUser user;

    FragmentManager fragmentManager;

    RelativeLayout fragmentContainer;

    AcceptedFragment acceptedFragment;
    PendingFragment pendingFragment;

    Boolean acceptedFrag,pendingFrag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        acceptedList = findViewById(R.id.accepted_orders);
        pendingList = findViewById(R.id.pending_order_list);
        fragmentContainer = findViewById(R.id.fragment_container);

        acceptedFrag = pendingFrag = false;

        firebaseFirestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        accepted = new ArrayList<OrdersClass>();
        pending = new ArrayList<OrdersClass>();

        updateLists();

        acceptedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Inflating order details fragment
                fragmentManager = getSupportFragmentManager();
                fragmentContainer.setClickable(true);

                acceptedFragment = new AcceptedFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("object",accepted.get(position));
                acceptedFragment.setArguments(bundle);

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_from_right);
                transaction.add(R.id.fragment_container,acceptedFragment).commit();
                acceptedFrag = true;
            }
        });

        pendingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Inflating order details fragment
                fragmentManager = getSupportFragmentManager();
                fragmentContainer.setClickable(true);

                pendingFragment = new PendingFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("object",pending.get(position));
                pendingFragment.setArguments(bundle);

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_from_right);
                transaction.add(R.id.fragment_container,pendingFragment).commit();
                pendingFrag = true;
            }
        });

    }

    private void updateLists()
    {
        firebaseFirestore.collection("REQUESTS").whereEqualTo("acceptedByEmail",user.getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(DocumentChange doc : task.getResult().getDocumentChanges())
                    {
                        OrdersClass obj = doc.getDocument().toObject(OrdersClass.class);
                        accepted.add(obj);
                    }
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        firebaseFirestore.collection("REQUESTS").whereEqualTo("email",user.getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(DocumentChange doc : task.getResult().getDocumentChanges())
                    {
                        OrdersClass obj = doc.getDocument().toObject(OrdersClass.class);
                        if(obj.getOrderStatus().equals("Accepted")) {
                            accepted.add(obj);
                        }
                    }

                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    @Override
    public void onBackPressed() {
        if(acceptedFrag)
        {
            //Removing Order add fragment to Main Activity
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_from_right);
            transaction.remove(acceptedFragment).commit();
            updateLists();
            fragmentContainer.setClickable(false);
            acceptedFrag = false;
        }
        else if(pendingFrag)
        {
            //Removing Order add fragment to Main Activity
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_from_right);
            transaction.remove(pendingFragment).commit();
            updateLists();
            fragmentContainer.setClickable(false);
            pendingFrag = false;
        }
        else
            super.onBackPressed();
    }
}
