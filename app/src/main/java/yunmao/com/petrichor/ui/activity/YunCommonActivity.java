package yunmao.com.petrichor.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import yunmao.com.petrichor.R;

import yunmao.com.petrichor.yy.recognization.online.InFileStream;
import yunmao.com.petrichor.yy.util.Logger;

/**
 * Created by msi on 2018/3/14.
 */

public abstract class YunCommonActivity extends AppCompatActivity {
    //protected TextView txtLog;
    protected Button btn;
    protected Button setting;
    protected EditText txtResult;

    protected Handler handler;

    protected String descText;

    protected int layout = R.layout.show_fragment;

    protected Class settingActivityClass = null;

    protected boolean running = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setStrictMode();
        InFileStream.setContext(this);
        setContentView(layout);
        initView();
        handler = new Handler() {

            /*
             * @param msg
             */
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                handleMsg(msg);
            }

        };
        Logger.setHandler(handler);
        initPermission();
        initRecog();
    }


    protected abstract void initRecog();

    protected void handleMsg(Message msg) {
//        if (txtLog != null && msg.obj != null) {
//            txtLog.append(msg.obj.toString() + "\n");
//        }
    }

    protected void initView() {
        txtResult = (EditText) findViewById(R.id.et_notes_note);
//        txtLog = (TextView) findViewById(R.id.txtLog);
        btn = (Button) findViewById(R.id.btn);
        setting = (Button) findViewById(R.id.setting);
        //txtLog.setText(descText + "\n");
        if (setting != null && settingActivityClass != null) {
            setting.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    running = true;
                    Intent intent = new Intent(YunCommonActivity.this, settingActivityClass);
                    startActivityForResult(intent, 1);
                }
            });
        }

    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    protected void initPermission() {
        String[] permissions = {
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.

            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
    }

    private void setStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());

    }
}
