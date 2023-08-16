package com.luopeng.rosbridge.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jilk.ros.ROSClient;
import com.jilk.ros.rosbridge.ROSBridgeClient;
import com.luopeng.rosbridge.R;
import com.luopeng.rosbridge.RCApplication;


public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    EditText etIP;
    EditText etPort;

    ROSBridgeClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // ButterKnife.bind(this);
        etIP= ((EditText) findViewById(R.id.et_ip));
        etPort= ((EditText) findViewById(R.id.et_port));
        findViewById(R.id.btn_connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = etIP.getText().toString();
                String port = etPort.getText().toString();
                connect(ip, port);
            }
        });
    }

    private void connect(String ip, String port) {
        client = new ROSBridgeClient("ws://" + ip + ":" + port);
        boolean conneSucc = client.connect(new ROSClient.ConnectionStatusListener() {
            @Override
            public void onConnect() {
                client.setDebug(true);
                ((RCApplication)getApplication()).setRosClient(client);
                showTip("Connect ROS success");
                Log.d(TAG,"Connect ROS success");
                startActivity(new Intent(MainActivity.this,NodesActivity.class));
            }

            @Override
            public void onDisconnect(boolean normal, String reason, int code) {
                showTip("ROS disconnect");
                Log.d(TAG,"ROS disconnect");
            }

            @Override
            public void onError(Exception ex) {
                ex.printStackTrace();
                showTip("ROS communication error");
                Log.d(TAG,"ROS communication error");
            }
        });
    }

    private void showTip(final String tip) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, tip,Toast.LENGTH_SHORT).show();
            }
        });
    }

}
