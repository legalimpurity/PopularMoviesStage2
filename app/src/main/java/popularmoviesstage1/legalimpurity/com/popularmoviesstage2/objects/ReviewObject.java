package popularmoviesstage1.legalimpurity.com.popularmoviesstage2.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 7/8/17.
 */

public class ReviewObject implements Parcelable {
    private String ApiID;
    private String Author;
    private String Content;

    public ReviewObject(long _id, String apiID, String author, String content) {
        ApiID = apiID;
        Author = author;
        Content = content;
    }

    public String getApiID() {
        return ApiID;
    }

    public void setApiID(String apiID) {
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
                this.Content,
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
        String[] data = new String[3];
        in.readStringArray(data);
        this.Author = data[0];
        this.Content = data[1];
        this.ApiID = data[2];
    }
}
