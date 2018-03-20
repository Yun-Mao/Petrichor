package yunmao.com.petrichor.ui.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import yunmao.com.petrichor.R;
import yunmao.com.petrichor.ui.activity.MainActivity;

/**
 * Created by msi on 2018/3/15.
 */

public class BookReadFragment extends BaseFragment {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;//绑定Toolbar
    private View view;
    private GridView gview;
    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adapter;
    private Bundle bundle;
    private MydatabaseHelper mydatabaseHelper;
    private String nameStr;
    private TextView nameText;

    public static String NAME[]=new String[100];
    public static String URL[]=new String[100];
    //public static String[] NAME={"1","2"};
    private String bookname;
    private int i;

    public static BookReadFragment newInstance() {
        BookReadFragment fragment = new BookReadFragment();
        return fragment;
    }
    protected void initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //mRootView = inflater.inflate(R.layout.home_content_frag, container, false);
    }
    @Override
    protected void initData(boolean isSavedNull) {
        //不需要加载数据
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).setToolbar(mToolbar);
    }
    @Override
    protected void initEvents() {
        mToolbar.setTitle("书籍");
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_content_frag, container, false);

        //设置按钮事件
        FloatingActionButton addFileButton = (FloatingActionButton) view.findViewById(R.id.fab);
        addFileButton.setOnClickListener(new OpenFileAction());




        mydatabaseHelper = new MydatabaseHelper(view.getContext(),"book.db",null,1);
        mydatabaseHelper.getWritableDatabase();
        final SQLiteDatabase db = mydatabaseHelper.getWritableDatabase();
        Cursor cursor = db.query("localbook", null, null, null, null, null, null);
        //Log.d("MainActivity", "book name is " + cursor);
        if (cursor.moveToFirst()) {
            do {
                String buf= cursor.getString((cursor.getColumnIndex("bookname")));
                NAME[i] = buf;
                buf= cursor.getString((cursor.getColumnIndex("bookurl")));
                URL[i] = buf;
                //Log.d("MainActivity", "book name is " + buf);
                i++;
            } while (cursor.moveToNext());
        }
        cursor.close();


        refreshGridView();


        //设置GridView短按监听事件
        gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openFile(URL[position],NAME[position]);
            }
        });

        //设置GridView长按监听事件
        gview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("警告")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setMessage("确定删除")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                View mView = gview.getChildAt(position);
                                TextView text1 = (TextView) mView.findViewById(R.id.GVBookName);
                                nameStr = String.valueOf(text1.getText());
                                db.delete("localbook","bookname=?", new String[]{nameStr});
                                //Log.d("TAG", nameStr);
                                //String buf = NAME[(int) id];
                                //从原来数组删除对应值
                                NAME=removeBookName(NAME,nameStr);
                                //Log.d("TAG",NAME.toString());
                                //Toast.makeText(view.getContext(), buf,Toast.LENGTH_SHORT).show();
                                refreshGridView();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                return true;
            }
        });

        return view;
    }


    public List<Map<String, Object>> getData() throws UnsupportedEncodingException {
        for(int i=0;i<NAME.length;i++){
            if(NAME[i]==null)break;
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", R.mipmap.bookicon);
            map.put("text", NAME[i]);
            data_list.add(map);
        }

        return data_list;
    }

    //按钮动作:  窗口跳转 (Activity)  没有携带数据流的
    class OpenFileAction implements View.OnClickListener
    {
        public void onClick(View v) {
            Intent intentMain = new Intent(view.getContext(), ListAllFileActivity.class);
            startActivityForResult(intentMain, 0);
        }
    }

    //打开TXT文件
    public void openFile(String bookurl,String bookname){
        Intent intent = new Intent(view.getContext(), ViewFile.class);
        bundle = new Bundle();
        bundle.putString("fileName", bookurl);
        bundle.putString("bookname",bookname);
        intent.putExtras(bundle);
        startActivityForResult(intent, 0);
    }


    //GridView刷新界面
    public void refreshGridView(){
        gview = (GridView) view.findViewById(R.id.BookGV);
        //新建List
        data_list = new ArrayList<Map<String, Object>>();
        //获取数据
        try {
            getData();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String []from ={"text"};
        int []to = {R.id.GVBookName};
        sim_adapter = new SimpleAdapter(view.getContext(), data_list, R.layout.book_item, from, to);
        gview.setAdapter(sim_adapter);
    }

    //删除数组中的某一值
    public String[] removeBookName(String[] arr1, String arr2){
        List<String> list = new LinkedList<String>();
        for (String str : arr1) {                //处理第一个数组
            if (!list.contains(str)) {
                list.add(str);
            }
        }
        //如果第二个数组存在和第一个数组相同的值，就删除
        if(list.contains(arr2)){
            list.remove(arr2);
        }

        String[] result = {};   //创建空数组
        return list.toArray(result);    //List to Array
    }

    public String[] addBookName(String[] arr1, String arr2){
        List<String> list = new LinkedList<String>();
        for (String str : arr1) {                //处理第一个数组
            if (!list.contains(str)) {
                list.add(str);
            }
        }
        list.add(arr2);

        String[] result = {};   //创建空数组
        return list.toArray(result);    //List to Array
    }
}
