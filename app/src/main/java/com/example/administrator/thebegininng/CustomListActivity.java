package com.example.administrator.thebegininng;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class CustomListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ArrayList<ListData> listDataArray = new ArrayList<>();

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("TEST", position + "번 리스트 선택됨");
        Log.i("TEST", "리스트 내용" + listDataArray.get(position).getText1());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list);

        ListData data1 = new ListData("1-1", "1-2", "01.jpg");
        listDataArray.add(data1);

        ListData data2 = new ListData("2-1", "2-2", "02.jpg");
        listDataArray.add(data2);

        ListData data3 = new ListData("3-1", "3-2", "03.jpg");
        listDataArray.add(data3);

        ListData data4 = new ListData("4-1", "4-2", "04.jpg");
        listDataArray.add(data4);

        ListData data5 = new ListData("5-1", "5-2", "05.jpg");
        listDataArray.add(data5);

        ListView listView = (ListView)findViewById(R.id.custom_list_listView);
        Custom2Adapter custom2Adapter = new Custom2Adapter(this, R.layout.custom_list_row, listDataArray);
        listView.setAdapter(custom2Adapter);
        listView.setOnItemClickListener(this);

    }
}
