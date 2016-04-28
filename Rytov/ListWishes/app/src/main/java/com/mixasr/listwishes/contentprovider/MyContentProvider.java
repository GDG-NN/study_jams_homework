package com.mixasr.listwishes.contentprovider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.mixasr.listwishes.database.DBHelper;
import com.mixasr.listwishes.database.ListElementsTable;
import com.mixasr.listwishes.database.ListTable;

public class MyContentProvider extends ContentProvider {

    private DBHelper database;

    private static final String AUTHORITY = "com.mixasr.listwishes.contentprovider";

    private static final String PATH_LIST = "list";
    private static final String PATH_ELEMENT = "element";

    private static final int LISTS = 10;
    private static final int LIST_ID = 20;
    private static final int ELEMENTS = 30;
    private static final int ELEMENT_ID = 40;

    public static final Uri CONTENT_URI_LIST = Uri.parse("content://" + AUTHORITY
            + "/" + PATH_LIST);
    public static final Uri CONTENT_URI_ELEMENT = Uri.parse("content://" + AUTHORITY
            + "/" + PATH_ELEMENT);

    public static final String CONTENT_ITEM_TYPE_LIST = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/" + PATH_LIST;
    public static final String CONTENT_ITEM_TYPE_ELEMENT = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/" + PATH_ELEMENT;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, PATH_LIST, LISTS);
        sURIMatcher.addURI(AUTHORITY, PATH_LIST + "/#", LIST_ID);
        sURIMatcher.addURI(AUTHORITY, PATH_ELEMENT, ELEMENTS);
        sURIMatcher.addURI(AUTHORITY, PATH_ELEMENT + "/#", ELEMENT_ID);
    }

    @Override
    public boolean onCreate() {
        database = new DBHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case LISTS:
                queryBuilder.setTables(ListTable.TABLE_LIST);
                break;
            case LIST_ID:
                queryBuilder.setTables(ListTable.TABLE_LIST);
                queryBuilder.appendWhere(ListTable.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                break;
            case ELEMENTS:
                queryBuilder.setTables(ListElementsTable.TABLE_ELEMENT);
                break;
            case ELEMENT_ID:
                queryBuilder.setTables(ListElementsTable.TABLE_ELEMENT);
                queryBuilder.appendWhere(ListElementsTable.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case LISTS:
                id = sqlDB.insert(ListTable.TABLE_LIST, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return Uri.parse(PATH_LIST + "/" + id);
            case ELEMENTS:
                id = sqlDB.insert(ListElementsTable.TABLE_ELEMENT, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return Uri.parse(PATH_ELEMENT + "/" + id);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case LISTS:
                rowsDeleted = sqlDB.delete(ListTable.TABLE_LIST, selection,
                        selectionArgs);
                break;
            case LIST_ID:
                String id = uri.getLastPathSegment();
                sqlDB.beginTransaction();
                if (TextUtils.isEmpty(selection)) {
                    sqlDB.delete(ListElementsTable.TABLE_ELEMENT,
                            ListElementsTable.COLUMN_LIST_ID + "=" + id,
                            null);
                    rowsDeleted = sqlDB.delete(ListTable.TABLE_LIST,
                            ListTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    sqlDB.delete(ListElementsTable.TABLE_ELEMENT,
                            ListElementsTable.COLUMN_LIST_ID + "=" + id,
                            null);
                    rowsDeleted = sqlDB.delete(ListTable.TABLE_LIST,
                            ListTable.COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                sqlDB.setTransactionSuccessful();
                sqlDB.endTransaction();
                break;
            case ELEMENTS:
                rowsDeleted = sqlDB.delete(ListElementsTable.TABLE_ELEMENT, selection,
                        selectionArgs);
                break;
            case ELEMENT_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(ListElementsTable.TABLE_ELEMENT,
                            ListTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(ListElementsTable.TABLE_ELEMENT,
                            ListTable.COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case LISTS:
                rowsUpdated = sqlDB.update(ListTable.TABLE_LIST,
                        values,
                        selection,
                        selectionArgs);
                break;
            case LIST_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(ListTable.TABLE_LIST,
                            values,
                            ListTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(ListTable.TABLE_LIST,
                            values,
                            ListTable.COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            case ELEMENTS:
                rowsUpdated = sqlDB.update(ListElementsTable.TABLE_ELEMENT,
                        values,
                        selection,
                        selectionArgs);
                break;
            case ELEMENT_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(ListElementsTable.TABLE_ELEMENT,
                            values,
                            ListTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(ListElementsTable.TABLE_ELEMENT,
                            values,
                            ListTable.COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
