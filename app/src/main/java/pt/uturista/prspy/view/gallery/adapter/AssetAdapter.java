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
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import pt.uturista.prspy.R;
import pt.uturista.prspy.model.Asset;

public class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.AssetsViewHolder> {

    private static final String TAG = "AssetsViewAdapter";
    private static final int LAYOUT_ROW = R.layout.gallery_details_asset_row;

    private SimpleAsset[] mCollection;


    public AssetAdapter() {
        mCollection = new SimpleAsset[0];
    }

    @Override
    @NonNull
    public AssetsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(LAYOUT_ROW, parent, false);

        return new AssetsViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return mCollection.length;
    }

    @Override
    public void onBindViewHolder(@NonNull AssetsViewHolder holder, int position) {
        SimpleAsset asset = mCollection[position];
        Context context = holder.assetName.getContext();

        // Sets the name
        holder.assetName.setText(asset.Name);

        // Sets the delay info (If it respawns or not, with or without initial delay)
       StringBuilder delayInfo = new StringBuilder();
        if (asset.InitialDelay != 0) {  // There's a Delay
            delayInfo.append(context.getString(R.string.asset_spawn_delay, asset.InitialDelay));
        }

        if (asset.RespawnDelay < 1000) { // Asset respawn
            delayInfo.append(context.getString(R.string.asset_spawn_respawns, asset.RespawnDelay));

        } else {                        // Asset does not respawn
            delayInfo.append(context.getString(R.string.asset_spawn_no_respawn));
        }
        holder.assetDelay.setText(delayInfo.toString());



        // Sets the quantity of the Spawner
        holder.assetQuantity.setText(String.format(Locale.ENGLISH, "%dx", asset.Qt));
/*
        Context context = holder.assetIcon.getContext();
        int resource = context.getResources().getIdentifier(asset.getVehicle().getIcon(), "drawable", context.getPackageName());

        if (resource != 0)
            holder.assetIcon.setImageResource(resource);
        else
            Log.e(TAG, "Icon for " + asset.getVehicle().getIcon() + " not found");*/
    }

    public void setAssets(Asset[] spawners) {
        if (spawners == null)
            return;

        Map<String, SimpleAsset> assetMap = new HashMap<>();
        for (Asset asset : spawners) {

            String key = String.format(Locale.ENGLISH, "%s%s%s", asset.getName(), asset.getInitialDelay(), asset.getRespawnDelay());
            SimpleAsset simpleAsset = assetMap.get(key);
            if (simpleAsset == null) {
                simpleAsset = new SimpleAsset(asset);
                assetMap.put(key, simpleAsset);
            } else {
                simpleAsset.incrementQt();
            }
        }

        mCollection = new SimpleAsset[assetMap.values().size()];
        assetMap.values().toArray(mCollection);
    }


    class AssetsViewHolder extends RecyclerView.ViewHolder {
        private TextView assetName;
        private TextView assetDelay;
        private TextView assetQuantity;
        private ImageView assetIcon;

        AssetsViewHolder(View view) {
            super(view);
            assetName = view.findViewById(R.id.layout_information_asset_name);
            assetQuantity = view.findViewById(R.id.layout_information_asset_quantity);
            assetDelay = view.findViewById(R.id.layout_information_asset_delay);
            assetIcon = view.findViewById(R.id.asset_icon);
        }

    }

    class SimpleAsset {
        final String Name;
        final int InitialDelay;
        final int RespawnDelay;
        private final String Icon;
        int Qt;

        SimpleAsset(Asset asset) {
            Name = asset.getName();
            InitialDelay = asset.getInitialDelay() / 60;
            RespawnDelay = asset.getRespawnDelay() / 60;
            Icon = asset.getIcon();
            Qt = 1;
        }

        void incrementQt() {
            Qt++;
        }
    }
}
