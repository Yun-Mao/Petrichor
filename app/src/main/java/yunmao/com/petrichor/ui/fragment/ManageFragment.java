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
import yunmao.com.petrichor.ui.activity.MainActivity;
import yunmao.com.petrichor.ui.activity.TakePhoteActivity;

/**
 * Created by msi on 2018/2/27.
 */

public class ManageFragment extends BaseFragment implements View.OnClickListener{
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private ImageView imageView;
    public static ManageFragment newInstance() {
        Bundle args = new Bundle();
        ManageFragment fragment = new ManageFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected void initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.tool_fragment, container, false);
    }
    @Override
    protected void initData(boolean isSavedNull) {
    }
    @Override
    protected void initEvents() {
        mToolbar.setTitle("设置");
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
                Intent intent = new Intent(getActivity(), TakePhoteActivity.class);
                startActivity(intent);
                //mToolbar.setTitle("书摘sss");
                //((MainActivity) getActivity()).setToolbar(mToolbar);
                break;
        }
    }
}
