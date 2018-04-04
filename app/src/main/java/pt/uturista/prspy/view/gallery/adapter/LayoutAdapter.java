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
