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
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.objects.TrailerVideoObject;

public class TrailersLoader extends AsyncTaskLoader {

    public static final String MOVIE_API_ID = "MOVIE_API_ID";

    private Context context;
    private Bundle args;

    public TrailersLoader(Context context, Bundle args) {
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
    public ArrayList<TrailerVideoObject> loadInBackground() {
        String movieAPIID = args.getString(MOVIE_API_ID);

        if (movieAPIID == null || TextUtils.isEmpty(movieAPIID)) {
            return null;
        }

        URL sortByRequestUrl = NetworkUtils.buildTrailersUrl(movieAPIID);

        try {
            String jsonMoviesResponse = NetworkUtils
                    .getResponseFromHttpUrl(sortByRequestUrl);
            ArrayList<TrailerVideoObject> output = JsonUtils.getTrailerObjectsFromJson(context,jsonMoviesResponse);
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
