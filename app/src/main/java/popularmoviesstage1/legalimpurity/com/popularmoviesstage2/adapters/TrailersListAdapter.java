package popularmoviesstage1.legalimpurity.com.popularmoviesstage2.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.R;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.Utils.NetworkUtils;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.listeners.ReviewClickListener;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.listeners.TrailerClickListener;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.objects.ReviewObject;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.objects.TrailerVideoObject;


public class TrailersListAdapter extends RecyclerView.Adapter<TrailersListAdapter.MovieItemHolder>{

    private ArrayList<TrailerVideoObject> trailerObjs = new ArrayList<TrailerVideoObject>();
    private Activity act;
    private TrailerClickListener clicker;

    public TrailersListAdapter(Activity act, TrailerClickListener clicker)
    {
        this.act = act;
        this.clicker = clicker;
    }

    public void setReviewssData(ArrayList<TrailerVideoObject> trailerObjs)
    {
        this.trailerObjs = trailerObjs;
        notifyDataSetChanged();
    }

    @Override
    public MovieItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(act).inflate(R.layout.trailer_list_item, parent,false);
        return new MovieItemHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(MovieItemHolder holder, int position) {
        TrailerVideoObject ro = trailerObjs.get(position);
        holder.bind(ro);
    }

    @Override
    public int getItemCount() {
        if(trailerObjs == null)
            return 0;
        return trailerObjs.size();
    }

    public class MovieItemHolder extends RecyclerView.ViewHolder
    {

        private TextView trailerName;
        private ImageView thumbnail;
        private View root_view;

        private MovieItemHolder(View itemView) {
            super(itemView);
            root_view = (View) itemView.findViewById(R.id.root_view);
            trailerName = (TextView) itemView.findViewById(R.id.trailerName);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
        }

        void bind(final TrailerVideoObject to)
        {
            trailerName.setText(to.getName());
            Picasso
                    .with(act)
                    .load(NetworkUtils.YOUTUBE_THUMBNAIL_URL_PREFIX+to.getYoutubeKey()+NetworkUtils.YOUTUBE_THUMBNAIL_URL_SUFFIX)
                    .placeholder(R.drawable.ic_play_arrow_white_24dp)
                    .into(thumbnail);
            root_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clicker.onMovieCLick(to);
                }
            });
        }
    }
}
