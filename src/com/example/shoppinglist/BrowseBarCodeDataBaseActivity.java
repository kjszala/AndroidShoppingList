package com.example.shoppinglist;

import java.util.List;

import com.example.shoppinglist.sqlite.model.BarCodeDataBase;
import com.example.shoppinlist.sqlite.helper.DatabaseHelper;

import android.os.Bundle;
import android.app.ListActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class BrowseBarCodeDataBaseActivity extends ListActivity {
		DatabaseHelper db;
		private ListView listView1;
		List<BarCodeDataBase> barCodeDataForList;
    
	@Override
	protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse_bar_code_data_base);
		BarCodeDataBase barCodes1 = new BarCodeDataBase("nazwa1", "barcodes1");
		BarCodeDataBase barCodes2 = new BarCodeDataBase("nazwa2", "barcodes2");
		db.addBarCode(barCodes1);
		db.addBarCode(barCodes2);
		db = new DatabaseHelper(this);
		barCodeDataForList = db.getAllBarCode();
		// Reading all contacts
        Log.d("Reading: ", "Reading all contacts222222.."); 
		Adapter_bar_code_list adapter = new Adapter_bar_code_list(this,
                R.layout.bar_code_list_item, barCodeDataForList);
		// Reading all contacts
        Log.d("Reading: ", "Reading all contacts111111111.."); 
        listView1 =(ListView)findViewById(R.id.list);
 
        listView1.setAdapter(adapter);

		
	    
	}
	
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.browse_bar_code_data_base, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void changeAddNewBarCode(View view) {
	    Intent intent = new Intent(this, AddNewBarCode.class);
	    startActivity(intent);
	}
	
	@Override
	  protected void onPause() {
	    db.closeDB();
	    super.onPause();
	  }

	
	

}
