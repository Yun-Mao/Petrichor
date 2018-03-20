package yunmao.com.petrichor.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import org.zackratos.ultimatebar.UltimateBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import yunmao.com.petrichor.R;

import me.wangyuwei.particleview.ParticleView;

/**
 * Created by msi on 2018/1/27.
 * 闪屏进入界面
 */

public class SplashActivity extends AppCompatActivity {
    private ParticleView pv_logo;//网络开源项目
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                deepFile("tessdata");
//            }
//        }).start();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        pv_logo = (ParticleView) findViewById(R.id.pv_logo);
        pv_logo.setOnParticleAnimListener(new ParticleView.ParticleAnimListener() {
            @Override
            public void onAnimationEnd() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.finish();
            }
        });
        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setImmersionBar();
        pv_logo.startAnim();
    }
    /**
     * 将assets中的文件复制出
     *
     * @param path
     */
    public void deepFile(String path) {
        String newPath = getExternalFilesDir(null) + "/";
        try {
            String str[] = getAssets().list(path);
            if (str.length > 0) {//如果是目录
                File file = new File(newPath + path);
                file.mkdirs();
                for (String string : str) {
                    path = path + "/" + string;
                    deepFile(path);
                    path = path.substring(0, path.lastIndexOf('/'));//回到原来的path
                }
            } else {//如果是文件
                InputStream is = getAssets().open(path);
                FileOutputStream fos = new FileOutputStream(new File(newPath + path));
                byte[] buffer = new byte[1024];
                int count = 0;
                while (true) {
                    count++;
                    int len = is.read(buffer);
                    if (len == -1) {
                        break;
                    }
                    fos.write(buffer, 0, len);
                }
                is.close();
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
