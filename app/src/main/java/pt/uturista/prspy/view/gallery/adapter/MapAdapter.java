/*
 * Copyright (c) 2018 uturista.pt
 *
 * Licensed under the Attribution-NonCommercial 4.0 International (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://creativecommons.org/licenses/by-nc/4.0/legalcode
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pt.uturista.prspy.view.gallery.adapter;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pt.uturista.prspy.R;
import pt.uturista.prspy.model.Level;
import pt.uturista.prspy.utils.filter.Filter;

import static android.content.ContentValues.TAG;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class MapAdapter extends RecyclerView.Adapter<MapAdapter.MapHolder>  {
    private final static int ROW_LAYOUT_ID = R.layout.gallery_map_row;
    private final Listener mListener;
    private Level[] mCollection;
    private Level[] mDisplayedCollection;
    private Filter<Level> mFilter;

    public MapAdapter(Listener listener) {
        mDisplayedCollection = mCollection = new Level[0];
        mListener = listener;
    }

    @Override
    public MapHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(ROW_LAYOUT_ID, parent, false);

        return new MapHolder(v, mListener);
    }

    public void filter(Filter<Level> filter) {
        if (filter == null)
            return;

        mFilter = filter;
        List<Level> newCollection = new ArrayList<>(mCollection.length);
        for (Level map : mCollection) {

            if (mFilter.filter(map)) {
                Log.d(TAG, ">True");
                newCollection.add(map);
            }else {
                Log.d(TAG, ">False");
            }
        }

        mDisplayedCollection = new Level[newCollection.size()];
        newCollection.toArray(mDisplayedCollection);

        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(MapHolder holder, int position) {

        Level map = mDisplayedCollection[position];
        holder.setMap(map);
        holder.MapName.setText(map.getName());

        Uri imageUri = Uri.parse(String.format(Locale.US, "https://www.realitymod.com/mapgallery/images/maps/%s/tile.jpg", map.getWebKey()));

        RequestOptions options = new RequestOptions()
                .placeholder(map.getColor());

        Glide.with(holder.MapImage.getContext())
                .load(imageUri)
                .apply(options)
                .transition(withCrossFade())
                .into(holder.MapImage);


    }

    @Override
    public int getItemCount() {
        return mDisplayedCollection.length;
    }

    public void setMaps(@Nullable Level[] maps) {
        if (maps != null) {
            mDisplayedCollection = mCollection = maps;
        }
    }



    class MapHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final Listener mListener;
        ImageView MapImage;
        TextView MapName;
        private Level mMap;


        MapHolder(View itemView, Listener listener)  {
            super(itemView);
            MapImage = itemView.findViewById(R.id.map_image);
            MapName = itemView.findViewById(R.id.map_name);
            mListener = listener;
            itemView.setOnClickListener(this);
        }


        void setMap(Level map){
            mMap = map;
        }

        @Override
        public void onClick(View view) {

            if(mMap != null){

                mListener.onMapClick(mMap);
            }
        }
    }
    public interface Listener{

        void onMapClick(Level map);
    }
}
