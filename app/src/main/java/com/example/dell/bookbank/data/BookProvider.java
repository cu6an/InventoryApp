package com.example.dell.bookbank.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class BookProvider extends ContentProvider {

    /*Will be used to log messages*/
    public static final String LOG_TAG = BookProvider.class.getSimpleName();

    /*The URI matcher used for content URI for  whole books table*/
    private static final int BOOKS = 100;

    /*The URI matcher code used for content URI for a single book from the entire books table*/
    private static final int BOOKS_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    /*Static initializer. This is run the first time anything is called from this class.*/
    static {

        /*The calls to addURI() go here, for all of the content URI patterns that the provider
        should recognize. All paths added to the UriMatcher have a corresponding code to return
        when a match is found.*/
        sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_BOOKS, BOOKS);

        /* The content URI of the form "content://com.example.dell.bookbank/books/#" will map to the
         * integer code stored in the constant BOOK_ID. This URI is used to provide access to ONE single row
         * of the books table.
         * # is a wild card, more like a place holder, that can be substituted for an
         * integer
         */
        sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_BOOKS + "/#", BOOKS_ID);

    }

    /*DB helper object*/
    private BookDbHelper mDbHelper;

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        mDbHelper = new BookDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
