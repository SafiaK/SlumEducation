package com.uog.umarkhan.slumeducation;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.uog.umarkhan.slumeducation.LocationDetails;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button b, b1, detailsBtn,volunteerBtn;
    private static final int SELECT_IMAGE = 1;
    private static final int Request_Camera_Code = 2;
    private StorageReference mStorage;
    private Dialog mAlertDialog;
    private Double longitude, latitude;
    private boolean checkGPS;

    private ProgressDialog mDialog;
    Uri downloadUri, ImageUri;
    ImageView iv;
    TextView ScrollTextView;
    EditText name, description, noOfChildren, FlagEtext, Loc,userPhone;
    String Id, Name, Description, ImageUrl, NoOfChildren,Location,UserPhone,volunteerName,volunteerPhone;
    Dialog questionDiolog;

    DatabaseReference databaseReference, TextViewDBReference;
    private LocationDetails mLocationDetails;
    private LocationDetails RetrieverObject;
    private Tracker tracker;
    ArrayList<LocationDetails> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleText();

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.WRITE_GSERVICES, android.Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CHANGE_NETWORK_STATE, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }


        tracker = new Tracker(MainActivity.this);


        mStorage = FirebaseStorage.getInstance().getReference();
        mDialog = new ProgressDialog(this);
        questionDiolog = new Dialog(this);


        //  FlagEtext=(EditText)findViewById(R.id.flagEText);

        databaseReference = FirebaseDatabase.getInstance().getReference("UserDetails");


        b = (Button) findViewById(R.id.button1);
        // b1=(Button)findViewById(R.id.button2) ;

        detailsBtn = (Button) findViewById(R.id.details);
        //  iv = (ImageView) findViewById(R.id.imageView1);

        volunteerBtn=(Button)findViewById(R.id.VolunteerButton);
        ScrollTextView = (TextView) findViewById(R.id.ScrolltextView);


        name = (EditText) findViewById(R.id.flagEText);
        description = (EditText) findViewById(R.id.DescriptionEditText);
        noOfChildren = (EditText) findViewById(R.id.ChildrenEditText);
        Loc = (EditText) findViewById(R.id.LocEditText);
        Loc.setEnabled(false);

        userPhone = (EditText) findViewById(R.id.editTextPhone);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkGPS = tracker.getLocation();
                if (checkGPS) {

                    Name = name.getText().toString();
                    Description = description.getText().toString();
                    NoOfChildren = noOfChildren.getText().toString();
                    UserPhone = userPhone.getText().toString();


                    if (Name.isEmpty() || Description.isEmpty() || NoOfChildren.isEmpty() || UserPhone.isEmpty()) {

                        Toast.makeText(getApplicationContext(), "Please Fill All Fields", Toast.LENGTH_LONG).show();

                    } else {
                        if (NoOfChildren.matches((".*\\d+.*"))) {


                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, Request_Camera_Code);


                        } else {
                            Toast.makeText(getApplicationContext(), "Please Enter Numeric Values in No. of Children", Toast.LENGTH_LONG).show();

                        }
                    }

                }

            }
        });


        //Retrieving firebase Values


        detailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                checkGPS = tracker.getLocation();
                //  Toast.makeText(getApplicationContext(),tracker.getLatitude()+" "+tracker.getLongitude(),Toast.LENGTH_LONG).show();
                if (checkGPS) {
                    RetrievingValues();
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);

                    intent.putExtra("Key", list);

                    intent.putExtra("flag", name.getText().toString());
                    startActivity(intent);


                    //Getting Address of the location
                    String loc = getLocationString();
                    Loc.setText(loc);

                }

            }
        });

        volunteerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Id!=null) {
                    final AlertDialog builder = new AlertDialog.Builder(MainActivity.this).create();
                    builder.setTitle("Do You Know Anyone?");
                    builder.setMessage("If You Know any Person Working Here for Slum Education Please Provide Us His/Her Name and Contact Number. \n\nDo You Know Anyone in the Area? \n Press \"Yes\" to Proceed. \n Press \"No\" to Continue Entering Details.");
                    builder.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent1 = new Intent(MainActivity.this, UserDetailActivity.class);
                            startActivityForResult(intent1, 321);
                            builder.dismiss();

                        }
                    });
                    builder.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            builder.dismiss();
                        }
                    });
                    builder.show();

                }

                else
                {
                    Toast.makeText(getApplicationContext(),"First give your Details",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    //onActivityResult for showing dialogbox to enter details from user

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==321 && resultCode==RESULT_OK)
        {
           volunteerName=data.getStringExtra("name");
            volunteerPhone=data.getStringExtra("phone");

            mLocationDetails.setVolunteerName(volunteerName);
            mLocationDetails.setVolunteerPhone(volunteerPhone);

            databaseReference.child(Id).setValue(mLocationDetails);

            Toast.makeText(getApplicationContext(),"Volunteer Added Sucessfully",Toast.LENGTH_SHORT).show();


        }


        if (requestCode == Request_Camera_Code && resultCode == RESULT_OK) {

            // ImageUri=data.getData();

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ImageUri = getImageUri(getApplicationContext(), bitmap);

            if (ImageUri == null) {
                Toast.makeText(getApplicationContext(), "data is empty", Toast.LENGTH_LONG).show();
            } else {
                Uri uri = ImageUri;
                uploadImage(uri);
            }
        }
        // onCreateDialog();

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);


        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void uploadImage(Uri imageUri) {
        mDialog.setMessage("Uploading Image...");
        mDialog.show();

        Uri uri = imageUri;


        if (uri != null) {
            latitude=tracker.getLatitude();
            if(latitude!=0 ) {
                StorageReference filepath = mStorage.child("Photos").child(uri.getLastPathSegment());
                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Toast.makeText(getApplicationContext(), "Image Uploaded Successful", Toast.LENGTH_LONG).show();

                        if (mDialog != null || mDialog.isShowing()) {
                            mDialog.dismiss();
                        }

                        downloadUri = taskSnapshot.getDownloadUrl();
                        String imageUrl = String.valueOf(downloadUri);
                        // Toast.makeText(getApplicationContext(),abc,Toast.LENGTH_LONG).show();
                        // onCreateDialog(imageUrl);


                        Name = name.getText().toString();
                        Description = description.getText().toString();
                        NoOfChildren = noOfChildren.getText().toString();
                       Location=getLocationString();
                        Loc.setText(Location);
                        UserPhone=userPhone.getText().toString();


                        longitude = tracker.getLongitude();
                        latitude = tracker.getLatitude();

                        Id = databaseReference.push().getKey();
                        mLocationDetails = new LocationDetails(Id, Name,Location, Description, imageUrl, NoOfChildren,UserPhone,volunteerName,volunteerPhone);

                        mLocationDetails.setLatitude(latitude);
                        mLocationDetails.setLongitude(longitude);

                        databaseReference.child(Id).setValue(mLocationDetails);

                        Toast.makeText(getApplicationContext(), "Details Added Successfully", Toast.LENGTH_LONG).show();

                        name.setText(null);
                        description.setText(null);
                        noOfChildren.setText(null);
                        userPhone.setText(null);
                        Loc.setText(null);
                        name.requestFocus();


                        //  Picasso.with(MainActivity.this).load(downloadUri).fit().centerCrop().into(iv);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Image Upload Not Successful Check Internet Connection", Toast.LENGTH_LONG).show();
                        if (mDialog != null || mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                    }
                });
            }
            else{Toast.makeText(getApplicationContext(),"Getting null longitude & Latitude... Please Try Again",Toast.LENGTH_LONG).show();}
        } else {
            Toast.makeText(getApplicationContext(), "ImageURRRIIII is empty", Toast.LENGTH_LONG).show();
            if (mDialog != null || mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }
    }

    public void RetrievingValues() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = "";

                for (DataSnapshot LocationData : dataSnapshot.getChildren()) {
                    LocationDetails ld = LocationData.getValue(LocationDetails.class);
                    list.add(ld);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public  void titleText(){
        TextViewDBReference=FirebaseDatabase.getInstance().getReference("TextTable");
      // TextTable tb=new TextTable("ABC");
      //  TextViewDBReference.setValue(tb);

        TextViewDBReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String abc="";
                for( DataSnapshot TitleDatasnapShot : dataSnapshot.getChildren()) {
                 abc=  TitleDatasnapShot.getValue(String.class);
                }

                ScrollTextView.setText(abc);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    public String getLocationString(){

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        String abc="";
        try {
            addresses = geocoder.getFromLocation(tracker.getLatitude(), tracker.getLongitude(), 1); // Here 1 represent max location result
            // to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            //  String state = addresses.get(0).getAddressLine(0);
            String country = addresses.get(0).getCountryName();

            //  Toast.makeText(getApplicationContext(),address+" "+city+" "+country,Toast.LENGTH_LONG).show();
            abc = address + " " + city + " " + country;

          //  Loc.setText(abc);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return abc;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
      //  tracker.stopUsingGPS();
    }
}
