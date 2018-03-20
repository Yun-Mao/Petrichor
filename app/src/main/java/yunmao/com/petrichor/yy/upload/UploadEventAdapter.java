package yunmao.com.petrichor.yy.upload;

import yunmao.com.petrichor.yy.util.Logger;
import yunmao.com.petrichor.yy.wakeup.IWakeupListener;
import com.baidu.speech.EventListener;


/**
 * Created by fujiayi on 2017/6/20.
 */

public class UploadEventAdapter implements EventListener {

    private static final int ERROR_NONE = 0;

    private IWakeupListener listener;

    public UploadEventAdapter() {

    }

    private static final String TAG = "WakeupEventAdapter";

    @Override
    public void onEvent(String name, String params, byte[] data, int offset, int length) {
        Logger.error(TAG, "name:" + name + "; params:" + params);

    }

}
