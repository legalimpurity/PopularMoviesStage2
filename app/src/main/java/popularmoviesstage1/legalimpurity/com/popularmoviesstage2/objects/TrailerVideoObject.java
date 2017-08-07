package popularmoviesstage1.legalimpurity.com.popularmoviesstage2.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 7/8/17.
 */

public class TrailerVideoObject implements Parcelable {
    private long _id;

    private String Name;
    private String YoutubeKey;

    public TrailerVideoObject(long _id, String name, String youtubeKey) {
        this._id = _id;
        Name = name;
        YoutubeKey = youtubeKey;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
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
                this._id
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
        String[] data = new String[2];
        in.readStringArray(data);
        this.Name = data[0];
        this.YoutubeKey = data[1];

        long[] data2 = new long[1];
        in.readLongArray(data2);
        this._id = data2[0];
    }
}
