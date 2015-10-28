package com.jph.bpu.client;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jph.bpu.client.net.KsudiUpload;

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
                String filePath=Environment.getExternalStorageDirectory().getPath()+"/test.jpg";
                new KsudiUpload(MainActivity.this,filePath).execute();
            }
        });
    }
}
