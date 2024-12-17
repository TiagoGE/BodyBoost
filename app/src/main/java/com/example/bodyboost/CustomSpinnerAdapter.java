package com.example.bodyboost;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomSpinnerAdapter extends ArrayAdapter<CharSequence> {

    private LayoutInflater inflater;
    private int resource;
    private Typeface typeface; // Typeface for bold text

    public CustomSpinnerAdapter(Context context, int resource, CharSequence[] items, Typeface typeface) {
        super(context, resource, items);
        this.resource = resource;
        this.typeface = typeface;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_spinner_dropdown_item, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.text1); // Use the ID from your custom layout
        textView.setText(getItem(position));
        textView.setTypeface(typeface, Typeface.BOLD); // Set text to bold

        return convertView;
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(resource, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(getItem(position));
        textView.setTypeface(typeface, Typeface.BOLD); // Set text to bold
//        textView.setTextColor(Color.BLACK); // Set text color to white
        return convertView;
    }
}
