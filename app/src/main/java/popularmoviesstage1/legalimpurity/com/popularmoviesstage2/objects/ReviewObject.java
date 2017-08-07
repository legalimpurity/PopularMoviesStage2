package popularmoviesstage1.legalimpurity.com.popularmoviesstage2.objects;

/**
 * Created by root on 7/8/17.
 */

public class ReviewObject {
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
}
