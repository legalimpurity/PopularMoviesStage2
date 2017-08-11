package popularmoviesstage1.legalimpurity.com.popularmoviesstage2.moviedetailfragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.R;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.adapters.TrailersListAdapter;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.listeners.TrailerClickListener;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.objects.MovieObject;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.objects.TrailerVideoObject;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.tasks.TrailersLoader;

import static popularmoviesstage1.legalimpurity.com.popularmoviesstage2.Utils.NetworkUtils.YOUTUBE_VIDEO_URL;

public class TrailersFragment extends Fragment implements LoaderManager.LoaderCallbacks {

    private static final int TRAILERS_DATA_LOADER = 24;

    private static final String ARG_MOVIE_OBJECT = "ARG_MOVIE_OBJECT";

    private MovieObject mo;
    private TrailersListAdapter trailersListAdapter;

    @BindView(R.id.review_list_recycler_view) RecyclerView review_list_recycler_view;

    public TrailersFragment() {
    }

    public static TrailersFragment newInstance(MovieObject mo) {
        TrailersFragment fragment = new TrailersFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_MOVIE_OBJECT, mo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_reviews, container, false);
        ButterKnife.bind(this,rootView);
        mo = getArguments().getParcelable(ARG_MOVIE_OBJECT);
        TrailerClickListener tcl = new TrailerClickListener() {
            @Override
            public void onMovieCLick(TrailerVideoObject trailerObject) {
                    Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailerObject.getYoutubeKey()));
                    Intent webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(YOUTUBE_VIDEO_URL + trailerObject.getYoutubeKey()));
                    try {
                        startActivity(appIntent);
                    } catch (ActivityNotFoundException ex) {
                        startActivity(webIntent);
                    }
            }
        };
        trailersListAdapter = new TrailersListAdapter(getActivity(),tcl);

        review_list_recycler_view.setAdapter(trailersListAdapter);
        review_list_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadReviewsData(getActivity());
        return rootView;
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new TrailersLoader(getContext(), args) ;
    }

    @Override
    public void onLoaderReset(Loader loader) {
        // No need to implement
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        if (null == data) {
            showErrorMessage();
        } else {
            ArrayList<TrailerVideoObject> realdata = (ArrayList<TrailerVideoObject>) data;
            trailersListAdapter.setReviewssData(realdata);
            showTrailers();
        }
    }

    private void loadReviewsData(FragmentActivity act) {
        Bundle queryBundle = new Bundle();
        queryBundle.putString(TrailersLoader.MOVIE_API_ID, mo.getApiId()+"");

        LoaderManager loaderManager = act.getSupportLoaderManager();
        Loader<String> trailersLoader = loaderManager.getLoader(TRAILERS_DATA_LOADER);
        if (trailersLoader == null) {
            loaderManager.initLoader(TRAILERS_DATA_LOADER, queryBundle, this);
        } else {
            loaderManager.restartLoader(TRAILERS_DATA_LOADER, queryBundle, this);
        }
    }
    private void showErrorMessage()
    {
    }

    private void showTrailers()
    {
    }

}
