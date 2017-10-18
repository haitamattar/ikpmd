package com.school.haitamelattar.freeob;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.school.haitamelattar.freeob.model.Advert;
import com.squareup.picasso.Picasso;

public class AdvertDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_detail);

        Advert advert = (Advert) getIntent().getSerializableExtra("Advert");

        ImageView imageView = (ImageView) this.findViewById(R.id.advertDetailImage);
        // Image for testing
        Picasso.with(this).load("http://www.studentfiets.nl/wp-content/uploads/2015/04/herenfiets-03.jpg").into(imageView);

        TextView categoryTextView = (TextView) this.findViewById(R.id.advertDetailCategory);
        TextView titleTextView = (TextView) this.findViewById(R.id.advertDetailTitle);
        TextView descriptionTextView = (TextView) this.findViewById(R.id.advertDetailDescription);

        categoryTextView.setText(advert.getCategoryName());
        titleTextView.setText(advert.getName());
        descriptionTextView.setText(advert.getDescription());
    }

    public void expandText(View view) {
        TextView textView = (TextView) this.findViewById(R.id.advertDetailDescription);
        textView.setMaxLines(Integer.MAX_VALUE);
        textView.setEllipsize(null);
    }


}
