package com.example.administrator.thebegininng;

import android.app.Activity;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017-10-17.
 */

public class Custom2Adapter extends ArrayAdapter<ListData>{
    private Context context;
    private int layoutResourceId;
    private ArrayList<ListData> listData;

    /*커스텀 어댑터는 컨텍스트정보, UI레이아웃ID, 리스트에 표시할 데이터가 필요합니다.
    UI레이아웃ID는 리스트의 칸 하나의 레이아웃을 구성하는 것으로
    이 예제에서는 res/layout/custom_list_row.xml (R.layout.custom_list_row)을 사용합니다.
    커스텀 어댑터는 이렇게 자신이 직접 레이아웃을 만들 수 있습니다.*/

    public Custom2Adapter(Context context, int layoutResourceId, ArrayList<ListData> listData){
        super(context, layoutResourceId, listData);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.listData = listData;
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

        tvTitle.setText(listData.get(position).getText1());
        tvContent.setText(listData.get(position).getText2());

        ImageView imageView = (ImageView) row.findViewById(R.id.custom_row_imageView);

        try{
            InputStream is = context.getAssets().open(listData.get(position).getImgName());

            Drawable d = Drawable.createFromStream(is, null);

            imageView.setImageDrawable(d);
        } catch(IOException e){
            Log.e("ERROR", "ERROR: " + e);
        }
        return row;
    }
}