package popularmoviesstage1.legalimpurity.com.popularmoviesstage2.contentprovider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class MoviesContract {

    public static final String CONTENT_AUTHORITY = "popularmoviesstage1.legalimpurity.com.popularmoviesstage2.provider";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    /* Inner class that defines the table contents of the clients table */
    public static final class MoviesEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_ORIGNAL_TITLE = "orignal_title";
        public static final String COLUMN_POSTER_URL = "poster_url";
        public static final String COLUMN_PLOT_SYNOPSIS = "plot_synopsis";
        public static final String COLUMN_USER_RATING = "user_rating";
        public static final String COLUMN_RELEASE_DATE = "release_date";
    }

    public static final String PATH_REVIEWS = "reviews";

    public static final class ReviewEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWS).build();

        public static final String TABLE_NAME = "reviews";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_API_ID = "api_id";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
    }


    public static final String PATH_TRAILER_VIDEOS = "trailerVideos";

    public static final class TrailerVideosEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILER_VIDEOS).build();

        public static final String TABLE_NAME = "trailer_videos";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "trailer_name";
        public static final String COLUMN_YOUTUBE_KEY = "youtube_key";
    }
}
