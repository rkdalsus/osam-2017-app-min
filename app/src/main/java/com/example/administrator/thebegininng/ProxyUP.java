package com.example.administrator.thebegininng;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Administrator on 2017-10-18.
 */

public class ProxyUP {
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void uploadArticle(Article article, String filePath, AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("title", article.getTitle());
        params.put("write", article.getWriter());
        params.put("id", article.getId());
        params.put("content", article.getContent());
        params.put("writeDate", article.getWriteDate());
        params.put("imgName", article.getImgName());

        try {
            params.put("uploadedfile", new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        client.post("http://http://10.53.128.116:5009/upload", params, responseHandler);
    }
}
