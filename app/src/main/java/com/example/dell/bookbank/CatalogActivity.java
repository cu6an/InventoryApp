/*Icons from icons8.com*/

package com.example.dell.bookbank;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import com.example.dell.bookbank.data.BookContract.BookEntry;
import com.example.dell.bookbank.data.BookDbHelper;

public class CatalogActivity extends AppCompatActivity {

    /*Instantiate an object from the booDbHelper class*/
    private BookDbHelper bookHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*Floating action bar opens Editor Activity when clicked.
         * this action is triggered by an intent
         **/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        bookHelper = new BookDbHelper(this);
    }

    public void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    /*displayDatabaseInfo method definition*/
    private void displayDatabaseInfo() {

        SQLiteDatabase db = bookHelper.getReadableDatabase();

        /*Set the projection to have the columns we wish to get from the database*/
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_PRODUCT_NAME,
                BookEntry.COLUMN_PRICE,
                BookEntry.COLUMN_QUANTITY,
                BookEntry.COLUMN_SUPPLIER_NAME,
                BookEntry.COLUMN_SUPPLIER_CONTACT,
        };

        /*Use the cursor to query the database*/
        Cursor cursor = db.query(
                BookEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        TextView displayView = (TextView) findViewById(R.id.text_view_book);

        try {
            /*Create a header and also display the contents of the columns names that we
             * entered with projection on the text view*/
            displayView.setText("The books table contains " + cursor.getCount() + " book(s).\n\n");
            displayView.append(BookEntry._ID + "\t" +
                    BookEntry.COLUMN_PRODUCT_NAME + "\t" +
                    BookEntry.COLUMN_PRICE + "\t" +
                    BookEntry.COLUMN_QUANTITY + "\t" +
                    BookEntry.COLUMN_SUPPLIER_NAME + "\t" +
                    BookEntry.COLUMN_SUPPLIER_CONTACT + "\t");

            /*Get the index positions of the database using a cursor*/
            int idColumnIndex = cursor.getColumnIndex(BookEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_QUANTITY);
            int sNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_SUPPLIER_NAME);
            int sContactColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_SUPPLIER_CONTACT);

            /*We loop through database rows for as long as move next is true.
             * We start by calling moveToNext() so that we do not read the
             * column headers that we already returned*/
            while (cursor.moveToNext()) {
                /*Extract the data values of columns using the index values we got earlier using the cursor
                 * */
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSName = cursor.getString(sNameColumnIndex);
                String currentSPName = cursor.getString(sContactColumnIndex);

                /*We append the results the the text view*/
                displayView.append(("\n\n" + currentID + " \t " +
                        currentName + " \t " +
                        currentPrice + " \t " +
                        currentQuantity + " \t " +
                        currentSName + " \t " +
                        currentSPName));
            }
        } finally {
            /*Close the cursor when its not in use, so as to free up resources*/
            cursor.close();
        }
    }
}
