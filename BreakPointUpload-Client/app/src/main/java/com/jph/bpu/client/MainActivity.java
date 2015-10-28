package com.jph.bpu.client;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jph.bpu.client.callback.RequestCallBack;
import com.jph.bpu.client.entity.FailInfo;
import com.jph.bpu.client.entity.SuccessInfo;
import com.jph.bpu.client.net.UploadUtil;

public class MainActivity extends Activity {
    TextView textView;
    Button btnUpload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView= (TextView) findViewById(R.id.textView);
        btnUpload=(Button)findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filePath=Environment.getExternalStorageDirectory().getPath()+"/456.png";
               new UploadUtil(filePath, new RequestCallBack() {
                   @Override
                   public void onStart() {

                   }

                   @Override
                   public void onLoading(long total, long current, boolean isUploading) {
                       textView.setText("total:"+total+" current:"+current);
                   }

                   @Override
                   public void onSuccess(SuccessInfo info) {
                       textView.setText("上传完成：上传文件的本地路径："+info.getLocalPath()+"\n服务器路径："+info.getNetPath());
                   }

                   @Override
                   public void onFailure(FailInfo error) {
                       textView.setText("上传失败：上传文件的本地路径："+error.getLocalPath());
                   }
               }).execute();
            }
        });
    }
}
