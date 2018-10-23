package com.example.dell.bookbank.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.dell.bookbank.R;
import com.example.dell.bookbank.data.BookContract.BookEntry;

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
         * integer code stored in the constant BOOKS_ID. This URI is used to provide access to ONE single row
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

        /*Get readable database since we only intend to read, and not insert/update or delete*/
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        /*This cursor will hold the result of the query*/
        Cursor cursor;

        /*Test if the URI matcher can match the URI to a specific code, given
        * the URI path*/
        int match = sUriMatcher.match(uri);

        switch (match) {
            case BOOKS:
                /*For the BOOKS code, query the books table directly with the given
                projection, selection, selection arguments, and sort order. The cursor
                could contain multiple rows of the books table.*/
                cursor = database.query(BookEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case BOOKS_ID:
                /*For the BOOKS_ID code, extract out the ID from the URI.
                For an example URI such as "content://com.example.dell.bookbank/books/2",
                the selection will be "_id=?" and the selection argument will be a
                String array containing the actual ID of 2 in this case.
                For every "?" in the selection, we need to have an element in the selection
                arguments that will fill in the "?". Since we have 1 question mark in the
                selection, we have 1 String in the selection arguments' String array.*/
                selection = BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                /*This will perform a query on the books table where the _id equals 2 to return a
                Cursor containing that row of the table.*/
                cursor = database.query(BookEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        /*Set notification URI on the Cursor,
        so we know what content URI the Cursor was created for.
        If the data at this URI changes, then we know we need to update the Cursor.*/
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        /*Return the cursor*/
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return BookEntry.CONTENT_LIST_TYPE;
            case BOOKS_ID:
                return BookEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    /*Insert new values into the provider from the content values*/
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        /*We need a single type for the URI since we are inserting data into
        * the entire books table*/
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return insertBook(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        // Get write-able database, since delete action makes changes to the database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // This variable Tracks the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(BookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case BOOKS_ID:
                // Delete a single row given by the ID in the URI
                selection = BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(BookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Delete not supported" + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return updateBook(uri, values, selection, selectionArgs);
            case BOOKS_ID:
                // For the BOOKS_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateBook(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private Uri insertBook(Uri uri, ContentValues values) {

        /*Constraints state that product name cant be null*/
        String productName = values.getAsString(BookEntry.COLUMN_PRODUCT_NAME);
        if (productName == null) {
            throw new IllegalArgumentException("Please enter a Book name");
        }

        /*Book must have a price*/
        Double price = values.getAsDouble(BookEntry.COLUMN_PRICE);
        if (price == null) {
            throw new IllegalArgumentException("Please enter a Book price");
        }

        /*The number of books available in stock cannot be null*/
        Integer quantity = values.getAsInteger(BookEntry.COLUMN_QUANTITY);
        if (quantity == null) {
            throw new IllegalArgumentException("Please enter quantity of books");
        }

        /*Check that the supplier name is not null*/
        String supplierName = values.getAsString(BookEntry.COLUMN_SUPPLIER_NAME);
        if (supplierName == null) {
            throw new IllegalArgumentException("Supplier name is required");
        }

        /* Check that the supplier phone number is not null*/
        String supplierPhoneNumber = values.getAsString(BookEntry.COLUMN_SUPPLIER_CONTACT);
        if (supplierPhoneNumber == null) {
            throw new IllegalArgumentException("Book requires a supplier contact");
        }

        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new book with the given values
        long id = database.insert(BookEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the book content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Update books in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more books).
     * Return the number of rows that were successfully updated.
     */
    private int updateBook(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        // If the COLUMN_PRODUCT_NAME key is present,
        // check that the product name value is not null.
        if (values.containsKey(BookEntry.COLUMN_PRODUCT_NAME)) {
            String productName = values.getAsString(BookEntry.COLUMN_PRODUCT_NAME);
            if (productName == null) {
                throw new IllegalArgumentException("Book requires a name");
            }
        }

        // If the COLUMN_PRICE key is present,
        // check that the price value is not null.
        if (values.containsKey(BookEntry.COLUMN_PRICE)) {
            Double price = values.getAsDouble(BookEntry.COLUMN_PRICE);
            if (price == null) {
                throw new IllegalArgumentException("Book requires a price");
            }
        }

        // If the COLUMN_QUANTITY key is present,
        // check that the quantity value is not null.
        if (values.containsKey(BookEntry.COLUMN_QUANTITY)) {
            Integer quantity = values.getAsInteger(BookEntry.COLUMN_QUANTITY);
            if (quantity == null) {
                throw new IllegalArgumentException("Book requires a quantity");
            }
        }

        // If the COLUMN_SUPPLIER_NAME key is present,
        // check that the supplier name value is not null.
        if (values.containsKey(BookEntry.COLUMN_SUPPLIER_NAME)) {
            String supplierName = values.getAsString(BookEntry.COLUMN_SUPPLIER_NAME);
            if (supplierName == null) {
                throw new IllegalArgumentException("Book requires a supplier name");
            }
        }

        // If the COLUMN_SUPPLIER_CONTACT key is present,
        // check that the supplier phone number value is not null.
        if (values.containsKey(BookEntry.COLUMN_SUPPLIER_CONTACT)) {
            String supplierPhoneNumber = values.getAsString(BookEntry.COLUMN_SUPPLIER_CONTACT);
            if (supplierPhoneNumber == null) {
                throw new IllegalArgumentException("Book requires a supplier phone number");
            }
        }

        // If there are no values to update, then don't update the database
        if (values.size() == 0) {
            return 0;
        }

        /*Otherwise get writable database to update the data*/
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(BookEntry.TABLE_NAME, values, selection, selectionArgs);

        /* If 1 or more rows were updated, then notify all listeners that the data at the
         given URI has changed*/
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }
}
