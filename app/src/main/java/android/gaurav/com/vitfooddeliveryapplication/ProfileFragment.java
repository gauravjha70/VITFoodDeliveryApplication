package android.gaurav.com.vitfooddeliveryapplication;

import android.content.Intent;
import android.gaurav.com.vitfooddeliveryapplication.Login.LoginActivity;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

public class ProfileFragment extends Fragment {

    ImageButton signOutButton, editProfileButton;
    ProgressBar progressBar;
    TextView name, registrationNumber, mobileNumber, walletBalance;
    ListView orderList;
    RelativeLayout fragmentContainer;

    RequestListAdapter requestListAdapter;
    ArrayList<OrdersClass> items;
    ArrayList<String> orderID;

    FragmentManager fragmentManager;
    OrderDetailFragment orderDetailFragment;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_fragment,container,false);

        signOutButton = rootView.findViewById(R.id.sign_out_button);
        name = rootView.findViewById(R.id.name);
        registrationNumber = rootView.findViewById(R.id.reg_number);
        progressBar = rootView.findViewById(R.id.progress_bar);
        orderList = rootView.findViewById(R.id.order_list);
        editProfileButton = rootView.findViewById(R.id.edit_profile_button);
        mobileNumber = rootView.findViewById(R.id.mob_number);
        fragmentContainer = rootView.findViewById(R.id.fragment_container);
        walletBalance = rootView.findViewById(R.id.wallet_balance);

        progressBar.setVisibility(View.VISIBLE);
        name.setVisibility(View.GONE);
        registrationNumber.setVisibility(View.GONE);
        signOutButton.setVisibility(View.GONE);
        mobileNumber.setVisibility(View.GONE);
        editProfileButton.setVisibility(View.GONE);
        walletBalance.setVisibility(View.GONE);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        firebaseFirestore.collection("USERS").whereEqualTo("email",user.getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                progressBar.setVisibility(View.GONE);

                for(DocumentChange doc : task.getResult().getDocumentChanges())
                {
                    name.setText(doc.getDocument().getString("name"));
                    registrationNumber.setText(doc.getDocument().getString("regNo"));
                    Log.e("Registration",doc.getDocument().getString("regNo"));
                    mobileNumber.setText(doc.getDocument().getString("mobileNumber"));
                    Log.e("text",registrationNumber.getText().toString());
                    walletBalance.setText("Rs. " + doc.getDocument().getDouble("credits"));
                    break;
                }
                name.setVisibility(View.VISIBLE);
                registrationNumber.setVisibility(View.VISIBLE);
                signOutButton.setVisibility(View.VISIBLE);
                mobileNumber.setVisibility(View.VISIBLE);
                walletBalance.setVisibility(View.VISIBLE);
               // editProfileButton.setVisibility(View.VISIBLE);
                inflateOrderList();
            }
        });


        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        orderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Inflating order details fragment
                fragmentManager = getChildFragmentManager();
                fragmentContainer.setClickable(true);

                orderDetailFragment = new OrderDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("orderType","orderCancel");
                bundle.putString("orderID",orderID.get(position));
                bundle.putSerializable("object",items.get(position));
                orderDetailFragment.setArguments(bundle);

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_from_right);
                transaction.add(R.id.fragment_container,orderDetailFragment).commit();
            }
        });

        return rootView;
    }

    public void inflateOrderList()
    {
        //Initialising recycler list adapter
        items = new ArrayList<OrdersClass>();
        requestListAdapter = new RequestListAdapter(getContext(),R.layout.orders_list_adapter,items);
        orderList.setAdapter(requestListAdapter);


        firebaseFirestore.collection("REQUESTS").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {

                if(e!=null)
                {

                }
                else
                {
                    orderID = new ArrayList<String>();
                    for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges())
                    {
                        switch(doc.getType())
                        {
                            case ADDED: OrdersClass order = new OrdersClass();
                                if(user.getEmail().equals(doc.getDocument().getString("email")))
                                {
                                    order.setName(doc.getDocument().getString("service"));
                                    order.setDeliveryAddress(doc.getDocument().getString("deliveryAddress"));
                                    order.setDeliveryTimming(doc.getDocument().getString("deliveryTimming"));
                                    order.setTiming(doc.getDocument().getString("timing"));
                                    order.setOrderStatus(doc.getDocument().getString("status"));
                                    order.setPrice(doc.getDocument().getDouble("price"));
                                    order.setService(doc.getDocument().getString("orderStatus"));
                                    order.setEmail(doc.getDocument().getString("email"));
                                    orderID.add(doc.getDocument().getId());
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
    }

}
