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


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import pt.uturista.prspy.R;
import pt.uturista.prspy.model.Asset;
import pt.uturista.prspy.model.Spawner;

public class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.AssetsViewHolder> {

    private static final String TAG = "AssetsViewAdapter";
    private static final int LAYOUT_ROW = R.layout.gallery_details_asset_row;

    private Asset[] mCollection;


    public AssetAdapter() {
        mCollection = new Asset[0];
    }

    @Override
    public AssetsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(LAYOUT_ROW, parent, false);

        return new AssetsViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return mCollection.length;
    }

    @Override
    public void onBindViewHolder(AssetsViewHolder holder, int position) {

        Asset asset = mCollection[position];

        // Sets the name
        holder.assetName.setText(asset.getName());

        Log.i(TAG, "Vehicles: " + asset.getName());

        // sets the time of first spawn, also indicates if there's a delay
        if (asset.getInitialDelay() == 0) {
            holder.assetSpawn.setVisibility(View.VISIBLE);
        } else {
            holder.assetSpawn.setVisibility(View.GONE);
        }

        // sets the time of Respawn if any
        int respawn = asset.getRespawnDelay();
        if (respawn == 99999) {
            holder.assetRespawn.setText("Spawn time: Never");
        } else {
            respawn = respawn / 60;
            holder.assetRespawn.setText("Spawn time: " + respawn + "m");
        }

        // Sets the quantity of the Spawner
        //holder.assetQuantity.setText(asset.getQuantity() + "x");
/*
        Context context = holder.assetIcon.getContext();
        int resource = context.getResources().getIdentifier(asset.getVehicle().getIcon(), "drawable", context.getPackageName());

        if (resource != 0)
            holder.assetIcon.setImageResource(resource);
        else
            Log.e(TAG, "Icon for " + asset.getVehicle().getIcon() + " not found");*/
    }

    public void setAssets(Asset[] spawners) {
        Log.d(TAG, "setAssets: " + spawners.length);
        if (spawners != null)
            mCollection = spawners;
    }


    class AssetsViewHolder extends RecyclerView.ViewHolder {
        AssetsViewHolder(View view) {
            super(view);
            assetName = (TextView) view.findViewById(R.id.layout_information_asset_name);
            assetSpawn = (TextView) view.findViewById(R.id.layout_information_asset_spawnTime);
            assetRespawn = (TextView) view.findViewById(R.id.layout_information_asset_respawn_delay);
            assetQuantity = (TextView) view.findViewById(R.id.layout_information_asset_quantity);
            assetIcon = (ImageView) view.findViewById(R.id.asset_icon);
        }

        private TextView assetName;
        private TextView assetSpawn;
        private TextView assetRespawn;
        private TextView assetQuantity;
        private ImageView assetIcon;
    }
}
