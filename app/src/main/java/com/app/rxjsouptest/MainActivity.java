package com.app.rxjsouptest;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.rxjsoup.RxJsoup;
import com.app.rxjsoup.SimpleObserver;

import org.jsoup.nodes.Document;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvContent = findViewById(R.id.tv_content);
        RxJsoup.requestDocument(RxJsoup.request().url("").build())
               .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread())
               .subscribe(new SimpleObserver<Document>(){

                   @Override
                   public void onNext(Document document) {
                   }

               });
    }
}
