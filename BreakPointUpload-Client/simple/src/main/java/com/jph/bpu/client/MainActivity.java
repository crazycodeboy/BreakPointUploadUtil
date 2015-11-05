package com.jph.bpu.client;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jph.bpu.client.util.Constant;
import com.jph.bpu.library.UplaodUtil;
import com.jph.bpu.library.callback.RequestCallBack;
import com.jph.bpu.library.entity.FailInfo;
import com.jph.bpu.library.entity.FileBody;
import com.jph.bpu.library.entity.SuccessInfo;

import java.util.ArrayList;

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
                String filePath3=Environment.getExternalStorageDirectory().getPath()+"/123.jpg";
                String filePath4=Environment.getExternalStorageDirectory().getPath()+"/888.jpg";
                String filePath5=Environment.getExternalStorageDirectory().getPath()+"/1.jpg";
                String filePath6=Environment.getExternalStorageDirectory().getPath()+"/2.jpg";
                String filePath7=Environment.getExternalStorageDirectory().getPath()+"/3.jpg";
                ArrayList files=new ArrayList();
                files.add(new FileBody(filePath2,"pickup"));
                files.add(new FileBody(filePath3,"pickup"));
                files.add(new FileBody(filePath4,"pickup"));
                files.add(new FileBody(filePath5,"pickup"));
                files.add(new FileBody(filePath6,"pickup"));
                files.add(new FileBody(filePath7,"pickup"));
                files.add(new FileBody(filePath,"pickup"));
                new UplaodUtil().upload(files, Constant.strSerUrl, new RequestCallBack() {
                    @Override
                    public void onStart() {
                        progressBar.setMax(100);
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        int progress = (int) ((current * 1.0 / total) * 100);
                        progressBar.setProgress(progress);
                        textView.setText("total:" + total + " current:" + current + "\n" + progress + "%");
                    }

                    @Override
                    public void onSuccess(SuccessInfo info, boolean isLast) {
                        textView.setText("上传完成：上传文件的本地路径：" + info.getLocalPath() + "\n服务器路径：" + info.getNetPath() + "\nisLast:" + isLast);
                    }

                    @Override
                    public void onFailure(FailInfo error, boolean isLast) {
                        textView.setText("上传失败：上传文件的本地路径：" + error.getLocalPath() + "\nisLast:" + isLast);
                    }
                });
            }
        });
    }
}
