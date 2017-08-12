package popularmoviesstage1.legalimpurity.com.popularmoviesstage2.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;


public class MovieContentProvider extends ContentProvider {

    public static final int MOVIES = 100;
    public static final int MOVIE_WITH_ID = 101;

    public static final int REVIEWS = 200;
    public static final int REVIEW_WITH_ID = 201;

    public static final int TRAILERS = 300;
    public static final int TRAILER_WITH_ID = 301;


    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_MOVIES + "/#", MOVIE_WITH_ID);

        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_REVIEWS, REVIEWS);
        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_REVIEWS + "/#", REVIEW_WITH_ID);

        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_TRAILER_VIDEOS, TRAILERS);
        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_TRAILER_VIDEOS + "/#", TRAILER_WITH_ID);

        return uriMatcher;
    }

    private MovieDbHelper mMoviesDbHelper;

    @Override
    public boolean onCreate() {

        Context context = getContext();
        mMoviesDbHelper = new MovieDbHelper(context);
        return true;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;
        long id = -1;
        switch (match) {
            case MOVIES:
                id = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(MoviesContract.MoviesEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case REVIEWS:
                id = db.insert(MoviesContract.ReviewEntry.TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(MoviesContract.ReviewEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case TRAILERS:
                id = db.insert(MoviesContract.TrailerVideosEntry.TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(MoviesContract.TrailerVideosEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mMoviesDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case MOVIES:
                retCursor =  db.query(MoviesContract.MoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case REVIEWS:
                retCursor =  db.query(MoviesContract.ReviewEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case TRAILERS:
                retCursor =  db.query(MoviesContract.TrailerVideosEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int tasksDeleted;
        switch (match) {
            case MOVIES:
                tasksDeleted = db.delete(MoviesContract.MoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case REVIEWS:
                tasksDeleted = db.delete(MoviesContract.ReviewEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TRAILERS:
                tasksDeleted = db.delete(MoviesContract.TrailerVideosEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (tasksDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return tasksDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int tasksUpdated;
        int match = sUriMatcher.match(uri);
        String id;

        switch (match) {
            case MOVIE_WITH_ID:
                id = uri.getPathSegments().get(1);
                tasksUpdated = mMoviesDbHelper.getWritableDatabase().update(MoviesContract.MoviesEntry.TABLE_NAME, values, "_id=?", new String[]{id});
                break;
            case REVIEW_WITH_ID:
                id = uri.getPathSegments().get(1);
                tasksUpdated = mMoviesDbHelper.getWritableDatabase().update(MoviesContract.MoviesEntry.TABLE_NAME, values, "_id=?", new String[]{id});
                break;
            case TRAILER_WITH_ID:
                id = uri.getPathSegments().get(1);
                tasksUpdated = mMoviesDbHelper.getWritableDatabase().update(MoviesContract.MoviesEntry.TABLE_NAME, values, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (tasksUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return tasksUpdated;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIES:
                return "vnd.android.cursor.dir" + "/" + MoviesContract.CONTENT_AUTHORITY + "/" + MoviesContract.PATH_MOVIES;
            case MOVIE_WITH_ID:
                return "vnd.android.cursor.item" + "/" + MoviesContract.CONTENT_AUTHORITY + "/" + MoviesContract.PATH_MOVIES;
            case REVIEWS:
                return "vnd.android.cursor.dir" + "/" + MoviesContract.CONTENT_AUTHORITY + "/" + MoviesContract.PATH_REVIEWS;
            case REVIEW_WITH_ID:
                return "vnd.android.cursor.item" + "/" + MoviesContract.CONTENT_AUTHORITY + "/" + MoviesContract.PATH_REVIEWS;
            case TRAILERS:
                return "vnd.android.cursor.dir" + "/" + MoviesContract.CONTENT_AUTHORITY + "/" + MoviesContract.PATH_TRAILER_VIDEOS;
            case TRAILER_WITH_ID:
                return "vnd.android.cursor.item" + "/" + MoviesContract.CONTENT_AUTHORITY + "/" + MoviesContract.PATH_TRAILER_VIDEOS;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
