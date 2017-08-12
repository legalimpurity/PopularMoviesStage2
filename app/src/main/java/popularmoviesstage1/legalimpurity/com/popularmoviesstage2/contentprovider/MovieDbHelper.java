package popularmoviesstage1.legalimpurity.com.popularmoviesstage2.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by root on 7/8/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    // The name of the database
    private static final String DATABASE_NAME = "moviesDb.db";

    // If you change the database schema, you must increment the database version
    private static final int VERSION = 1;


    // Constructor
    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    /**
     * Called when the tasks database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_TABLE_MOVIES = "CREATE TABLE "  + MoviesContract.MoviesEntry.TABLE_NAME + " (" +
                MoviesContract.MoviesEntry.COLUMN_API_ID + " INTEGER PRIMARY KEY NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_ORIGNAL_TITLE + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_POSTER_URL + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_PLOT_SYNOPSIS + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_USER_RATING + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL" +
                ");";

        final String CREATE_TABLE_REVIEWS = "CREATE TABLE "  + MoviesContract.ReviewEntry.TABLE_NAME + " (" +
                MoviesContract.ReviewEntry.COLUMN_API_ID + " INTEGER PRIMARY KEY NOT NULL, " +
                MoviesContract.ReviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                MoviesContract.ReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL" +
                ");";

        final String CREATE_TABLE_TRAILER_VIDEOS = "CREATE TABLE "  + MoviesContract.TrailerVideosEntry.TABLE_NAME + " (" +
                MoviesContract.TrailerVideosEntry.COLUMN_API_ID + " INTEGER PRIMARY KEY NOT NULL, " +
                MoviesContract.TrailerVideosEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                MoviesContract.TrailerVideosEntry.COLUMN_YOUTUBE_KEY + " TEXT NOT NULL" +
                ");";

        db.execSQL(CREATE_TABLE_MOVIES);
        db.execSQL(CREATE_TABLE_REVIEWS);
        db.execSQL(CREATE_TABLE_TRAILER_VIDEOS);
    }


    /**
     * This method discards the old table of data and calls onCreate to recreate a new one.
     * This only occurs when the version number for this database (DATABASE_VERSION) is incremented.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MoviesEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.ReviewEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.TrailerVideosEntry.TABLE_NAME);
        onCreate(db);
    }
}
