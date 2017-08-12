package popularmoviesstage1.legalimpurity.com.popularmoviesstage2.objects;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;

import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.contentprovider.MoviesContract;

/**
 * Created by rajatkhanna on 01/08/17.
 */

public class MovieObject implements Parcelable {


    private ArrayList <ReviewObject> ReviewObjs = new ArrayList <ReviewObject>();
    private ArrayList <TrailerVideoObject> TrailerVideoObjs = new ArrayList <TrailerVideoObject>();

    // Same as is from api, as they will always be Unique
    private long ApiId;
    private String OrignalTitle;
    private String MoviePosterImageThumbnailUrl;
    private String PlotSynopsis;
    private String UserRating;
    private String ReleaseDate;

    private boolean bookmarked;

    // To be used when creating object from api

    public MovieObject(long ApiId, String orignalTitle, String moviePosterImageThumbnailUrl, String plotSynopsis, String userRating, String releaseDate, boolean bookmarked) {
        this.ApiId = ApiId;
        this.OrignalTitle = orignalTitle;
        this.MoviePosterImageThumbnailUrl = moviePosterImageThumbnailUrl;
        this.PlotSynopsis = plotSynopsis;
        this.UserRating = userRating;
        this.ReleaseDate = releaseDate;
        this.bookmarked = bookmarked;
    }

    public boolean isBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

    public long getApiId() {
        return ApiId;
    }

    public void setApiId(long apiId) {
        this.ApiId = apiId;
    }

    public ArrayList<ReviewObject> getReviewObjs() {
        return ReviewObjs;
    }

    public void setReviewObjs(ArrayList<ReviewObject> reviewObjs) {
        this.ReviewObjs = reviewObjs;
    }

    public ArrayList<TrailerVideoObject> getTrailerVideoObjs() {
        return TrailerVideoObjs;
    }

    public void setTrailerVideoObjs(ArrayList<TrailerVideoObject> TrailerVideoObjs) {
        this.TrailerVideoObjs = TrailerVideoObjs;
    }

    public String getOrignalTitle() {
        return OrignalTitle;
    }

    public void setOrignalTitle(String orignalTitle) {
        this.OrignalTitle = orignalTitle;
    }

    public String getMoviePosterImageThumbnailUrl() {
        return MoviePosterImageThumbnailUrl;
    }

    public void setMoviePosterImageThumbnailUrl(String moviePosterImageThumbnailUrl) {
        this.MoviePosterImageThumbnailUrl = moviePosterImageThumbnailUrl;
    }

    public String getPlotSynopsis() {
        return PlotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.PlotSynopsis = plotSynopsis;
    }

    public String getUserRating() {
        return UserRating;
    }

    public void setUserRating(String userRating) {
        UserRating = userRating;
    }

    public String getReleaseDate() {
        return ReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        ReleaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.OrignalTitle,
                this.MoviePosterImageThumbnailUrl,
                this.PlotSynopsis,
                this.UserRating,
                this.ReleaseDate
        });

        dest.writeLongArray(new long[] {
                this.ApiId
        });

        dest.writeBooleanArray(new boolean[]{
                bookmarked
        });

        dest.writeTypedList(ReviewObjs);
        dest.writeTypedList(TrailerVideoObjs);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MovieObject createFromParcel(Parcel in) {
            return new MovieObject(in);
        }

        public MovieObject[] newArray(int size) {
            return new MovieObject[size];
        }
    };

    public MovieObject(Parcel in){
        String[] data = new String[5];

        in.readStringArray(data);
        this.OrignalTitle = data[0];
        this.MoviePosterImageThumbnailUrl = data[1];
        this.PlotSynopsis = data[2];
        this.UserRating = data[3];
        this.ReleaseDate = data[4];

        long[] data2 = new long[1];

        in.readLongArray(data2);
        ApiId = data2[0];

        boolean[] data3 = new boolean[1];
        in.readBooleanArray(data3);
        bookmarked = data3[0];

        ReviewObjs = new ArrayList<ReviewObject>();
        in.readTypedList(ReviewObjs, ReviewObject.CREATOR);
        in.readTypedList(TrailerVideoObjs, ReviewObject.CREATOR);
    }

    public MovieObject(Cursor mCursor){
        this.ApiId = mCursor.getLong(MoviesContract.MOVIES_PROJECTION_INDEXES.COLUMN_API_ID_POSITION);

        this.OrignalTitle = mCursor.getString(MoviesContract.MOVIES_PROJECTION_INDEXES.COLUMN_ORIGNAL_TITLE_POSITION);
        this.MoviePosterImageThumbnailUrl = mCursor.getString(MoviesContract.MOVIES_PROJECTION_INDEXES.COLUMN_POSTER_URL_POSITION);
        this.PlotSynopsis = mCursor.getString(MoviesContract.MOVIES_PROJECTION_INDEXES.COLUMN_PLOT_SYNOPSIS_POSITION);
        this.UserRating = mCursor.getString(MoviesContract.MOVIES_PROJECTION_INDEXES.COLUMN_USER_RATING_POSITION);
        this.ReleaseDate = mCursor.getString(MoviesContract.MOVIES_PROJECTION_INDEXES.COLUMN_USER_RATING_POSITION);
        // If object is coming from DB, it is obviosuly bookmarked
        this.bookmarked = true;
    }


    public ContentValues getContentValues() {
        ContentValues movieObjectValues = new ContentValues();
        movieObjectValues.put(MoviesContract.MoviesEntry.COLUMN_API_ID, ApiId);

        movieObjectValues.put(MoviesContract.MoviesEntry.COLUMN_ORIGNAL_TITLE, OrignalTitle);
        movieObjectValues.put(MoviesContract.MoviesEntry.COLUMN_PLOT_SYNOPSIS, PlotSynopsis);
        movieObjectValues.put(MoviesContract.MoviesEntry.COLUMN_POSTER_URL, MoviePosterImageThumbnailUrl);
        movieObjectValues.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE, ReleaseDate);
        movieObjectValues.put(MoviesContract.MoviesEntry.COLUMN_USER_RATING, UserRating);
        return movieObjectValues;
    }

}