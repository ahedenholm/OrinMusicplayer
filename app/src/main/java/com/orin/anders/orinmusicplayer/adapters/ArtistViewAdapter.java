package com.orin.anders.orinmusicplayer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.orin.anders.orinmusicplayer.R;

import java.util.ArrayList;
import java.util.List;

public class ArtistViewAdapter extends RecyclerView.Adapter {

    private List<Integer> imageResList = new ArrayList<Integer>();

    public ArtistViewAdapter(List<Integer> imageUrlList) {
        this.imageResList = imageUrlList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_artist, viewGroup, false);

        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
        itemViewHolder.item.setImageResource(imageResList.get(i));
    }

    @Override
    public int getItemCount() {
        return imageResList.size();
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView item;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.item = (ImageView) itemView.findViewById(R.id.artist_image);
        }
    }
}
