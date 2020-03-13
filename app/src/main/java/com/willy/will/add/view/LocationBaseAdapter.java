package com.willy.will.add.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.willy.will.R;
import com.willy.will.common.model.Location;

import java.util.ArrayList;

public class LocationBaseAdapter extends BaseAdapter {

    private ArrayList<Location> locationArray = new ArrayList<>();

    @Override
    public int getCount() {
        return locationArray.size();
    }

    @Override
    public Location getItem(int position) {
        return locationArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(locationArray.get(position).getLocationId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_location, parent, false);
        }

        TextView placeName = convertView.findViewById(R.id.place_name);
        TextView addressName = convertView.findViewById(R.id.address_name);
        Location location = getItem(position);
        String addressNameValue = location.getAddressName();
        placeName.setText(location.getPlaceName());
        Log.d("addressNameValue", "addressNameValue/" + addressNameValue+"/");
        if(addressNameValue==null || addressNameValue.equals("")){
            addressName.setVisibility(View.GONE);
        }else{
            addressName.setText(location.getAddressName());
        }

        return convertView;
    }

    public void addItem(Location location) {
        locationArray.add(location);
    }


}
