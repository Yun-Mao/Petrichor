package yunmao.com.petrichor.ui.widget.RecyclerViewDecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by msi on 2018/2/27.
 */
public class SpacesCategoryDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesCategoryDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = 0;
        outRect.right = 0;
        outRect.bottom = space;
    }
}
