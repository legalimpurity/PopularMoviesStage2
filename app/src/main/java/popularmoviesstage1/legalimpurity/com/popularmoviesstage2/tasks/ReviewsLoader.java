package popularmoviesstage1.legalimpurity.com.popularmoviesstage2.tasks;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

import java.net.URL;
import java.util.ArrayList;

import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.Utils.JsonUtils;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.Utils.NetworkUtils;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.objects.ReviewObject;

public class ReviewsLoader extends AsyncTaskLoader {

    public static final String MOVIE_API_ID = "MOVIE_API_ID";

    private Context context;
    private Bundle args;

    public ReviewsLoader(Context context, Bundle args) {
        super(context);
        this.context = context;
        this.args = args;
    }

    @Override
    protected void onStartLoading() {
        if (args == null) {
            return;
        }
        forceLoad();
    }

    @Override
    public ArrayList<ReviewObject> loadInBackground() {
        long movieAPIID = args.getLong(MOVIE_API_ID);

        if (movieAPIID == 0) {
            return null;
        }

        URL sortByRequestUrl = NetworkUtils.buildReviewsUrl(movieAPIID);

        try {
            String jsonMoviesResponse = NetworkUtils
                    .getResponseFromHttpUrl(sortByRequestUrl);
            ArrayList<ReviewObject> output = JsonUtils.getReviewObjectsFromJson(context,jsonMoviesResponse,movieAPIID);
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
