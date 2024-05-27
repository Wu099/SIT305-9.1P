package com.task.lostfound;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {

    private TextView title_text,description_text,date_text,location_text,phone_text;
    private Integer id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        title_text=findViewById(R.id.title_text);
        description_text=findViewById(R.id.description_text);
        date_text=findViewById(R.id.date_text);
        location_text=findViewById(R.id.location_text);
        phone_text=findViewById(R.id.phone_text);

        Bundle extras = getIntent().getExtras();
        id = extras.getInt("id");
        String name = extras.getString("name");
        String post_type = extras.getString("post_type");
        title_text.setText(String.format("%s:%s",post_type,name));
        description_text.setText("Description:"+extras.getString("description"));
        date_text.setText("Date:"+extras.getString("date"));
        phone_text.setText("Contact:"+extras.getString("phone"));
        location_text.setText("Location:"+extras.getString("location"));
    }
    public void removeClick(View v){
        LostFoundSqlHelper  lostFoundSqlHelper = LostFoundSqlHelper.getInstance(this, 1);
        //打开数据库帮助器的写连接
        lostFoundSqlHelper.openWriteLink();
        lostFoundSqlHelper.delete("_id=?",String.valueOf(id));
        lostFoundSqlHelper.closeLink();
        Toast.makeText(this,"REMOVE SUCCESS!",Toast.LENGTH_SHORT).show();
        finish();
    }
}