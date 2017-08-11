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

    public void setUpdatedMovieObject(MovieObject mo)
    {
        this.mo = mo;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MainDetailFragment.newInstance(mo);
            case 1:
                // If trailers have loaded first than put it on second position.
                if(mo.getReviewObjs() != null)
                    return ReviewsFragment.newInstance(mo);
                else
                    return TrailersFragment.newInstance(mo);
            case 2:
                return TrailersFragment.newInstance(mo);
        }
        return MainDetailFragment.newInstance(mo);
    }

    @Override
    public int getCount() {
        // If both have loaded
        if(mo.getTrailerVideoObjs() != null && mo.getReviewObjs() != null)
            return 3;
        // If one of them have loaded
        else if(mo.getTrailerVideoObjs() != null || mo.getReviewObjs() != null)
            return 2;
        // If nothing has loaded
        else
            return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return act.getString(R.string.movie_details);
            case 1:
                // If trailers have loaded first than put it on second position.
                if(mo.getReviewObjs() != null)
                    return act.getString(R.string.reviews);
                else
                    return act.getString(R.string.trailers);
            case 2:
                return act.getString(R.string.trailers);
        }
        return null;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}