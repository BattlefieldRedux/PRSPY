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

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import pt.uturista.prspy.R;


public class LayoutAdapter extends RecyclerView.Adapter<LayoutAdapter.ViewHolder> implements View.OnClickListener {

    private final Listener mListener;
    private String[] mCollection = new String[0];
    private CheckedTextView mSelectedView;
    private int mSelectedPosition;

    public LayoutAdapter(Listener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.common_string_row, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if (position < mCollection.length) {
            holder.TextView.setText(mCollection[position]);
            holder.TextView.setTag(position);
            holder.TextView.setOnClickListener(this);
        }

        if (position == mSelectedPosition) {
            holder.TextView.setChecked(true);
            mSelectedView = holder.TextView;
        }
    }


    public void setData(String[] data) {
        if (data != null)
            mCollection = data;
    }

    @Override
    public int getItemCount() {
        return mCollection.length;
    }

    @Override
    public void onClick(View v) {
        if (mSelectedView != null)
            mSelectedView.setChecked(false);

        mSelectedView = (CheckedTextView) v;
        mSelectedView.setChecked(true);

        mSelectedPosition = (int) mSelectedView.getTag();
        mListener.onLayoutSelecter(mSelectedPosition);
    }

    public void setItemChecked(int position) {
        final int oldChecked = mSelectedPosition;
        mSelectedPosition = position;

        notifyItemChanged(oldChecked);
        notifyItemChanged(mSelectedPosition);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        final CheckedTextView TextView;

        ViewHolder(View itemView) {
            super(itemView);
            TextView = itemView.findViewById(R.id.text);
        }
    }

    public interface Listener {
        void onLayoutSelecter(int position);
    }
}
