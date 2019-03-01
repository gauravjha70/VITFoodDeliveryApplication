package android.gaurav.com.vitfooddeliveryapplication.Orders;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.gaurav.com.vitfooddeliveryapplication.ConfirmOrderBottomFragment;
import android.gaurav.com.vitfooddeliveryapplication.OrdersClass;
import android.gaurav.com.vitfooddeliveryapplication.R;
import android.gaurav.com.vitfooddeliveryapplication.Transaction.TransactionClass;
import android.gaurav.com.vitfooddeliveryapplication.UserClass;
import android.graphics.Bitmap;
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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class PendingFragment extends Fragment {

    TextView service, address, time, price, message, name;
    TextView regNo, phNo;
    Button completeButton;
    OrdersClass ordersClass;
    ImageView profileImage;
    RelativeLayout detailSection;
    ProgressBar progressBar;
    ImageView qrCode;

    String orderType="", orderID = "";

    ConfirmOrderBottomFragment confirmOrderBottomFragment;

    FirebaseFirestore firebaseFirestore;
    FirebaseUser user;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pending_fragment,container,false);

        service = rootView.findViewById(R.id.service);
        address = rootView.findViewById(R.id.delivery_address);
        time = rootView.findViewById(R.id.time);
        price = rootView.findViewById(R.id.price);
        message = rootView.findViewById(R.id.message);
        name = rootView.findViewById(R.id.name);
        profileImage = rootView.findViewById(R.id.profile_img);
        detailSection = rootView.findViewById(R.id.details_section);
        progressBar = rootView.findViewById(R.id.progress_bar);
        regNo = rootView.findViewById(R.id.reg_number);
        phNo = rootView.findViewById(R.id.phone_number);
        qrCode = rootView.findViewById(R.id.qr_code);

        qrCode.setVisibility(View.GONE);

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
        generateQRCode();


        return rootView;
    }

    void getUserdetails(){
        firebaseFirestore.collection("USERS").whereEqualTo("email",ordersClass.getAcceptedByEmail())
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

    void generateQRCode()
    {

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(orderID, BarcodeFormat.QR_CODE,100,100);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrCode.setImageBitmap(bitmap);
            qrCode.setVisibility(View.VISIBLE);
        }catch(WriterException e) {
            e.printStackTrace();
        }


    }


}
