package popularmoviesstage1.legalimpurity.com.popularmoviesstage2.objects;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.contentprovider.MoviesContract;

/**
 * Created by root on 7/8/17.
 */

public class TrailerVideoObject implements Parcelable {

    private long Foreign_ApiID;
    private String Name;
    private String YoutubeKey;

    public TrailerVideoObject(long Foreign_ApiID, String name, String youtubeKey) {
        this.Foreign_ApiID = Foreign_ApiID;
        Name = name;
        YoutubeKey = youtubeKey;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getYoutubeKey() {
        return YoutubeKey;
    }

    public void setYoutubeKey(String youtubeKey) {
        YoutubeKey = youtubeKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.Name,
                this.YoutubeKey
        });
        dest.writeLongArray(new long[]{
                this.Foreign_ApiID
        });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public TrailerVideoObject createFromParcel(Parcel in) {
            return new TrailerVideoObject(in);
        }

        public TrailerVideoObject[] newArray(int size) {
            return new TrailerVideoObject[size];
        }
    };

    public TrailerVideoObject(Parcel in){
        String[] data = new String[3];
        in.readStringArray(data);
        this.Name = data[0];
        this.YoutubeKey = data[1];

        long[] data2 = new long[1];
        this.Foreign_ApiID = data2[0];
    }

    public TrailerVideoObject(Cursor mCursor){
        this.Foreign_ApiID = mCursor.getLong(MoviesContract.TRAILER_PROJECTION_INDEXES.COLUMN_FOREIGN_ID_POSITION);
        this.YoutubeKey = mCursor.getString(MoviesContract.TRAILER_PROJECTION_INDEXES.COLUMN_YOUTUBE_KEY_POSITION);
        this.Name = mCursor.getString(MoviesContract.TRAILER_PROJECTION_INDEXES.COLUMN_NAME_POSITION);
    }


    public ContentValues getContentValues() {
        ContentValues movieObjectValues = new ContentValues();
        movieObjectValues.put(MoviesContract.TrailerVideosEntry.COLUMN_FOREIGN_ID, Foreign_ApiID);
        movieObjectValues.put(MoviesContract.TrailerVideosEntry.COLUMN_YOUTUBE_KEY, YoutubeKey);
        movieObjectValues.put(MoviesContract.TrailerVideosEntry.COLUMN_NAME, Name);
        return movieObjectValues;
    }
}
