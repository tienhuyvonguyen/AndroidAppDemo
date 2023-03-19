package com.example.app.utility;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.app.R;
import com.example.app.data.model.Product;

import java.util.ArrayList;

public class Adapter extends ArrayAdapter<Product> {

    public Adapter(@NonNull Context context, ArrayList<Product> courseModelArrayList) {
        super(context, 0, courseModelArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.card_item, parent, false);
        }

        Product courseModel = getItem(position);
//        //TODO: set the text and image of the courseModel to the textview and imageview respectively.
//        TextView courseTV = listitemView.findViewById(R.id.idTVCourse);
//        ImageView courseIV = listitemView.findViewById(R.id.idIVcourse);
//
//        courseTV.setText(courseModel.getCourse_name());
//        courseIV.setImageResource(courseModel.getImgid());
        return listitemView;
    }
}
