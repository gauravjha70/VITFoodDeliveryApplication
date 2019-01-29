package android.gaurav.com.vitfooddeliveryapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerList;
    FloatingActionButton addButton;

    FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerList = findViewById(R.id.recycler_list);
        addButton = findViewById(R.id.add_button);

        //Firebase
        firebaseFirestore = FirebaseFirestore.getInstance();                 //Firestore Initialise

    }
}
