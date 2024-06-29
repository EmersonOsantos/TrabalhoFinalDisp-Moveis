package com.emerson.trabfinal.Contacts;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.emerson.trabfinal.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

public class Detail_Contact_Activity extends AppCompatActivity implements OnMapReadyCallback {
    ImageView Image_Contact_D;
    TextView Id_Contact_D, Uid_User_D, Name_Contact_D, Lastname_Contact_D, Mail_Contact_D, Age_Contact_D, Phone_Contact_D, Home_Contact_D;

    String id_c, uid_user, name_c, lastname_c, mail_c, age_c, phone_c, home_c;
    Button Call_C, Message_C,Endereco_C;
    private GoogleMap mMap;

    //private double lati = -34, longi = 151;private String titulo = "SYDNEY";
    private double lati = -20.50232, longi = -54.61349;
    private String titulo = "FACOM";
    Address address;
    private ResourceBundle bundle;

    private Detail_Contact_Activity_binding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contact);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Detalhe do contato");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        InitVariables();
        RecoverDataContact();
        SetDataContact();
        GetImageContact();

        Call_C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(Detail_Contact_Activity.this,
                        Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    CallContact();
                }else{
                    CallPermissionRequest.launch(Manifest.permission.CALL_PHONE);
                }
            }
        });

        Message_C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(Detail_Contact_Activity.this,
                        Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    SendMessage();
                }else{
                    MessagePermissionRequest.launch(Manifest.permission.SEND_SMS);
                }
            }
        });
    }

    private void InitVariables() {
        Image_Contact_D = findViewById(R.id.Image_Contact_D);

        Name_Contact_D = findViewById(R.id.Name_Contact_D);

        Mail_Contact_D = findViewById(R.id.Mail_Contact_D);

        Phone_Contact_D = findViewById(R.id.Phone_Contact_D);
        Home_Contact_D = findViewById(R.id.Home_Contact_D);

        Call_C = findViewById(R.id.Call_C);
        Message_C = findViewById(R.id.Message_C);
    }

    private void RecoverDataContact(){
        Bundle bundle = getIntent().getExtras();

        assert bundle != null;
        id_c = bundle.getString("id_c");
        uid_user = bundle.getString("uid_user");
        name_c = bundle.getString("name_c");
        lastname_c = bundle.getString("lastname_c");
        mail_c = bundle.getString("mail_c");
        phone_c = bundle.getString("phone_c");
        age_c = bundle.getString("age_c");
        home_c = bundle.getString("home_c");
        enderecoMaps();
    }

    private void SetDataContact() {

        Name_Contact_D.setText(name_c);

        Mail_Contact_D.setText(mail_c);
        Phone_Contact_D.setText(phone_c);

        Home_Contact_D.setText(home_c);
    }

    private void GetImageContact(){
        String image = getIntent().getStringExtra("image_c");

        try {

            Glide.with(getApplicationContext()).load(image).placeholder(R.drawable.profile).into(Image_Contact_D);

        }catch (Exception e){
            Toast.makeText(this, "Waiting Image", Toast.LENGTH_SHORT).show();

        }
    }

    private void CallContact() {
        String phone = Phone_Contact_D.getText().toString();
        if (!phone.equals("")){
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:"+phone));
            startActivity(intent);
        }else {
            Toast.makeText(this, "não há telefone para ligar", Toast.LENGTH_SHORT).show();
        }
    }

    private void SendMessage(){
        String phone = Phone_Contact_D.getText().toString();
        if (!phone.equals("")){
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("smsto:"+phone));
            intent.putExtra("sms_body", "");
            startActivity(intent);
        }else {
            Toast.makeText(this, "não há telefone para sms", Toast.LENGTH_SHORT).show();
        }
    }

    private ActivityResultLauncher<String> CallPermissionRequest =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
                if (isGranted){
                    CallContact();
                }else {
                    Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show();
                }
            });

    private ActivityResultLauncher<String> MessagePermissionRequest =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
                if (isGranted){
                    SendMessage();
                }else{
                    Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void enderecoMaps() {
        Geocoder geocoder=new Geocoder(getApplicationContext());
        List<Address> list;
        String endereco=home_c;
        try {
            list= geocoder.getFromLocationName(endereco,1);
            if(list==null){
                return;
            }
            Address address1=list.get(0);
            lati=address1.getLatitude();
            longi=address1.getLongitude();
            titulo=name_c;


        } catch (IOException e) {
            double lati = -20.50232, longi = -54.61349;
            String titulo = "FACOM";

        }
    }


    @Override
    public void onMapReady( GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(lati, longi);
        mMap.addMarker(new MarkerOptions().position(latLng).title(titulo));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,
                15));

    }
}