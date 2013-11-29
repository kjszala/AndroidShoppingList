package com.example.shoppinglist;

import java.util.List;

import com.example.shoppinglist.sqlite.model.BarCodeDataBase;
import com.example.shoppinlist.sqlite.helper.DatabaseHelper;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class AddNewBarCode extends Activity {
	DatabaseHelper db;
	String scanContent = null;
	String name = null;
	EditText mEdit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_bar_code);
		// Show the Up button in the action bar.
		setupActionBar();
		db = new DatabaseHelper(this);
	    mEdit   = (EditText)findViewById(R.id.barCodeNameField);
		
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
		getMenuInflater().inflate(R.menu.add_new_bar_code, menu);
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
	
	public void scanForBarCode(View view){
		if(view.getId()==R.id.scan_button){
			IntentIntegrator scanIntegrator = new IntentIntegrator(this);
			scanIntegrator.initiateScan();
			}
	}
	
	public void saveButtonClick(View view){
		name = mEdit.getText().toString();
		if (name!=null && scanContent !=null){
			List<BarCodeDataBase> barCodeDB = db.getAllBarCode();
			boolean existBarCode = false;
			for(BarCodeDataBase bd: barCodeDB){
				if(bd.getBarCode()==scanContent){
					existBarCode = true;
					Toast toast = Toast.makeText(getApplicationContext(), "Bar code already exist", Toast.LENGTH_SHORT);
				    toast.show();
					break;
				}
			}
			if(existBarCode == false){
			db.addBarCode(new BarCodeDataBase(name, scanContent));
			}
		}
		NavUtils.navigateUpFromSameTask(this);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if (scanningResult != null) {
			scanContent = scanningResult.getContents();
			}
		
		else{
		    Toast toast = Toast.makeText(getApplicationContext(),
		        "No scan data received!", Toast.LENGTH_SHORT);
		    toast.show();
		}
		}

}
