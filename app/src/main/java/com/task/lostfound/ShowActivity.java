package com.task.lostfound;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ShowActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private LostFoundSqlHelper  lostFoundSqlHelper;
    private ListView list_view;
    private List<LostFound> lostFoundList;
    private List<String> lostFoundStrList;
    private ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        list_view=findViewById(R.id.list_view);
        lostFoundList=new ArrayList<>();
        lostFoundStrList=new ArrayList<>();
        arrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,lostFoundStrList);
        list_view.setAdapter(arrayAdapter);
        list_view.setOnItemClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        lostFoundSqlHelper = LostFoundSqlHelper.getInstance(this, 1);
        //打开数据库帮助器的写连接
        lostFoundSqlHelper.openWriteLink();
        lostFoundList.clear();
        lostFoundStrList.clear();
        List<LostFound> tempLostFoundList= lostFoundSqlHelper.query("order by update_time desc");
        for (LostFound lostFound : tempLostFoundList) {
            String tempStr=String.format("%s:%s",lostFound.getPostType(),lostFound.getName());
            lostFoundStrList.add(tempStr);
            lostFoundList.add(lostFound);
        }
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        lostFoundSqlHelper.closeLink();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=new Intent(this,DetailActivity.class);
        Bundle bundle=new Bundle();
        LostFound lostFound = lostFoundList.get(i);
        bundle.putInt("id",lostFound.getId());
        bundle.putString("name",lostFound.getName());
        bundle.putString("location",lostFound.getLocation());
        bundle.putString("description",lostFound.getDescription());
        bundle.putString("phone",lostFound.getPhone());
        bundle.putString("date",lostFound.getDate());
        bundle.putString("post_type",lostFound.getPostType());
        intent.putExtras(bundle);
        startActivity(intent);
    }
}