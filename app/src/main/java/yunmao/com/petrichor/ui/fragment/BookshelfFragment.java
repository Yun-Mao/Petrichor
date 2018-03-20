package yunmao.com.petrichor.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import yunmao.com.petrichor.R;
import yunmao.com.petrichor.api.presenter.impl.BookshelfPresenterImpl;
import yunmao.com.petrichor.api.view.IBookListView;
import yunmao.com.petrichor.bean.table.Bookshelf;
import yunmao.com.petrichor.holder.BookShelfEditorHolder;
import yunmao.com.petrichor.ui.activity.BaseActivity;
import yunmao.com.petrichor.ui.activity.MainActivity;
import yunmao.com.petrichor.ui.adapter.BookShelfAdapter;
import yunmao.com.petrichor.ui.widget.RecyclerViewDecoration.StaggeredGridDecoration;
import yunmao.com.petrichor.utils.common.DensityUtils;
import yunmao.com.petrichor.utils.common.KeyBoardUtils;
import yunmao.com.petrichor.utils.common.TimeUtils;
import yunmao.com.petrichor.utils.common.UIUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import butterknife.BindView;

import static java.lang.Thread.sleep;

/**
 * Created by msi on 2018/2/27.
 */
public class BookshelfFragment extends BaseFragment implements IBookListView , SwipeRefreshLayout.OnRefreshListener{
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_widget)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private GridLayoutManager mLayoutManager;
    private BookShelfAdapter mbookshelfAdapter;
    private List<Bookshelf> mBookshelfs;
    private BookshelfPresenterImpl mBookshelfPresenter;
    private int spanCount = 1;
    private boolean isSortable;
    private BaseFragment currentFragment;
    private ItemTouchHelper touchHelper;

    public static BookshelfFragment mInstance;

    public static BookshelfFragment newInstance() {
        Bundle args = new Bundle();
        BookshelfFragment fragment = new BookshelfFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.bookshelf_fragment, container, false);
        if (savedInstanceState != null) {
            isSortable = savedInstanceState.getBoolean("isSortable");
        }
    }

    @Override
    @SuppressLint("RestrictedApi")
    protected void initEvents() {
        mToolbar.setTitle("Bookshelf");
        spanCount = getResources().getInteger(R.integer.gallery_span_count);
        mBookshelfPresenter = new BookshelfPresenterImpl(this);
        mBookshelfs = new ArrayList<>();
        mSwipeRefreshLayout.setColorSchemeResources(R.color.recycler_color1, R.color.recycler_color2,
                R.color.recycler_color3, R.color.recycler_color4);

        //设置布局管理器
        mLayoutManager = new GridLayoutManager(getActivity(), spanCount);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mbookshelfAdapter.getItemColumnSpan(position);
            }
        });
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //设置adapter
        mbookshelfAdapter = new BookShelfAdapter(getActivity(), mBookshelfs, spanCount);
        mbookshelfAdapter.setSortable(isSortable);
        mRecyclerView.setAdapter(mbookshelfAdapter);

        //设置Item增加、移除动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnScrollListener(new RecyclerViewScrollDetector());
        final int space = DensityUtils.dp2px(getActivity(), 4);
        mRecyclerView.addItemDecoration(new StaggeredGridDecoration(space, space, space, space, spanCount));
        mSwipeRefreshLayout.setOnRefreshListener(this);
