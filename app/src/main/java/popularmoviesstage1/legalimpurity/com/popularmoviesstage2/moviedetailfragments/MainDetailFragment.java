package popularmoviesstage1.legalimpurity.com.popularmoviesstage2.moviedetailfragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.R;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.objects.MovieObject;

public class MainDetailFragment extends Fragment {

    private static final String ARG_MOVIE_OBJECT = "ARG_MOVIE_OBJECT";

    private MovieObject mo;

    @BindView(R.id.textViewReleaseDateValue) TextView textViewReleaseDateValue;
    @BindView(R.id.textViewVoteAverageValue) TextView textViewVoteAverageValue;
    @BindView(R.id.textViewPlotSynopsisValue) TextView textViewPlotSynopsisValue;

    public MainDetailFragment() {
    }

    public static MainDetailFragment newInstance(MovieObject mo) {
        MainDetailFragment fragment = new MainDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_MOVIE_OBJECT, mo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_movie_detail, container, false);
        ButterKnife.bind(this,rootView);
        mo = getArguments().getParcelable(ARG_MOVIE_OBJECT);
        textViewReleaseDateValue.setText(mo.getReleaseDate());
        textViewVoteAverageValue.setText(mo.getUserRating());
        textViewPlotSynopsisValue.setText(mo.getPlotSynopsis());
        return rootView;
    }
}