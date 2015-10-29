package com.jph.bpu.client;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jph.bpu.client.callback.RequestCallBack;
import com.jph.bpu.client.entity.FailInfo;
import com.jph.bpu.client.entity.SuccessInfo;
import com.jph.bpu.client.net.UploadUtil;

import java.util.ArrayList;

import static com.jph.bpu.client.R.id.progressBar;

public class MainActivity extends Activity {
    TextView textView;
    Button btnUpload;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView= (TextView) findViewById(R.id.textView);
        btnUpload=(Button)findViewById(R.id.btnUpload);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filePath=Environment.getExternalStorageDirectory().getPath()+"/456.png";
                String filePath2=Environment.getExternalStorageDirectory().getPath()+"/test.jpg";
                ArrayList files=new ArrayList();
                files.add(filePath2);
                files.add(filePath);
               new UploadUtil(files, new RequestCallBack() {
                   @Override
                   public void onStart() {
                       progressBar.setMax(100);
                   }
                   @Override
                   public void onLoading(long total, long current, boolean isUploading) {
                       int progress=(int) ((current *1.0 /total)*100);
                       progressBar.setProgress(progress);
                       textView.setText("total:"+total+" current:"+current+"\n"+progress+"%");
                   }
                   @Override
                   public void onSuccess(SuccessInfo info,boolean isLast) {
                       textView.setText("上传完成：上传文件的本地路径："+info.getLocalPath()+"\n服务器路径："+info.getNetPath()+"\nisLast:"+isLast);
                   }

                   @Override
                   public void onFailure(FailInfo error,boolean isLast) {
                       textView.setText("上传失败：上传文件的本地路径："+error.getLocalPath()+"\nisLast:"+isLast);
                   }
               }).execute();
            }
        });
    }
}
