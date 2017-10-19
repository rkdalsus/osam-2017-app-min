package com.example.administrator.thebegininng;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

import static android.R.attr.id;

public class WriteActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etWriter;
    private EditText etTitle;
    private EditText etContent;
    private ImageButton ibPhoto;
    private Button buUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        etWriter    = (EditText)    findViewById(R.id.writeText1);
        etTitle     = (EditText)    findViewById(R.id.writeText2);
        etContent   = (EditText)    findViewById(R.id.writeText3);
        ibPhoto     = (ImageButton) findViewById(R.id.writeImageButton);
        buUpload    = (Button)      findViewById(R.id.writeSaveButton);

        ibPhoto.setOnClickListener(this);
        buUpload.setOnClickListener(this);
    }

    private String filePath;
    private String fileName;

    @Override
    protected void onActivityResult(int requsetCode, int resultCode, Intent data) {
        super.onActivityResult(requsetCode, resultCode, data);
        try {
            if (requsetCode == REQUEST_PHOTO_ALBUM) {
                Uri uri = getRealPathUri(data.getData());
                filePath = uri.toString();
                fileName = uri.getLastPathSegment();

                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                ibPhoto.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            Log.e("test", "onActivityResult ERROR:" + e);
        }
    }

    private Uri getRealPathUri(Uri uri) {
        Uri filePathUri = uri;
        if (uri.getScheme().toString().compareTo("content") == 0) {
            Cursor cursor = getApplicationContext().getContentResolver().query(uri, null, null,null, null);
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                filePathUri = Uri.parse(cursor.getString(column_index));
            }
        }
        return filePathUri;
    }

    private static final int REQUEST_PHOTO_ALBUM = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHOTO_ALBUM: {
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent, REQUEST_PHOTO_ALBUM);
                }
                else {
                    Toast.makeText(this, "사진을 추가하려면 권한을 허용하세요.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    public ProgressDialog progressDialog;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.writeImageButton: {
                if (Build.VERSION.SDK_INT >= 23) {
                    // Here, thisActivity is the current activity
                    if (ContextCompat.checkSelfPermission(WriteActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(WriteActivity.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE)) {

                        } else {

                            ActivityCompat.requestPermissions(WriteActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    REQUEST_PHOTO_ALBUM);

                        }
                    } else {
                        ActivityCompat.requestPermissions(WriteActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_PHOTO_ALBUM);
                    }
                } else {

                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent, REQUEST_PHOTO_ALBUM);
                }
                break;
            }
            case R.id.writeSaveButton: {
                final Handler handler = new Handler();
                progressDialog = ProgressDialog.show(WriteActivity.this, "", "업로드 중입니다.");
                String ID = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA).format(new Date());

                Article article = new Article(0,
                        etTitle.getText().toString(),
                        etWriter.getText().toString(),
                        ID,
                        etContent.getText().toString(),
                        date,
                        fileName);

                Log.i("TAG", "onClick: " + article.toString());

                ProxyUP.uploadArticle(article, filePath, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.e("TAG", "onSuccess: " + statusCode);
                        progressDialog.cancel();
                        Toast.makeText(getApplicationContext(), "onSuccess", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.e("TAG", "onFailure statusCode: " + statusCode);
                        Log.e("TAG", "onFailure responseBody: " + responseBody);
                        Log.e("TAG", "onFailure error: " + error);
                        Log.e("TAG", "onFailure filePath: " + filePath);
                        progressDialog.cancel();
                        Toast.makeText(getApplicationContext(), "onFailure", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public boolean getUseSynchronousMode() {
                        return false;
                    }
                });
                break;


            }
        }
    }
}
