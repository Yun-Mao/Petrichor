package yunmao.com.petrichor.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.baidu.ocr.sdk.OCR;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import yunmao.com.petrichor.R;
import yunmao.com.petrichor.api.common.DatabaseHelper;
import yunmao.com.petrichor.api.common.service.HtmlService;
import yunmao.com.petrichor.api.presenter.impl.BookshelfPresenterImpl;
import yunmao.com.petrichor.api.presenter.impl.NotesPresenterImpl;
import yunmao.com.petrichor.api.view.IBookListView;
import yunmao.com.petrichor.api.view.INotesView;
import yunmao.com.petrichor.utils.FileUtil;
import yunmao.com.petrichor.utils.common.TimeUtils;
import yunmao.com.petrichor.utils.common.UIUtils;
import yunmao.com.petrichor.yy.recognization.CommonRecogParams;
import yunmao.com.petrichor.yy.recognization.online.OnlineRecogParams;

/**
 * Created by msi on 2018/3/9.
 */

public class CameratoActivity extends yunmao.com.petrichor.ui.activity.ActivityRecog  implements INotesView ,IBookListView {
    private static final int REQUEST_CODE_BOOK = 104;
    private static final int REQUEST_CODE_GENERAL = 105;
    private static final int REQUEST_CODE_GENERAL_BASIC = 106;
    private static final int REQUEST_CODE_ACCURATE_BASIC = 107;
    private static final int REQUEST_CODE_ACCURATE = 108;
    private static final int REQUEST_CODE_GENERAL_ENHANCED = 109;
    private static final int REQUEST_CODE_GENERAL_WEBIMAGE = 110;
    private static final int REQUEST_CODE_BANKCARD = 111;
    private static final int REQUEST_CODE_VEHICLE_LICENSE = 120;
    private static final int REQUEST_CODE_DRIVING_LICENSE = 121;
    private static final int REQUEST_CODE_LICENSE_PLATE = 122;
    private static final int REQUEST_CODE_BUSINESS_LICENSE = 123;
    private static final int REQUEST_CODE_RECEIPT = 124;

    private boolean hasGotToken = false;
    protected Button next;
    private AlertDialog.Builder alertDialog;
    private EditText textView;
    private EditText isbnView;
    public EditText et_notes_name;
    public EditText et_notes_remark;
    private EditText et_notes_paper;
    private EditText et_notes_note;
    private EditText et_notes_page;
    private NotesPresenterImpl mNotesPresenter;
    private BookshelfPresenterImpl mBookshelfPresenter;
    public CameratoActivity() {
        super();
        //settingActivityClass = OnlineSetting.class;
    }
    @Override
    protected CommonRecogParams getApiParams() {
        return new OnlineRecogParams(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_fragment);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());
        initView();
        initPermission();

        alertDialog = new AlertDialog.Builder(this);
        textView = (EditText) findViewById(R.id.et_notes_paper);
        isbnView=(EditText) findViewById(R.id.et_notes_remark);
        et_notes_name = (EditText) findViewById(R.id.et_notes_name);
        next=(Button)findViewById(R.id.add_button);
        mNotesPresenter = new NotesPresenterImpl(this);
        mBookshelfPresenter = new BookshelfPresenterImpl(this);

//        try {
//            String htmlContent = HtmlService.getHtml("https://api.douban.com/v2/book/isbn/9781107626133");
//            JSONObject jsonObject = new JSONObject(htmlContent);
//            String name = jsonObject.getString("title");
//            et_notes_name.setText(name);
//        } catch (Exception e) {
//            et_notes_name.setText("程序出现异常："+e.toString());
//        }

        // 通用文字识别
        findViewById(R.id.camera_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkTokenStatus()) {
                    return;
                }
                Intent intent = new Intent(CameratoActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                        CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent, REQUEST_CODE_GENERAL_BASIC);
            }
        });
        findViewById(R.id.next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkTokenStatus()) {
                    return;
                }
                Intent intent = new Intent(CameratoActivity.this, CaptureActivity.class);
                Bundle b = new Bundle();
                b.putBoolean("add", true);
                intent.putExtras(b);
                startActivityForResult(intent, REQUEST_CODE_BOOK);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_notes_name = (EditText) findViewById(R.id.et_notes_name);
                et_notes_remark = (EditText) findViewById(R.id.et_notes_remark);
                et_notes_paper = (EditText) findViewById(R.id.et_notes_paper);
                et_notes_note = (EditText) findViewById(R.id.et_notes_note);
                et_notes_page = (EditText) findViewById(R.id.et_notes_page);
                int a;
                try {
                    a = Integer.parseInt(et_notes_page.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    a=0;
                }
                if (et_notes_name.getText()==null) {
                    Snackbar.make(BaseActivity.activity.getToolbar(), R.string.notes_name_is_empty, Snackbar.LENGTH_SHORT).show();
                } else {
                    mNotesPresenter.addNotes(et_notes_name.getText().toString(), et_notes_paper.getText().toString(),et_notes_note.getText().toString(),a, TimeUtils.getCurrentTime());
                    if(search_book(et_notes_name.getText().toString())){
                    }else {
                        mBookshelfPresenter.addBookshelf(et_notes_name.getText().toString(), et_notes_remark.getText().toString(), TimeUtils.getCurrentTime());
                    }
                    Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_LONG).show();
                }
            }
        });
        // 请选择您的初始化方式
        // initAccessToken();
        initAccessTokenWithAkSk();
    }

    private boolean checkTokenStatus() {
        if (!hasGotToken) {
            Toast.makeText(getApplicationContext(), "token还未成功获取", Toast.LENGTH_LONG).show();
        }
        return hasGotToken;
    }

    private void initAccessToken() {
        OCR.getInstance().initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                String token = accessToken.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                alertText("licence方式获取token失败", error.getMessage());
            }
        }, getApplicationContext());
    }

    private void initAccessTokenWithAkSk() {
        OCR.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                String token = result.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                alertText("AK，SK方式获取token失败", error.getMessage());
            }
        }, getApplicationContext(), "。。。", "。。。");
    }

    private void alertText(final String title, final String message) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String str="";
