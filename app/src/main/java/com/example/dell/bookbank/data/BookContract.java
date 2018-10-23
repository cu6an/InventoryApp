/*Icons from icons8.com*/

package com.example.dell.bookbank.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class BookContract {

    /**
     * The "Content authority" represents the entire content provider, similar to the
     * relationship between a domain name and its website. We use the package name for the app to represent
     * Content authority. Package name is guaranteed to be unique on the device
     */
    public static final String CONTENT_AUTHORITY = "com.example.dell.bookbank";

    /*The base URI is created from the content authority, it is what apps will use to contact
     * the content provider*/
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /*Possible path (appended to base content URI for possible URI's). In this case we use the database name
     * as that will guarantee data for the database that already exists*/
    public static final String PATH_BOOKS = "books";

    /*The constructor of the Book contract left blank, so its not used anywhere*/
    private BookContract() {
    }

    /*Class BookEntry with the database column-header names*/
    public static final class BookEntry implements BaseColumns {

        /**
         * The content URI to access the book data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of books.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single book.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        public final static String TABLE_NAME = "books";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_PRODUCT_NAME = "Product_Name";

        public final static String COLUMN_PRICE = "Price";

        public final static String COLUMN_QUANTITY = "Quantity";

        public final static String COLUMN_SUPPLIER_NAME = "Supplier_Name";

        public final static String COLUMN_SUPPLIER_CONTACT = "Supplier_Contact";

    }
}
