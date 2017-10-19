package com.example.administrator.thebegininng;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017-10-17.
 */

public class CustomAdapter extends ArrayAdapter<Article>{
    private Context context;
    private int layoutResourceId;
    private ArrayList<Article> articleData;

    /*커스텀 어댑터는 컨텍스트정보, UI레이아웃ID, 리스트에 표시할 데이터가 필요합니다.
    UI레이아웃ID는 리스트의 칸 하나의 레이아웃을 구성하는 것으로
    이 예제에서는 res/layout/custom_list_row.xml (R.layout.custom_list_row)을 사용합니다.
    커스텀 어댑터는 이렇게 자신이 직접 레이아웃을 만들 수 있습니다.*/

    public CustomAdapter(Context context, int layoutResourceId, ArrayList<Article> articleData){
        super(context, layoutResourceId, articleData);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.articleData = articleData;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;

        if(row == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }

        TextView tvTitle = (TextView) row.findViewById(R.id.custom_row_textView1);
        TextView tvContent = (TextView) row.findViewById(R.id.custom_row_textView2);

        tvTitle.setText(articleData.get(position).getTitle());
        tvContent.setText(articleData.get(position).getContent());

        ImageView imageView = (ImageView) row.findViewById(R.id.custom_row_imageView);

        String img_path = context.getFilesDir().getPath() + "/" + articleData.get(position).getImgName();
        File img_load_path = new File(img_path);

        if(img_load_path.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(img_path);
            imageView.setImageBitmap(bitmap);
        }
        return row;
    }
}
