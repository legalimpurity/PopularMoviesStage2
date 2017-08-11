package popularmoviesstage1.legalimpurity.com.popularmoviesstage2.adapters;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.R;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.moviedetailfragments.MainDetailFragment;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.moviedetailfragments.ReviewsFragment;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.moviedetailfragments.TrailersFragment;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.objects.MovieObject;

public class MovieDetailPagerAdapter extends FragmentPagerAdapter {

    private MovieObject mo;
    private AppCompatActivity act;

    public MovieDetailPagerAdapter(AppCompatActivity act, MovieObject mo) {
        super(act.getSupportFragmentManager());
        this.act = act;
        this.mo = mo;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MainDetailFragment.newInstance(mo);
            case 1:
                return ReviewsFragment.newInstance(mo);
            case 2:
                return TrailersFragment.newInstance(mo);
        }
        return MainDetailFragment.newInstance(mo);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return act.getString(R.string.movie_details);
            case 1:
                return act.getString(R.string.reviews);
            case 2:
                return act.getString(R.string.trailers);
        }
        return null;
    }
}