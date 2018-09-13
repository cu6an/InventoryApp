package com.example.dell.bookbank.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.PublicKey;

import static com.example.dell.bookbank.data.BookContract.*;

public class BookDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "store.db";

    private static final int DATABASE_VERSION = 2;

    public BookDbHelper (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_BOOK_TABLE = "CREATE TABLE " + BookEntry.TABLE_NAME + "("
                + BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BookEntry.COLUMN_PRODUCT_NAME + "TEXT NOT NULL, "
                + BookEntry.COLUMN_PRICE + " INTEGER NOT NULL DEFAULT 0, "
                + BookEntry.COLUMN_QUANTITY + " INTEGER NOT NULL, "
                + BookEntry.COLUMN_SUPPLIER_NAME + "TEXT NOT NULL, "
                + BookEntry.COLUMN_SUPPLIER_CONTACT + " TEXT NOT NULL);";
        db.execSQL(CREATE_BOOK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
