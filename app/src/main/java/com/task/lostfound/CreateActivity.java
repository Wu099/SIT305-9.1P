package com.task.lostfound;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CreateActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private EditText name_edit,phone_edit,description_edit,date_edit,location_edit;
    private RadioGroup postTypeGroup;
    private String postType="Lost";
    private LostFoundSqlHelper  lostFoundSqlHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        // Initialize Places.
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyBWHm2XnfQor_R59lpmLMH4Ws2XWg7P458");
        }

        name_edit=findViewById(R.id.name_edit);
        phone_edit=findViewById(R.id.phone_edit);
        description_edit=findViewById(R.id.description_edit);
        date_edit=findViewById(R.id.date_edit);
        postTypeGroup=findViewById(R.id.postTypeGroup);
        postTypeGroup.setOnCheckedChangeListener(this);
        location_edit = findViewById(R.id.LocationEditText);

        setUpLocationET();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                location_edit.setText(place.getName());  // Set the selected place's name to the location_edit EditText
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(this, "Error: " + status.getStatusMessage(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                // User canceled the operation
                Toast.makeText(this, "Operation canceled", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void saveClick(View v){
        String name=name_edit.getText().toString().trim();
        String phone=phone_edit.getText().toString().trim();
        String description=description_edit.getText().toString().trim();
        String date=date_edit.getText().toString().trim();
        String curlocation = location_edit.getText().toString().trim();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String lat = "";
        String lng = "";
        try {
            List<Address> addresses = geocoder.getFromLocationName(curlocation, 1);
            if (addresses.size() > 0) {
                Address location = addresses.get(0);
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                lat = String.valueOf(latitude);
                lng = String.valueOf(longitude);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        LostFound lostFound=new LostFound(name,postType,phone,description,curlocation,date,lat,lng);
        long result = lostFoundSqlHelper.insert(lostFound);
        if(result!=-1){
            Toast.makeText(this,"ADD SUCCESS!",Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(this,"ADD Failed!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        RadioButton checkRadioButton = findViewById(radioGroup.getCheckedRadioButtonId());
        postType = checkRadioButton.getText().toString().trim();
    }

    @Override
    protected void onStart() {
        super.onStart();
        lostFoundSqlHelper = LostFoundSqlHelper.getInstance(this, 1);
        //打开数据库帮助器的写连接
        lostFoundSqlHelper.openWriteLink();
    }

    @Override
    protected void onStop() {
        super.onStop();
        lostFoundSqlHelper.closeLink();
    }

    private void setUpLocationET() {
        location_edit.setFocusable(false);
        location_edit.setOnClickListener(v -> {
            // Define the fields to specify which types of place data to return.
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

            // Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(this);
            startActivityForResult(intent, 1000);
        });
    }
}