//        mFab.setImageDrawable(AppCompatResources.getDrawable(getActivity(), R.drawable.ic_action_add_white));
        mFab.setOnClickListener(v -> {
            final BookShelfEditorHolder bookShelfHolder = new BookShelfEditorHolder(getActivity(), "", "");
            final int inputSpace = DensityUtils.dp2px(getActivity(), 16);
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(false)
                    .setView(bookShelfHolder.getContentView(), inputSpace, inputSpace, inputSpace, inputSpace)
                    .setTitle(UIUtils.getContext().getString(R.string.add_bookshelf))
                    .setNegativeButton(R.string.cancel, (dialog, which) -> {
                        dialog.dismiss();
                        KeyBoardUtils.closeKeyBord(bookShelfHolder.et_bookshelf_name, getActivity());
                    })
                    .setPositiveButton(R.string.ok, (dialog, which) -> {
                        if (!bookShelfHolder.check()) {
                            Snackbar.make(BaseActivity.activity.getToolbar(), R.string.bookshelf_name_is_empty, Snackbar.LENGTH_SHORT).show();
                        } else {
                            mBookshelfPresenter.addBookshelf(bookShelfHolder.getName(), bookShelfHolder.getRemark(), TimeUtils.getCurrentTime());
                        }
                        KeyBoardUtils.closeKeyBord(bookShelfHolder.et_bookshelf_name, getActivity());
                    }).create().show();
        });
        touchHelper = new ItemTouchHelper(new SimpleItemTouchHelperCallback(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sort) {
            if (isSortable) {
                for(int i = 0 ; i < mBookshelfs.size() ; i++) {
                    System.out.println(mBookshelfs.get(i).getTitle());
                    System.out.println(mBookshelfs.get(i).getOrder());
                    mBookshelfPresenter.updateBookshelf(mBookshelfs.get(i));
                }
                touchHelper.attachToRecyclerView(null);
            } else {
                touchHelper.attachToRecyclerView(mRecyclerView);
            }
            isSortable = !isSortable;
            mbookshelfAdapter.setSortable(isSortable);
            mbookshelfAdapter.notifyDataSetChanged();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initData(boolean isSavedNull) {
        onRefresh();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).setToolbar(mToolbar);
        init();
    }

    private void init() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isSortable", isSortable);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRefresh() {
        mBookshelfPresenter.loadBookshelf();
    }

    private void onLoadMore() {

    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(true));
    }

    @Override
    public void hideProgress() {
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(false));
    }

    @Override
    public void refreshData(Object result) {
        if (result instanceof List) {
            mBookshelfs.clear();
            mBookshelfs.addAll((List<Bookshelf>) result);
            mbookshelfAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void addData(Object result) {

    }

    class RecyclerViewScrollDetector extends RecyclerView.OnScrollListener {
        private int lastVisibleItem;
        private int mScrollThreshold = DensityUtils.dp2px(UIUtils.getContext(), 1);

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mbookshelfAdapter.getItemCount()) {
                onLoadMore();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            boolean isSignificantDelta = Math.abs(dy) > mScrollThreshold;

            if (isSignificantDelta) {
                if (dy > 0) {
                    mFab.hide();
                } else {
                    mFab.show();
                }
            }
            lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
        }
    }

    class SimpleItemTouchHelperCallback extends ItemTouchHelper.SimpleCallback {
        //保存被删除item信息，用于撤销操作
        BlockingQueue queue = new ArrayBlockingQueue(3);

        public SimpleItemTouchHelperCallback(int dragDirs, int swipeDirs) {
            super(dragDirs, swipeDirs);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            if (mBookshelfs.isEmpty()) {
                return false;
            }
            //得到拖动ViewHolder的position
            int fromPosition = viewHolder.getAdapterPosition();
            final Bookshelf bookshelf;
            bookshelf = mBookshelfs.get(fromPosition);
            //System.out.println(fromPosition);
            //得到目标ViewHolder的position
            int toPosition = target.getAdapterPosition();
            //System.out.println(toPosition);
            final Bookshelf bookshelf_new;
            if(toPosition==0){
                mBookshelfs.get(fromPosition).setOrder(mBookshelfs.get(toPosition).getOrder()+1);
            }else if(toPosition==mBookshelfs.size()-1){
                mBookshelfs.get(fromPosition).setOrder(mBookshelfs.get(toPosition).getOrder()-1);
            }else if(fromPosition < toPosition){
                mBookshelfs.get(fromPosition).setOrder((mBookshelfs.get(toPosition).getOrder()+mBookshelfs.get(toPosition+1).getOrder())/2);
            }else{
                mBookshelfs.get(fromPosition).setOrder((mBookshelfs.get(toPosition).getOrder()+mBookshelfs.get(toPosition-1).getOrder())/2);
            }
            //System.out.println("xx0"+bookshelf.getId());
            //System.out.println(bookshelf_new.getOrder());
            //System.out.println(bookshelf.getOrder());
            if (fromPosition < toPosition) {
                //分别把中间所有的item的位置重新交换
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(mBookshelfs, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(mBookshelfs, i, i - 1);
                }
            }
            //mBookshelfPresenter.updateBookshelf(bookshelf);
            mbookshelfAdapter.notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            if (mBookshelfs.isEmpty()) {
                return;
            }
            int position = viewHolder.getAdapterPosition();
            final Bookshelf bookshelf = mBookshelfs.get(position);
            bookshelf.setIndex(position);
            queue.add(bookshelf);
            mBookshelfs.remove(position);
            mbookshelfAdapter.notifyItemRemoved(position);
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (mBookshelfs.isEmpty()) {
                return;
            }
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                //滑动时改变Item的透明度
                final float alpha = 1 - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                viewHolder.itemView.setAlpha(alpha);
                viewHolder.itemView.setTranslationX(dX);
            } else {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }

        @Override
        public void clearView(final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            try {
                if (!queue.isEmpty()) {
                    Snackbar.make(mToolbar, R.string.delete_bookshelf_success, Snackbar.LENGTH_LONG).setAction(R.string.repeal, v -> {
                        final Bookshelf bookshelf = (Bookshelf) queue.remove();
                        mBookshelfs.add(bookshelf.getIndex(), bookshelf);
                        if (bookshelf.getIndex() == 0) {
                            mRecyclerView.smoothScrollToPosition(0);
                        }
                        mbookshelfAdapter.notifyDataSetChanged();
                    }).setCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {//回掉函数
                            super.onDismissed(snackbar, event);
                            if (event == DISMISS_EVENT_SWIPE || event == DISMISS_EVENT_TIMEOUT || event == DISMISS_EVENT_CONSECUTIVE) {
                                final Bookshelf bookshelf = (Bookshelf) queue.remove();
                                mBookshelfPresenter.deleteBookshelf(bookshelf.getId() + "");
                            }
                        }
                    }).show();
                }
            }
            catch(Exception e){
                Log.d("xxx", "Exception = " + e);
            }
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }
    }
}
