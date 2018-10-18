/*Icons from icons8.com*/

package com.example.dell.bookbank.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static com.example.dell.bookbank.data.BookContract.*;

public class BookDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "store.db";

    private static final int DATABASE_VERSION = 1;

    public BookDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*Override the methods from SQLiteOpenHelper*/
    @Override
    public void onCreate(SQLiteDatabase db) {

        /*String variable CREATE_BOOK_TABLE to have our string for creating the database*/
        String CREATE_BOOK_TABLE = "CREATE TABLE IF NOT EXISTS " + BookEntry.TABLE_NAME + " ("
                + BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BookEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + BookEntry.COLUMN_PRICE + " INTEGER NOT NULL DEFAULT 0, "
                + BookEntry.COLUMN_QUANTITY + " INTEGER NOT NULL, "
                + BookEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, "
                + BookEntry.COLUMN_SUPPLIER_CONTACT + " TEXT NOT NULL); ";
        db.execSQL(CREATE_BOOK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
