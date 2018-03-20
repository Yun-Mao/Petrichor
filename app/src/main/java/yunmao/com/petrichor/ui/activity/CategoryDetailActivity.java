package yunmao.com.petrichor.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import yunmao.com.petrichor.R;
import yunmao.com.petrichor.common.Constant;
import yunmao.com.petrichor.ui.adapter.CategoryDetailAdapter;
import yunmao.com.petrichor.ui.fragment.BaseFragment;
import yunmao.com.petrichor.ui.fragment.CategoryDetailFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by msi on 2018/2/27.
 */
public class CategoryDetailActivity extends BaseActivity {
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    private int index;
    private String[] mCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_category_detail);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initEvents() {
        index = getIntent().getIntExtra("index", 0);
        setTitle(getIntent().getStringExtra("title"));
        switch (index) {
            case Constant.CATEGORY_LITERATURE:
                mCategory = getResources().getStringArray(R.array.book_category_literature);
                break;
            case Constant.CATEGORY_POPULAR:
                mCategory = getResources().getStringArray(R.array.book_category_popular);
                break;
            case Constant.CATEGORY_CULTURE:
                mCategory = getResources().getStringArray(R.array.book_category_culture);
                break;
            case Constant.CATEGORY_LIFE:
                mCategory = getResources().getStringArray(R.array.book_category_life);
                break;
            case Constant.CATEGORY_MANAGEMENT:
                mCategory = getResources().getStringArray(R.array.book_category_management);
                break;
            case Constant.CATEGORY_TECHNOLOGY:
                mCategory = getResources().getStringArray(R.array.book_category_technology);
                break;
            case Constant.CATEGORY_COUNTRY:
                mCategory = getResources().getStringArray(R.array.book_category_country);
                break;
            case Constant.CATEGORY_SUBJECT:
                mCategory = getResources().getStringArray(R.array.book_category_subject);
                break;
            case Constant.CATEGORY_AUTHOR:
                mCategory = getResources().getStringArray(R.array.book_category_author);
                break;
            case Constant.CATEGORY_PUBLISHER:
                mCategory = getResources().getStringArray(R.array.book_category_publisher);
                break;
            case Constant.CATEGORY_THRONG:
                mCategory = getResources().getStringArray(R.array.book_category_throng);
                break;
            case Constant.CATEGORY_RELIGION:
                mCategory = getResources().getStringArray(R.array.book_category_religion);
                break;
            case Constant.CATEGORY_OTHER:
                mCategory = getResources().getStringArray(R.array.book_category_other);
                break;
            default:
                mCategory = new String[]{};
                break;
        }

        List<BaseFragment> fragments = new ArrayList<>();
        for (String child : mCategory) {
            final CategoryDetailFragment fragment = CategoryDetailFragment.newInstance(child);
            fragments.add(fragment);
        }
        mViewPager.setAdapter(new CategoryDetailAdapter(getSupportFragmentManager(), fragments, mCategory));
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setCurrentItem(0);
        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
