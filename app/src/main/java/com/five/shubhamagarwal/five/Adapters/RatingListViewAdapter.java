package com.five.shubhamagarwal.five.Adapters;

/**
 * Created by shubhamagrawal on 01/04/17.
 */

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.five.shubhamagarwal.five.Models.RatingParameter;
import com.five.shubhamagarwal.five.R;

import java.util.List;

public class RatingListViewAdapter extends ArrayAdapter<RatingParameter> {

    private AppCompatActivity activity;
    private List<RatingParameter> ratingList;

    public RatingListViewAdapter(AppCompatActivity context, int resource, List<RatingParameter> objects) {
        super(context, resource, objects);
        this.activity = context;
        this.ratingList = objects;
    }

    @Override
    public RatingParameter getItem(int position) {
        return ratingList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.rating_icon_text, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
            //holder.ratingBar.getTag(position);
        }

        holder.ratingBar.setOnRatingBarChangeListener(onRatingChangedListener(holder, position));

        holder.ratingBar.setTag(position);
        holder.ratingBar.setRating((float)getItem(position).getRatingStar());
        holder.movieName.setText(getItem(position).getName());

        return convertView;
    }

    private RatingBar.OnRatingBarChangeListener onRatingChangedListener(final ViewHolder holder, final int position) {
        return new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                RatingParameter item = getItem(position);
                item.setRatingStar(v);
                Log.i("Adapter", "star: " + v);
            }
        };
    }

    private static class ViewHolder {
        private RatingBar ratingBar;
        private TextView movieName;

        public ViewHolder(View view) {
            ratingBar = (RatingBar) view.findViewById(R.id.rate_img);
            movieName = (TextView) view.findViewById(R.id.text);
        }
    }
}
