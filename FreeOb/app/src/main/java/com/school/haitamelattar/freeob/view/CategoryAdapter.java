package com.school.haitamelattar.freeob.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.school.haitamelattar.freeob.R;
import com.school.haitamelattar.freeob.model.Category;

import java.util.ArrayList;

/**
 * Created by jordy on 26-10-17.
 */

public class CategoryAdapter extends ArrayAdapter<Category> {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Category> categories;

    public CategoryAdapter(Context context, int resourceId, int textViewId, ArrayList<Category> categories) {
        super(context, resourceId, textViewId, categories);
        this.context = context;
        this.categories = categories;
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of categories
     */
    @Override
    public int getCount() {
        return categories.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param i Position of the item to return from the data set.
     * @return The object at the specified position.
     */
    @Override
    public Category getItem(int i) {
        return categories.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.content_category_spinner, parent, false);

        TextView categoryTextView = (TextView) view.findViewById(R.id.spinnerItem);
        Category category = categories.get(i);
        categoryTextView.setText(category.toString());
        categoryTextView.setTag((Long) category.getId());

        view.setTag(categoryTextView);

        return view;
    }

    public void setError(View v, CharSequence s) {
        TextView textview = (TextView) v.findViewById(R.id.spinnerItem);
        textview.setError(s);
    }

    private static class ViewHolder {
        public TextView categoryTextView;
        public TextView getTextView() {
            return categoryTextView;
        }
    }

}
