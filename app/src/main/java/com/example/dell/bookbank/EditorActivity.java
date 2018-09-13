package com.example.dell.bookbank;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.dell.bookbank.data.BookContract;
import com.example.dell.bookbank.data.BookDbHelper;

import static com.example.dell.bookbank.data.BookContract.*;

public class EditorActivity extends AppCompatActivity {

    private EditText pNameEditText;
    private EditText priceEditText;
    private EditText quantityEditText;
    private EditText supplierNameEditText;
    private EditText supplierContactEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        pNameEditText = (EditText) findViewById(R.id.product_name);
        priceEditText = (EditText) findViewById(R.id.price);
        quantityEditText = (EditText) findViewById(R.id.quantity);
        supplierNameEditText = (EditText) findViewById(R.id.supplier_name);
        supplierContactEditText = (EditText) findViewById(R.id.supplier_contact);
    }

    private void insertBook(){

        String pNameString = pNameEditText.getText().toString().trim();
        String priceString = priceEditText.getText().toString().trim();
        String quantityString = quantityEditText.getText().toString().trim();
        String supplierString = supplierNameEditText.getText().toString().trim();
        String supplierContactString = supplierContactEditText.getText().toString().trim();

        BookDbHelper bookHelper = new BookDbHelper(this);

        SQLiteDatabase db = bookHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_PRODUCT_NAME, pNameString);
        values.put(BookEntry.COLUMN_PRICE, priceString);
        values.put(BookEntry.COLUMN_QUANTITY, quantityString);
        values.put(BookEntry.COLUMN_SUPPLIER_NAME, supplierString);
        values.put(BookEntry.COLUMN_SUPPLIER_CONTACT, supplierContactString);

        db.insert(BookEntry.TABLE_NAME, null, values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                insertBook();
                NavUtils.navigateUpFromSameTask(this);
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
