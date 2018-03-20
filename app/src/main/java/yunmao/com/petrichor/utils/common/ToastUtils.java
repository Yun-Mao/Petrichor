package yunmao.com.petrichor.utils.common;

import android.widget.Toast;

import yunmao.com.petrichor.BaseApplication;

/**
 * Created by msi on 2018/2/27.
 */
public class ToastUtils {
    public static void showShort(String msg) {
        Toast.makeText(BaseApplication.getApplication(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(String msg) {
        Toast.makeText(BaseApplication.getApplication(), msg, Toast.LENGTH_LONG).show();
    }
}
