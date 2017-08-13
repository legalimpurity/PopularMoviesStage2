package popularmoviesstage1.legalimpurity.com.popularmoviesstage2.objects;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.contentprovider.MoviesContract;

/**
 * Created by root on 7/8/17.
 */

public class ReviewObject implements Parcelable {
    private long Foreign_ApiID;
    private String Author;
    private String Content;

    public ReviewObject(long Foreign_ApiID, String author, String content) {
        this.Foreign_ApiID = Foreign_ApiID;
        this.Author = author;
        this.Content = content;
    }

    public long getForeign_ApiID() {
        return Foreign_ApiID;
    }

    public void setForeign_ApiID(long foreign_ApiID) {
        Foreign_ApiID = foreign_ApiID;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.Author,
                this.Content
        });
        dest.writeLongArray(new long[] {
                this.Foreign_ApiID
        });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ReviewObject createFromParcel(Parcel in) {
            return new ReviewObject(in);
        }

        public ReviewObject[] newArray(int size) {
            return new ReviewObject[size];
        }
    };

    public ReviewObject(Parcel in){
        String[] data = new String[3];
        in.readStringArray(data);
        this.Author = data[0];
        this.Content = data[1];

        long[] data2 = new long[1];
        in.readLongArray(data2);
        this.Foreign_ApiID = data2[0];
    }

    public ReviewObject(Cursor mCursor){
        this.Foreign_ApiID = mCursor.getLong(MoviesContract.REVIEWS_PROJECTION_INDEXES.COLUMN_FOREIGN_ID_POSITION);
        this.Content = mCursor.getString(MoviesContract.REVIEWS_PROJECTION_INDEXES.COLUMN_CONTENT_POSITION);
        this.Author = mCursor.getString(MoviesContract.REVIEWS_PROJECTION_INDEXES.COLUMN_AUTHOR_POSITION);
    }


    public ContentValues getContentValues() {
        ContentValues movieObjectValues = new ContentValues();
        movieObjectValues.put(MoviesContract.ReviewEntry.COLUMN_FOREIGN_ID, Foreign_ApiID);
        movieObjectValues.put(MoviesContract.ReviewEntry.COLUMN_AUTHOR, Author);
        movieObjectValues.put(MoviesContract.ReviewEntry.COLUMN_CONTENT, Content);
        return movieObjectValues;
    }

}