//                alertDialog.setTitle(title)
//                        .setMessage(message)
//                        .setPositiveButton("确定", null)
//                        .show();
                try {
                    JSONObject jsonObject = new JSONObject(message);
                    String id = jsonObject.getString("words_result");
                    JSONArray jsonArray = new JSONArray(id);
                    for (int i=0; i < jsonArray.length(); i++){
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        String words = jsonObject2.getString("words");
                        str+=words;
                        //textView.setText(words);
                    }
                }
                catch(Exception e){
                    textView.setText(e.toString());
                }
                textView.setText(str);
            }
        });
    }

    private void infoPopText(final String result) {
        alertText("", result);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initAccessToken();
        } else {
            Toast.makeText(getApplicationContext(), "需要android.permission.READ_PHONE_STATE", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_BOOK && resultCode == REQUEST_CODE_BOOK) {
            String temp = data.getStringExtra("q");
            try {
                String htmlContent = HtmlService.getHtml("https://api.douban.com/v2/book/isbn/"+temp);
                JSONObject jsonObject = new JSONObject(htmlContent);
                String name = jsonObject.getString("title");
                et_notes_name.setText(name);
            } catch (Exception e) {
                et_notes_name.setText("程序出现异常："+e.toString());
            }
            isbnView.setText(temp);
        }
        // 识别成功回调，通用文字识别（含位置信息）
        if (requestCode == REQUEST_CODE_GENERAL && resultCode == Activity.RESULT_OK) {
            RecognizeService.recGeneral(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            infoPopText(result);
                        }
                    });
        }

        // 识别成功回调，通用文字识别（含位置信息高精度版）
        if (requestCode == REQUEST_CODE_ACCURATE && resultCode == Activity.RESULT_OK) {
            RecognizeService.recAccurate(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            infoPopText(result);
                        }
                    });
        }

        // 识别成功回调，通用文字识别
        if (requestCode == REQUEST_CODE_GENERAL_BASIC && resultCode == Activity.RESULT_OK) {
            RecognizeService.recGeneralBasic(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            infoPopText(result);
                        }
                    });
        }

        // 识别成功回调，通用文字识别（高精度版）
        if (requestCode == REQUEST_CODE_ACCURATE_BASIC && resultCode == Activity.RESULT_OK) {
            RecognizeService.recAccurateBasic(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            infoPopText(result);
                        }
                    });
        }

        // 识别成功回调，通用文字识别（含生僻字版）
        if (requestCode == REQUEST_CODE_GENERAL_ENHANCED && resultCode == Activity.RESULT_OK) {
            RecognizeService.recGeneralEnhanced(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            infoPopText(result);
                        }
                    });
        }

        // 识别成功回调，网络图片文字识别
        if (requestCode == REQUEST_CODE_GENERAL_WEBIMAGE && resultCode == Activity.RESULT_OK) {
            RecognizeService.recWebimage(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            infoPopText(result);
                        }
                    });
        }

        // 识别成功回调，银行卡识别
        if (requestCode == REQUEST_CODE_BANKCARD && resultCode == Activity.RESULT_OK) {
            RecognizeService.recBankCard(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            infoPopText(result);
                        }
                    });
        }

        // 识别成功回调，行驶证识别
        if (requestCode == REQUEST_CODE_VEHICLE_LICENSE && resultCode == Activity.RESULT_OK) {
            RecognizeService.recVehicleLicense(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            infoPopText(result);
                        }
                    });
        }

        // 识别成功回调，驾驶证识别
        if (requestCode == REQUEST_CODE_DRIVING_LICENSE && resultCode == Activity.RESULT_OK) {
            RecognizeService.recDrivingLicense(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            infoPopText(result);
                        }
                    });
        }

        // 识别成功回调，车牌识别
        if (requestCode == REQUEST_CODE_LICENSE_PLATE && resultCode == Activity.RESULT_OK) {
            RecognizeService.recLicensePlate(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            infoPopText(result);
                        }
                    });
        }

        // 识别成功回调，营业执照识别
        if (requestCode == REQUEST_CODE_BUSINESS_LICENSE && resultCode == Activity.RESULT_OK) {
            RecognizeService.recBusinessLicense(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            infoPopText(result);
                        }
                    });
        }

        // 识别成功回调，通用票据识别
        if (requestCode == REQUEST_CODE_RECEIPT && resultCode == Activity.RESULT_OK) {
            RecognizeService.recReceipt(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            infoPopText(result);
                        }
                    });
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放内存资源
        OCR.getInstance().release();
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void addData(Object result) {

    }
    @Override
    public void showProgress() {
        //mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(true));
    }

    @Override
    public void hideProgress() {
        //mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(false));
    }
    @Override
    public void refreshData(Object result) {

    }
    private boolean search_book(String str) {

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql="select * from bookshelf  where  title='"+str+"'";
        Cursor cursor = db.rawQuery(sql,null);
        while (cursor.moveToNext()) {
            db.close();
            Log.i(" search_city_name_exist", str + "在数据库已存在,return true");
            cursor.close();
            return true;// //有城市在数据库已存在，返回true
        }
        db.close();
        cursor.close();
        Log.i(" search_city_name_exist", str + "在数据库不存在，return false");
        return false;// //在数据库以前存在 false

    }
}
