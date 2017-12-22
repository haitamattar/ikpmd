package com.school.haitamelattar.freeob.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.school.haitamelattar.freeob.R;
import com.school.haitamelattar.freeob.model.Advert;

import java.util.ArrayList;

/**
 * Haha ekte goeie grap
 */
public class AdvertsAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private Advert[] adverts;

    public AdvertsAdapter(Context context, Advert[] adverts) {
        this.context = context;
        this.adverts = adverts;
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of adverts
     */
    @Override
    public int getCount() {
        return adverts.length;
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param i Position of the item to return from the data set.
     * @return The object at the specified position.
     */
    @Override
    public Object getItem(int i) {
        return adverts[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        ViewHolder viewHolder;

        if(view == null) {
            view = inflater.inflate(R.layout.content_adverts_list, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.titleTextView = (TextView) view.findViewById(R.id.advertTitle);
            viewHolder.descriptionTextView = (TextView) view.findViewById(R.id.advertDescription);
            viewHolder.categoryTextView = (TextView) view.findViewById(R.id.advertCategory);
            viewHolder.iconImageView = (ImageView) view.findViewById(R.id.advertIcon);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final TextView titleTextView = viewHolder.titleTextView;
        final TextView descriptionTextView = viewHolder.descriptionTextView;
        final TextView categoryTextView = viewHolder.categoryTextView;
        final ImageView iconImageView = viewHolder.iconImageView;

        final Advert advert = adverts[i];

        iconImageView.post(new Runnable() {
            @Override
            public void run() {
                if (advert.getImage() != null) {
                    byte[] decodedString = Base64.decode(advert.getImage(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    if (decodedByte != null) iconImageView.setImageBitmap(decodedByte);
                }
            }
        });

        titleTextView.setText(advert.getName());
        descriptionTextView.setText(advert.getDescription());
        categoryTextView.setText(advert.getCategoryName());

        return view;
    }

    private static class ViewHolder {
        public TextView titleTextView;
        public TextView descriptionTextView;
        public TextView categoryTextView;
        public ImageView iconImageView;
    }
}
