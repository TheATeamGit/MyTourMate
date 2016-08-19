package com.example.hasibuzzaman.mytourmate.mainClassAndMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hasibuzzaman.mytourmate.JsonPersing.PlacesResponse;
import com.example.hasibuzzaman.mytourmate.R;

import java.util.ArrayList;

/**
 * Created by Hasibuzzaman on 8/20/2016.
 */
public class MyAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<PlacesResponse.Result> results ;

    public MyAdapter(Context context, LayoutInflater layoutInflater,ArrayList<PlacesResponse.Result> results) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.results = results;
    }

    public class ViewHolder
    {
        TextView placeNameTv;
        TextView placeAddressTv;
        ImageView imageView;

    }
    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view==null)
        {  viewHolder= new ViewHolder();
            view= layoutInflater.inflate(R.layout.item_list_view,null,false);
            viewHolder.placeNameTv= (TextView) view.findViewById(R.id.placeNameTv);
            viewHolder.placeAddressTv= (TextView) view.findViewById(R.id.placeAddressTv);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.myimageview);
            view.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.placeNameTv.setText(results.get(i).getName());
        viewHolder.placeAddressTv.setText(results.get(i).getVicinity());
        viewHolder.imageView.setImageResource(R.drawable.place);

        return view;
    }
}

