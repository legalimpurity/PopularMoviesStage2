package popularmoviesstage1.legalimpurity.com.popularmoviesstage2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.Utils.NetworkUtils;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.adapters.MovieDetailPagerAdapter;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.objects.MovieObject;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String MOVIE_OBJECT_KEY = "0a46c76c98b80b4ed6befbe3760b28b1";


    @BindView(R.id.movie_poster) ImageView movie_poster;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout toolbar_layout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.container) ViewPager container;
    @BindView(R.id.tabs) TabLayout tabs;

    private MovieObject mo;
    private MovieDetailPagerAdapter movieDetailPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(getIntent() != null && getIntent().getExtras() != null) {
            mo = (MovieObject) getIntent().getExtras().getParcelable(MOVIE_OBJECT_KEY);
        }
        setView(this);
        setFab(this);
    }

    private void setView(Activity act)
    {
        Picasso.with(act)
                .load(NetworkUtils.MOVIES_IMAGE_URL+mo.getMoviePosterImageThumbnailUrl())
                .placeholder(R.drawable.ic_local_movies_grey_24dp)
                .into(movie_poster);

        toolbar_layout.setTitle(mo.getOrignalTitle());

        movieDetailPagerAdapter = new MovieDetailPagerAdapter(this,mo);

        container.setAdapter(movieDetailPagerAdapter);
        tabs.setupWithViewPager(container);
    }

    private void setFab(final Activity act)
    {

        String sharer_content = "";

        sharer_content = sharer_content + act.getResources().getString(R.string.movie_title) + " : \n";
        sharer_content = sharer_content + mo.getOrignalTitle() + "\n\n";

        sharer_content = sharer_content + act.getResources().getString(R.string.release_date) + " : \n";
        sharer_content = sharer_content + mo.getReleaseDate() + "\n\n";

        sharer_content = sharer_content + act.getResources().getString(R.string.vote_average) + " : \n";
        sharer_content = sharer_content + mo.getUserRating() + "\n\n";

        sharer_content = sharer_content + act.getResources().getString(R.string.plot_synopsis) + " : \n";
        sharer_content = sharer_content + mo.getPlotSynopsis() + "\n\n";

        sharer_content = sharer_content + act.getResources().getString(R.string.shared_from_themoviedb);

        final String finalSharer_content = sharer_content;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");

                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, act.getResources().getString(R.string.shared_subject));
                intent.putExtra(android.content.Intent.EXTRA_TEXT, finalSharer_content);
                act.startActivity(Intent.createChooser(intent, act.getResources().getString(R.string.app_name)));
            }
        });
    }

}
