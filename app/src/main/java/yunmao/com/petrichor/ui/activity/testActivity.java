package yunmao.com.petrichor.ui.activity;

import yunmao.com.petrichor.ui.activity.ActivityRecog;
//import yunmao.com.petrichor.yy.activity.setting.OnlineSetting;
import yunmao.com.petrichor.yy.recognization.CommonRecogParams;
import yunmao.com.petrichor.yy.recognization.online.OnlineRecogParams;

/**
 * Created by msi on 2018/3/14.
 */

public class testActivity extends yunmao.com.petrichor.ui.activity.ActivityRecog {
    {
        descText = "在线普通识别功能(含长语音)\n"
                + "请保持设备联网，对着麦克风说出日常用语即可\n"
                + "普通录音限制60s。使用长语音无此限制， 点击“设置”按钮，开启长语音选项后，请选择输入法模型(适合长句)。\n"
                + "\n"
                + "集成指南：\n"
                + "1. PID 参数需要额外输入，Demo中已经有PidBuilder可以根据参数设置PID \n"
                + "2. ASR_START 不可连续调用，需要引擎空闲或者ASR_CANCEL输入事件后调用。详细请参见文档ASR_START的描述\n";
    }

    public testActivity() {
        super();
        //settingActivityClass = OnlineSetting.class;
    }

    @Override
    protected CommonRecogParams getApiParams() {
        return new OnlineRecogParams(this);
    }
}
