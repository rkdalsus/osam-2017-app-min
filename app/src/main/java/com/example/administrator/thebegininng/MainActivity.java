package com.example.administrator.thebegininng;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.LocationServices;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity implements OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private Button button1;
    private Button button2;
    private Button button3;
    private Button mButtonWrite;
    private Button mButtonRefresh;
    private ListView mainListView;
    private ArrayList<Article> articleList;
    private GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
            .enableAutoManage(this /* FragmentActivity */,
                    this /* OnConnectionFailedListener */)
            .addApi(Drive.API)
            .addScope(Drive.SCOPE_FILE)
            .setAccountName("users.account.name@gmail.com")
            .build();

    private Dao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Dao dao = new Dao(getApplicationContext());

        String testJsonData = dao.getJsonTestData();

        dao.insertJsonData( testJsonData );*/

        mButtonWrite = (Button) findViewById(R.id.main_button_write);
        mButtonWrite.setOnClickListener(this);

        mButtonRefresh = (Button) findViewById(R.id.main_button_refresh);
        mButtonRefresh.setOnClickListener(this);

        button1 = (Button) findViewById(R.id.main_button_simple_list1);
        button1.setOnClickListener(this);

        button2 = (Button) findViewById(R.id.main_button_simple_list2);
        button2.setOnClickListener(this);

        button3 = (Button) findViewById(R.id.main_button_custom_list);
        button3.setOnClickListener(this);

        mainListView = (ListView) findViewById(R.id.main_listVIew);

    }

    public void initGAC() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            onStart();
        }
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitudeText.setTexT(String.valueOf(mLastLocation.getLatitude()));
            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResume() {
        super.onResume();

        refreshData();
    }
    private void listView() {

        //DB로부터 게시글 리스트를 받아옴
        articleList = dao.getArticleList();

        //CustomAdapter를 적용함
        CustomAdapter customAdapter = new CustomAdapter(this, R.layout.custom_list_row, articleList);
        mainListView.setAdapter(customAdapter);
    }

    private static AsyncHttpClient client = new AsyncHttpClient();

    private void refreshData() {

        client.get("http://10.53.128.116:5009/loadData", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //DB에 JSON데이터 저장
                String jsonData = new String(responseBody);
                Log.i("test", "jsonData: " + jsonData);

                dao = new Dao(getApplicationContext());
                dao.insertJsonData(jsonData);

                listView();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.main_button_write:
                Intent intentWrite = new Intent(this, WriteActivity.class);
                startActivity(intentWrite);
                break;
            case R.id.main_button_refresh:
                refreshData();
                break;
            case R.id.main_button_simple_list1:
                Intent intentSimpleList1 = new Intent(this, SimpleList1Activity.class);
                startActivity(intentSimpleList1);
                break;
            case R.id.main_button_simple_list2:
                Intent intentSimpleList2 = new Intent(this, SimpleList2Activity.class);
                startActivity(intentSimpleList2);
                break;
            case R.id.main_button_custom_list:
                Intent intentCustomList = new Intent(this, CustomListActivity.class);
                startActivity(intentCustomList);
                break;

        }
    }
}
