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
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.listeners.MovieClickListener;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.listeners.ReviewClickListener;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.objects.MovieObject;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.objects.ReviewObject;


public class ReviewsListAdapter extends RecyclerView.Adapter<ReviewsListAdapter.MovieItemHolder>{

    private ArrayList<ReviewObject> reviewObjs = new ArrayList<ReviewObject>();
    private Activity act;
    private ReviewClickListener clicker;

    public ReviewsListAdapter(Activity act, ReviewClickListener clicker)
    {
        this.act = act;
        this.clicker = clicker;
    }

    public void setReviewssData(ArrayList<ReviewObject> reviewObjs)
    {
        this.reviewObjs = reviewObjs;
        notifyDataSetChanged();
    }

    @Override
    public MovieItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(act).inflate(R.layout.review_list_item, parent,false);
        return new MovieItemHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(MovieItemHolder holder, int position) {
        ReviewObject ro = reviewObjs.get(position);
        holder.bind(ro);
    }

    @Override
    public int getItemCount() {
        if(reviewObjs == null)
            return 0;
        return reviewObjs.size();
    }

    public class MovieItemHolder extends RecyclerView.ViewHolder
    {

        private TextView author, review;

        private MovieItemHolder(View itemView) {
            super(itemView);
            author = (TextView) itemView.findViewById(R.id.author);
            review = (TextView) itemView.findViewById(R.id.review);
        }

        void bind(final ReviewObject ro)
        {
            author.setText(ro.getAuthor());
            review.setText(ro.getContent());
        }
    }
}
