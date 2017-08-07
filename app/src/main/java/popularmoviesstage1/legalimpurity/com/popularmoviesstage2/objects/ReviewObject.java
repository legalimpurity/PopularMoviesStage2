package popularmoviesstage1.legalimpurity.com.popularmoviesstage2.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 7/8/17.
 */

public class ReviewObject implements Parcelable {
    private long _id;

    private long ApiID;
    private String Author;
    private String Content;

    public ReviewObject(long _id, long apiID, String author, String content) {
        this._id = _id;
        ApiID = apiID;
        Author = author;
        Content = content;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long getApiID() {
        return ApiID;
    }

    public void setApiID(long apiID) {
        ApiID = apiID;
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
        dest.writeLongArray(new long[]{
                this._id,
                this.ApiID
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
        String[] data = new String[2];
        in.readStringArray(data);
        this.Author = data[0];
        this.Content = data[1];

        long[] data2 = new long[2];
        in.readLongArray(data2);
        this._id = data2[0];
        this.ApiID = data2[1];
    }
}
