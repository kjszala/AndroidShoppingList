package com.example.shoppinlist.sqlite.helper;

import java.util.ArrayList;
import java.util.List;

import com.example.shoppinglist.sqlite.model.BarCodeDataBase;
import com.example.shoppinglist.sqlite.model.ListDataBase;
import com.example.shoppinglist.sqlite.model.ProductDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	// Logcat tag
    private static final String LOG = "DatabaseHelper";
 
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "shoppinListManager";
 
    // Table Names
    private static final String TABLE_LIST = "lists";
    private static final String TABLE_BAR_CODE = "barCodes";
    private static final String TABLE_PRODUCT = "products";
 
    // Column Names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_AMOUNT= "amount";
    private static final String KEY_BARCODESTRING="barCode";
    private static final String KEY_LIST_ID = "iDList";
    private static final String KEY_BARCODE_ID = "iDBarCode";
 
    // Table Create Statements
    // List table create statement
    private static final String CREATE_TABLE_LIST = "CREATE TABLE "
            + TABLE_LIST 
            + "(" 
    		+ KEY_ID + " INTEGER PRIMARY KEY," 
    		+ KEY_NAME + " TEXT" 
    		+ ")";
 
    // BarCode table create statement
    private static final String CREATE_TABLE_BAR_CODE = "CREATE TABLE " 
    		+ TABLE_BAR_CODE
    		+ "(" 
    		+ KEY_ID + " INTEGER PRIMARY KEY," 
    		+ KEY_NAME + " TEXT,"
            + KEY_BARCODESTRING + " TEXT" 
    		+ ")";
 
    // Product table create statement
    private static final String CREATE_TABLE_PRODUCT = "CREATE TABLE "
            + TABLE_PRODUCT + "(" 
    		+ KEY_ID + " INTEGER PRIMARY KEY," 
            + KEY_AMOUNT + " INTEGER,"
            + KEY_LIST_ID + " INTEGER," 
            + KEY_BARCODE_ID + " INTEGER" +")";
 
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
 
        // creating required tables
        db.execSQL(CREATE_TABLE_LIST);
        db.execSQL(CREATE_TABLE_BAR_CODE);
        db.execSQL(CREATE_TABLE_PRODUCT);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BAR_CODE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
 
        // create new tables
        onCreate(db);
    }
    
    /*
     * Creating a list
     */
    public long createList(ListDataBase lists) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, lists.getName());
     
        // insert row
        long list_id = db.insert(TABLE_LIST, null, values);
   
        return list_id;
    }
    
    /*
     * Creating a barCodes
     */
    public long createBarCode(BarCodeDataBase barCodes) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, barCodes.getName());
        values.put(KEY_BARCODESTRING, barCodes.getBarCode());
     
        // insert row
        long barCodes_id = db.insert(TABLE_BAR_CODE, null, values);
   
        return barCodes_id;
    }
 
    /*
     * Creating a product
     */
    public long createProduct(ProductDataBase products, long barCode_id, long list_id) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_AMOUNT, products.getAmount());
        values.put(KEY_BARCODE_ID, barCode_id);
        values.put(KEY_LIST_ID, list_id);
     
        // insert row
        long product_id = db.insert(TABLE_PRODUCT, null, values);
   
        return product_id;
    }

    
    /*
     * To operate on list!
     */
      
    /*
     * Get single list
     */
    public ListDataBase getList(long list_id) {
        SQLiteDatabase db = this.getReadableDatabase();
     
        String selectQuery = "SELECT  * FROM " + TABLE_LIST + " WHERE "
                + KEY_ID + " = " + list_id;
     
        Log.e(LOG, selectQuery);
     
        Cursor c = db.rawQuery(selectQuery, null);
     
        if (c != null)
            c.moveToFirst();
     
        ListDataBase ldb = new ListDataBase();
        ldb.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        ldb.setName((c.getString(c.getColumnIndex(KEY_NAME))));
     
        return ldb;
    }
    
    /*
     * Getting all lists
     * */
    public List<ListDataBase> getAllList() {
        List<ListDataBase> lists = new ArrayList<ListDataBase>();
        String selectQuery = "SELECT  * FROM " + TABLE_LIST;
     
        Log.e(LOG, selectQuery);
     
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                ListDataBase ldb = new ListDataBase();
                ldb.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                ldb.setName((c.getString(c.getColumnIndex(KEY_NAME))));
     
                // adding to listshopping list
                lists.add(ldb);
            } while (c.moveToNext());
        }
     
        return lists;
    }
    
    /*
     * Updating a list
     */
    public int updateList(ListDataBase list) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, list.getName());
     
        // updating row
        return db.update(TABLE_LIST, values, KEY_ID + " = ?",
                new String[] { String.valueOf(list.getId()) });
    }

    /*
     *  Deleting a list
     */    
    public void deleteList(ListDataBase listToDelete){
    	SQLiteDatabase db = this.getWritableDatabase();       
        List<ProductDataBase> allProductFromList = getAllProduct(listToDelete.getId());
     
            // delete all products
            for (ProductDataBase product : allProductFromList) {
                // delete products
                deleteProduct(product.getId());
            }
        
     
        // now delete the bar code
        db.delete(TABLE_LIST, KEY_ID + " = ?",
                new String[] { String.valueOf(listToDelete.getId()) });
    	
    }

    /*
     * Add new list
     */
    public void addList(ListDataBase list) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, list.getName()); // Contact Name
 
        // Inserting Row
        db.insert(TABLE_LIST, null, values);
        db.close();
    }
    
    
    /*
     * to operate on bar code!
     */
        
    /*
     * get single bar code
     */
    public BarCodeDataBase getBarCode(long barCode_id) {
        SQLiteDatabase db = this.getReadableDatabase();
     
        String selectQuery = "SELECT  * FROM " + TABLE_BAR_CODE + " WHERE "
                + KEY_ID + " = " + barCode_id;
     
        Log.e(LOG, selectQuery);
     
        Cursor c = db.rawQuery(selectQuery, null);
     
        if (c != null)
            c.moveToFirst();
     
        BarCodeDataBase bcdb = new BarCodeDataBase();
        bcdb.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        bcdb.setName((c.getString(c.getColumnIndex(KEY_NAME))));
        bcdb.setBarCode(c.getString(c.getColumnIndex(KEY_BARCODESTRING)));
     
        return bcdb;
    }
    
    /*
     * getting all bar codes
     */
    public List<BarCodeDataBase> getAllBarCode() {
        List<BarCodeDataBase> barCodes = new ArrayList<BarCodeDataBase>();
        String selectQuery = "SELECT  * FROM " + TABLE_BAR_CODE;
     
        Log.e(LOG, selectQuery);
     
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                BarCodeDataBase bcdb = new BarCodeDataBase();
                bcdb.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                bcdb.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                bcdb.setBarCode(c.getString(c.getColumnIndex(KEY_BARCODESTRING)));
                // adding to barcodeshoppinglist list
                barCodes.add(bcdb);
            } while (c.moveToNext());
        }
     
        return barCodes;
    }
    
    /*
     * Updating a bar code
     */
    public int updateBarCode(BarCodeDataBase barCodes) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, barCodes.getName());
        values.put(KEY_BARCODESTRING, barCodes.getBarCode());
     
        // updating row
        return db.update(TABLE_BAR_CODE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(barCodes.getId()) });
    }

    /*
     *  Deleting a bar code
     */
    public void deleteBarCode(BarCodeDataBase barCodes){
    	SQLiteDatabase db = this.getWritableDatabase();
    	 
        
        List<ProductDataBase> allProductWithThisBarCode = getAllProductWithThisBarCode(barCodes.getId());
     
            // delete all products
            for (ProductDataBase product : allProductWithThisBarCode) {
                // delete products
                deleteProduct(product.getId());
            }
        
     
        // now delete the bar code
        db.delete(TABLE_BAR_CODE, KEY_ID + " = ?",
                new String[] { String.valueOf(barCodes.getId()) });
    }

    /*
     *  Add new bar code
     */
    public void addBarCode(BarCodeDataBase barCodes) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, barCodes.getName()); 
        values.put(KEY_BARCODESTRING, barCodes.getBarCode()); 
         
        db.insert(TABLE_BAR_CODE, null, values);
        db.close(); 
    }
    
    
    /*
     * to operate on product
     */
    
    /*
     * Get single product
     */
    public ProductDataBase getProduct(long product_id) {
        SQLiteDatabase db = this.getReadableDatabase();
     
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT + " WHERE "
                + KEY_ID + " = " + product_id;
     
        Log.e(LOG, selectQuery);
     
        Cursor c = db.rawQuery(selectQuery, null);
     
        if (c != null)
            c.moveToFirst();
     
        ProductDataBase pdb = new ProductDataBase();
        pdb.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        pdb.setAmount((c.getInt(c.getColumnIndex(KEY_AMOUNT))));
     
        return pdb;
    }
    
    /*
     * Get all product with this bar codes
     */
    public List<ProductDataBase> getAllProductWithThisBarCode(long barcode_id){
    	List<ProductDataBase> products = new ArrayList<ProductDataBase>();
    	String selectQuery = "SELECT * FROM " + TABLE_PRODUCT + " tp "
    						+ " WHERE" 
    						+ "tp." + KEY_BARCODE_ID + " = " + barcode_id ;
    	
    	Log.e(LOG, selectQuery);
    	
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor c = db.rawQuery(selectQuery, null);
    	
    	if(c.moveToFirst()) {
    		do{
    			ProductDataBase pdb = new ProductDataBase();
    			pdb.setId(c.getInt(c.getColumnIndex(KEY_ID)));
    			pdb.setAmount(c.getInt(c.getColumnIndex(KEY_AMOUNT)));
    			
    			products.add(pdb);
    		}while(c.moveToNext());
    		
    	}
    	return products;
    }
    
    /*
     * Getting all products from selected list
     * */
    public List<ProductDataBase> getAllProduct(long list_id) {
        List<ProductDataBase> products = new ArrayList<ProductDataBase>();
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT + " tp " 
        					+ " WHERE " 
        					+ " tp." + KEY_LIST_ID + " = " + list_id ;
     
        Log.e(LOG, selectQuery);
     
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                ProductDataBase pdb = new ProductDataBase();
                pdb.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                pdb.setAmount((c.getInt(c.getColumnIndex(KEY_AMOUNT))));
     
                // adding to listshopping list
                products.add(pdb);
            } while (c.moveToNext());
        }
     
        return products;
    }
    
    /*
     * Updating a product amount
     */
    public int updateProduct(ProductDataBase product) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_AMOUNT, product.getAmount());
     
        // updating row
        return db.update(TABLE_PRODUCT, values, KEY_ID + " = ?",
                new String[] { String.valueOf(product.getId()) });
    }

    /*
     *  Deleting a product
     */    
    public void deleteProduct(long product_id){
    	SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCT, KEY_ID + " = ?",
                new String[] { String.valueOf(product_id) });
    	
    }

    /*
     * add new product
     */
    public void addProduct(ProductDataBase product, long list_id, long barCode_id){
    	SQLiteDatabase db = this.getWritableDatabase();
    	 
        ContentValues values = new ContentValues();
        values.put(KEY_AMOUNT, product.getAmount()); 
        values.put(KEY_BARCODE_ID, barCode_id); 
        values.put(KEY_LIST_ID, list_id);
         
        db.insert(TABLE_PRODUCT, null, values);
        db.close(); 
    }
    
 /*
  	* Closing Database connection
  */
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
    
    
}
