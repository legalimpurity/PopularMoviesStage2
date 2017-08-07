package popularmoviesstage1.legalimpurity.com.popularmoviesstage2.objects;

/**
 * Created by root on 7/8/17.
 */

public class TrailerVideoObject {
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
}
