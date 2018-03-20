package yunmao.com.petrichor.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;
import java.util.Random;

import yunmao.com.petrichor.R;
import yunmao.com.petrichor.api.ApiCompleteListener;
import yunmao.com.petrichor.api.model.INotesModel;
import yunmao.com.petrichor.api.model.impl.NotesModelImpl;
import yunmao.com.petrichor.api.presenter.INotesPresenter;
import yunmao.com.petrichor.api.presenter.impl.NotesPresenterImpl;
import yunmao.com.petrichor.api.view.INotesView;
import yunmao.com.petrichor.bean.http.douban.BaseResponse;
import yunmao.com.petrichor.bean.table.Notes;
import yunmao.com.petrichor.holder.NotesEditorHolder;
import yunmao.com.petrichor.ui.activity.BaseActivity;
import yunmao.com.petrichor.utils.common.DensityUtils;
import yunmao.com.petrichor.utils.common.KeyBoardUtils;
import yunmao.com.petrichor.utils.common.TimeUtils;
import yunmao.com.petrichor.utils.common.UIUtils;

import static yunmao.com.petrichor.utils.common.UIUtils.getContext;

/**
 * Created by msi on 2018/3/5.
 */

public class NotesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int TYPE_EMPTY = 0;
    private static final int TYPE_DEFAULT = 1;
    private final List<Notes> notes;
    private Context mContext;
    private int columns;
    private boolean isSortable;
    public NotesAdapter(Context context, List<Notes> responses, int columns) {
        this.notes = responses;
        this.columns = columns;
        this.mContext = context;
    }

    //
    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
    //


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_DEFAULT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_note, parent, false);
            return new NotesAdapter.NotesHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty, parent, false);
            return new NotesAdapter.EmptyHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (notes == null || notes.isEmpty()) {
            return TYPE_EMPTY;
        } else {
            return TYPE_DEFAULT;
        }
    }

    public int getItemColumnSpan(int position) {
        switch (getItemViewType(position)) {
            case TYPE_DEFAULT:
                return 1;
            default:
                return columns;
        }
    }
    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NotesAdapter.NotesHolder) {
            final Notes note = notes.get(position);
            Random random = new Random();   //创建随机颜色
            int red = random.nextInt(200) + 22;
            int green = random.nextInt(200) + 22;
            int blue = random.nextInt(200) + 22;
            int color = Color.rgb(red, green, blue);
            ((NotesAdapter.NotesHolder) holder).rl_content.setBackgroundColor(color);
            ((NotesAdapter.NotesHolder) holder).tv_notes_name.setText(note.getTitle());
            ((NotesAdapter.NotesHolder) holder).tv_paper.setText(note.getPaper());
            ((NotesAdapter.NotesHolder) holder).tv_note.setText(note.getNote());
            ((NotesAdapter.NotesHolder) holder).tv_page.setText(note.getPage()+"");
            ((NotesAdapter.NotesHolder) holder).tv_create_time.setText(note.getCreateTime());
//            ((NotesAdapter.NotesHolder) holder).itemView.setOnClickListener(v -> {
//                    final NotesEditorHolder noteHolder = new NotesEditorHolder(mContext, note.getTitle(), note.getPaper(),note.getNote(),note.getPage());
//                    final int space = DensityUtils.dp2px(UIUtils.getContext(), 16);
//                    final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                    builder.setCancelable(false)
//                            .setView(noteHolder.getContentView(), space, space, space, space)
//                            .setTitle(UIUtils.getContext().getString(R.string.watch))
//                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                    KeyBoardUtils.closeKeyBord(noteHolder.et_notes_name, mContext);
//                                }
//                            })
//                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    if (!noteHolder.check()) {
//                                        Snackbar.make(BaseActivity.activity.getToolbar(), R.string.bookshelf_name_is_empty, Snackbar.LENGTH_SHORT).show();
//                                        return;
//                                    } else {
//
//                                    }
//                                    KeyBoardUtils.closeKeyBord(noteHolder.et_notes_name, mContext);
//                                }
//                            }).create().show();
//                new AlertDialog.Builder(getContext()).setTitle("系统提示")//设置对话框标题
//
//                        .setMessage("请确认所有数据都保存后再推出系统！")//设置显示的内容
//
//                        .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
//
//
//
//                            @Override
//
//                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
//
//                                // TODO Auto-generated method stub
//
//
//
//                            }
//
//                        }).setNegativeButton("返回",new DialogInterface.OnClickListener() {//添加返回按钮
//
//
//
//                    @Override
//
//                    public void onClick(DialogInterface dialog, int which) {//响应事件
//
//                        // TODO Auto-generated method stub
//
//                        //Log.i("alertdialog"," 请保存数据！");
//
//                    }
//                }).show();//在按键响应事件中显示此对话框
//            });
            holder.itemView.setTag(note.getTitle());
            if (!isSortable) {
                holder.itemView.setAlpha(1.0f);
               ((NotesAdapter.NotesHolder) holder).itemView.setOnLongClickListener(new View.OnLongClickListener()
                {
                    @Override
                    public boolean onLongClick(View v)
                    {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
//                        mOnItemClickLitener.onItemClick(holder.itemView, pos);
                        return false;
                    }
                });
            } else {
                holder.itemView.setAlpha(0.4f);
                holder.itemView.setOnLongClickListener(null);
            }
        }
    }

    public boolean isSortable() {
        return isSortable;
    }

    public void setSortable(boolean sortable) {
        isSortable = sortable;
    }

    @Override
    public int getItemCount() {
        if (notes.isEmpty()) {
            return 1;
        }
        return notes.size();
    }

    class NotesHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rl_content;
        private TextView tv_notes_name;
        private TextView tv_paper;
        private TextView tv_note;
        private TextView tv_create_time;
        private TextView tv_page;

        public NotesHolder(View itemView) {
            super(itemView);
            rl_content = (RelativeLayout) itemView.findViewById(R.id.rl_content);
            tv_notes_name = (TextView) itemView.findViewById(R.id.tv_notes_name);
            tv_paper = (TextView) itemView.findViewById(R.id.tv_paper);
            tv_note = (TextView) itemView.findViewById(R.id.tv_note);
            tv_page=(TextView) itemView.findViewById(R.id.tv_page);
            tv_create_time = (TextView) itemView.findViewById(R.id.tv_create_time);
        }
    }

    class EmptyHolder extends RecyclerView.ViewHolder {
        public EmptyHolder(View itemView) {
            super(itemView);
        }
    }
}
