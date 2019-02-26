package android.gaurav.com.vitfooddeliveryapplication;

import android.gaurav.com.vitfooddeliveryapplication.Login.SignUpFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    ListView requestList;
    FloatingActionButton addButton, profileButton;
    RelativeLayout fragmentContainer;

    AddOrderFragment addOrderFragment;
    OrderDetailFragment orderDetailFragment;
    ProfileFragment profileFragment;
    Boolean addOrderFrag, orderDetailFrag, profileFrag;

    RequestListAdapter requestListAdapter;
    ArrayList<OrdersClass> items;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    FragmentManager fragmentManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestList = findViewById(R.id.request_list);
        addButton = findViewById(R.id.add_button);
        fragmentContainer = findViewById(R.id.fragment_container);
        profileButton = findViewById(R.id.profile_button);

        //Initialising flag for fragments
        addOrderFrag = orderDetailFrag = profileFrag = false;

        //Firebase
        firebaseFirestore = FirebaseFirestore.getInstance();                 //Firestore Initialise
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        //Initialising recycler list adapter
        items = new ArrayList<OrdersClass>();
        requestListAdapter = new RequestListAdapter(getApplicationContext(),R.layout.orders_list_adapter,items);
        requestList.setAdapter(requestListAdapter);


        firebaseFirestore.collection("REQUESTS").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if(e!=null)
                {

                }
                else
                {
                    for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges())
                    {
                        switch(doc.getType())
                        {
                            case ADDED: OrdersClass order = new OrdersClass();
                                        if((!user.getEmail().equals(doc.getDocument().getString("email"))) && doc.getDocument().getString("orderStatus").equals("Pending"))
                                        {
                                            order.setName(doc.getDocument().getString("name"));
                                            order.setDeliveryAddress(doc.getDocument().getString("deliveryAddress"));
                                            order.setDeliveryTimming(doc.getDocument().getString("deliveryTimming"));
                                            order.setTiming(doc.getDocument().getString("timing"));
                                            order.setOrderStatus(doc.getDocument().getString("status"));
                                            order.setPrice(doc.getDocument().getDouble("price"));
                                            order.setService(doc.getDocument().getString("service"));
                                            order.setEmail(doc.getDocument().getString("email"));
                                            order.setMessage(doc.getDocument().getString("message"));
                                            requestListAdapter.add(order);
                                        }

                                        break;
                            case REMOVED:
                            case MODIFIED:
                        }
                    }
                }

            }
        });

        Log.e("Error",items.size()+"");

       requestList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               //Inflating order details fragment
               fragmentManager = getSupportFragmentManager();
               fragmentContainer.setClickable(true);

               profileButton.hide();
               addButton.hide();

               orderDetailFragment = new OrderDetailFragment();
               Bundle bundle = new Bundle();
               bundle.putSerializable("object",items.get(position));
               orderDetailFragment.setArguments(bundle);

               FragmentTransaction transaction = fragmentManager.beginTransaction();
               transaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_from_right);
               transaction.add(R.id.fragment_container,orderDetailFragment).commit();
               orderDetailFrag = true;
           }
       });


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getSupportFragmentManager();
                fragmentContainer.setClickable(true);

                profileButton.hide();
                addButton.hide();

                addOrderFragment = new AddOrderFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_from_right);
                transaction.add(R.id.fragment_container,addOrderFragment).commit();
                addOrderFrag = true;
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getSupportFragmentManager();
                fragmentContainer.setClickable(true);

                profileButton.hide();
                addButton.hide();

                profileFragment = new ProfileFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_from_right);
                transaction.add(R.id.fragment_container,profileFragment).commit();
                profileFrag = true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(addOrderFrag)
        {
            //Removing Order add fragment to Main Activity
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_from_right);
            transaction.remove(addOrderFragment).commit();
            fragmentContainer.setClickable(false);
            addOrderFrag = false;
        }
        else if(orderDetailFrag)
        {
            //Removing Order Detail fragment to Main Activity
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_from_right);
            transaction.remove(orderDetailFragment).commit();
            fragmentContainer.setClickable(false);
            orderDetailFrag = false;
        }
        else if(profileFrag){
            //Removing Profile fragment to Main Activity
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_from_right);
            transaction.remove(profileFragment).commit();
            fragmentContainer.setClickable(false);
            profileFrag = false;
        }
        else
            super.onBackPressed();

        profileButton.show();
        addButton.show();
    }
}
