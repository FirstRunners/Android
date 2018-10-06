package com.android.firstlearners.learners.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.firstlearners.learners.R;
import com.android.firstlearners.learners.etc.Address;

import java.util.List;

public class GridViewAdapter extends BaseAdapter {
    private List<Address> selectedItem;
    private View.OnClickListener gridViewListener;

    public GridViewAdapter(List<Address> selectedItem) {
        this.selectedItem = selectedItem;
    }

    @Override
    public int getCount() {
        return selectedItem.size();
    }

    @Override
    public Object getItem(int position) {
        return selectedItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_gridview, parent, false);
        }
        convertView.setOnClickListener(gridViewListener);
        convertView.setTag(position);
        TextView name = convertView.findViewById(R.id.selected_name);

        String name_value = selectedItem.get(position).name;
        if(name_value.length() > 3){
            name.setText(name_value.substring(0,3));
        }else{
            name.setText(name_value);
        }
        return convertView;
    }

    public void addItem(List<Address> selectedItem){
        this.selectedItem = selectedItem;
    }

    public void setGridViewListener(View.OnClickListener gridViewListener) {
        this.gridViewListener = gridViewListener;
    }
}
