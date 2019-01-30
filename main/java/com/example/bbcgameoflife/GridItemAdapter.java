package com.example.bbcgameoflife;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/*
    The adapter employs a store to recycle previously created cells instead of recreating them every time.
    This is to improve efficiency during user scrolling of the gridView.
 */

public class GridItemAdapter extends ArrayAdapter<Cell> {

    private static class ViewHolder {   //class for storing references to view
        TextView cellTextView;
    }

    public GridItemAdapter(Context context, List<Cell> cells) {
        super(context, -1, cells);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Cell cell = getItem(position);      // Cell object for each position
        GridItemAdapter.ViewHolder viewHolder;      // ViewHolder object to reference view store in class

        if (convertView == null) {      //if view not already built, inflate and find references to the view contained

            LayoutInflater inflater = LayoutInflater.from(getContext());        // inflate the resource xml file
            convertView = inflater.inflate(R.layout.item_cell, parent, false);

            viewHolder = new GridItemAdapter.ViewHolder();      // create object
            viewHolder.cellTextView = convertView.findViewById(R.id.cellTextView);  // store view reference in viewHolder

            convertView.setTag(viewHolder);     // views in holder stored in the tag of the convertView
        } else {
            viewHolder = (GridItemAdapter.ViewHolder) convertView.getTag(); // if already built then retrieved from tag
        }

        if (cell != null) {             // if a valid cell is found the text is set in the textView depending on isAlive boolean
            String cellDisplay = "";
            if(cell.getAlive()) {
                cellDisplay = "O";
            }
            viewHolder.cellTextView.setText(cellDisplay);
        }
        return convertView;
    }
}
