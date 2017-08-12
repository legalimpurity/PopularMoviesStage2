package popularmoviesstage1.legalimpurity.com.popularmoviesstage2.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 7/8/17.
 */

public class TrailerVideoObject implements Parcelable {

    private String ApiId;
    private String Name;
    private String YoutubeKey;

    public TrailerVideoObject(String ApiId, String name, String youtubeKey) {
        this.ApiId = ApiId;
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
                this.YoutubeKey,
                this.ApiId
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
        this.ApiId = data[2];
    }
}
