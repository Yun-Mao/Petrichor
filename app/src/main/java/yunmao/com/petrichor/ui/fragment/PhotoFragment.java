package yunmao.com.petrichor.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import butterknife.BindView;
import yunmao.com.petrichor.R;
import yunmao.com.petrichor.ui.activity.CameratoActivity;
import yunmao.com.petrichor.ui.activity.MainActivity;
import yunmao.com.petrichor.ui.activity.TakePhoteActivity;

/**
 * Created by msi on 2018/2/27.
 */

public class PhotoFragment extends BaseFragment implements View.OnClickListener{
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
    @BindView(R.id.toolbar)
    Toolbar mToolbar;//绑定Toolbar
    private ImageView imageView;
    public static PhotoFragment newInstance() {
        PhotoFragment fragment = new PhotoFragment();
        return fragment;
    }
    @Override
    protected void initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.photo_fragment, container, false);
    }
    @Override
    protected void initData(boolean isSavedNull) {
        //不需要加载数据
    }
    @Override
    protected void initEvents() {
        mToolbar.setTitle("书摘");
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        ((MainActivity) getActivity()).setToolbar(mToolbar);
    }
    private void init() {
        imageView = (ImageView) getView().findViewById(R.id.btn_camera);
        imageView.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_camera:
                Intent intent = new Intent(getActivity(), CameratoActivity.class);
                startActivity(intent);
                break;
        }
    }
}
