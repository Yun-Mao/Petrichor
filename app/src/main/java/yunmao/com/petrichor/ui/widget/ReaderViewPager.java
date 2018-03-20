package yunmao.com.petrichor.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by msi on 2018/2/27.
 */

public class ReaderViewPager extends ViewPager {
    public ReaderViewPager(Context context) {
        super(context);
    }

    public ReaderViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        super.onPageScrolled(position, offset, offsetPixels);
    }
}
