package yunmao.com.petrichor.ui.fragment;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import yunmao.com.petrichor.ui.activity.MainActivity;


public class ListAllFileActivity extends ListActivity {
	
	
	private List<File> fileList;
	private Bundle bundle;
	private String fileNameKey = "fileName";
	private String nameString;
	private MydatabaseHelper mydatabaseHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		File path = android.os.Environment.getExternalStorageDirectory();
		File[] f = path.listFiles();
		
		fill(f);
	}

	private void fill(File[] files) {
		fileList = new ArrayList<File>();
		
		for (File file : files) {
			if (isValidFileOrDir(file)) {
				fileList.add(file);
			}
		}
		ArrayAdapter<String> fileNameList = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1,
					fileToStrArr(fileList)  );

		setListAdapter(fileNameList);
	}
	
	private boolean isValidFileOrDir(File fileIn)
	{
		if (fileIn.isDirectory()) {
			return true;
		}
		else {
			String fileNameLow = fileIn.getName().toLowerCase();
			if (fileNameLow.endsWith(".txt")) {
				return true;
			}
		}
		return false;
	}

	private String[] fileToStrArr(List<File> fl)
	{
		ArrayList<String> fnList = new ArrayList<String>();
		for (int i = 0; i < fl.size(); i++) {
			nameString = fl.get(i).getName();
			//Log.d("TAG",nameString);
			fnList.add(nameString);
		}
		return fnList.toArray(new String[0]);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		BookReadFragment homeContentFragment = new BookReadFragment();

		File file = fileList.get(position);
		if (file.isDirectory())
		{
			File[] f = file.listFiles();
			fill(f);
		}
		else {
			mydatabaseHelper = new MydatabaseHelper(this,"book.db",null,1);
			SQLiteDatabase db = mydatabaseHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("bookname",nameString);
			values.put("bookurl",file.getAbsolutePath());
			db.insert("localbook",null,values);

			Intent intent = new Intent();
			intent.setClass(ListAllFileActivity.this, MainActivity.class);
			intent.putExtra("refresh",true);
			startActivity(intent);
		}
	}
	
}
