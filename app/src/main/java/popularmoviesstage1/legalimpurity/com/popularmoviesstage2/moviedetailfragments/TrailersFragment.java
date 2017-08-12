package popularmoviesstage1.legalimpurity.com.popularmoviesstage2.moviedetailfragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.R;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.adapters.TrailersListAdapter;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.listeners.TrailerClickListener;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.objects.MovieObject;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.objects.TrailerVideoObject;

import static popularmoviesstage1.legalimpurity.com.popularmoviesstage2.Utils.NetworkUtils.YOUTUBE_VIDEO_URL;

public class TrailersFragment extends Fragment{

//    private static final int TRAILERS_DATA_LOADER = 24;

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

            @Override
            public void onMovieShare(TrailerVideoObject trailerObject) {
                String sharer_content = "";

                sharer_content = sharer_content + getString(R.string.youtube_url);
                sharer_content = sharer_content + YOUTUBE_VIDEO_URL + trailerObject.getYoutubeKey();

                sharer_content = sharer_content + getString(R.string.shared_from_themoviedb);

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");

                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.shared_subject));
                intent.putExtra(android.content.Intent.EXTRA_TEXT, sharer_content);
                startActivity(Intent.createChooser(intent, getString(R.string.app_name)));
            }
        };
        trailersListAdapter = new TrailersListAdapter(getActivity(),tcl);
        if (mo.getTrailerVideoObjs() != null)
            trailersListAdapter.setReviewssData(mo.getTrailerVideoObjs());
        review_list_recycler_view.setAdapter(trailersListAdapter);
        review_list_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
//        loadReviewsData(getActivity());
        return rootView;
    }

//    @Override
//    public void onLoadFinished(Loader loader, Object data) {
//        if (null == data) {
//            showErrorMessage();
//        } else {
//            ArrayList<TrailerVideoObject> realdata = (ArrayList<TrailerVideoObject>) data;
//            trailersListAdapter.setReviewssData(realdata);
//            showTrailers();
//        }
//    }

//    private void loadReviewsData(FragmentActivity act) {
//        Bundle queryBundle = new Bundle();
//        queryBundle.putString(TrailersLoader.MOVIE_API_ID, mo.getApiId()+"");
//
//        LoaderManager loaderManager = act.getSupportLoaderManager();
//        Loader<String> trailersLoader = loaderManager.getLoader(TRAILERS_DATA_LOADER);
//        if (trailersLoader == null) {
//            loaderManager.initLoader(TRAILERS_DATA_LOADER, queryBundle, this);
//        } else {
//            loaderManager.restartLoader(TRAILERS_DATA_LOADER, queryBundle, this);
//        }
//    }
    private void showErrorMessage()
    {
    }

    private void showTrailers()
    {
    }

}
