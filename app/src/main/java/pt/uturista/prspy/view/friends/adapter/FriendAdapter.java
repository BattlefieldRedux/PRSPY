package pt.uturista.prspy.view.friends.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pt.uturista.prspy.R;
import pt.uturista.prspy.model.Player;
import pt.uturista.prspy.model.Server;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private static final String TAG = "FriendAdapter";
    private final static int ROW_LAYOUT_ID = R.layout.friend_row;
    private @NonNull  Player[] mPlayers;
    private @NonNull Server[] mServers;


    public FriendAdapter(@NonNull Player[] friends,@NonNull Server[] servers) {

        Log.d(TAG, "FriendAdapter");
        mPlayers = friends;
        mServers = servers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(ROW_LAYOUT_ID, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position < mPlayers.length)
        {
            Player player = mPlayers[position];

            Server friendsServer = null;
            for (Server server : mServers) {
                if(server.getID().equals(player.getServerID())){
                    friendsServer = server;
                }
            }

            holder.FriendName.setText(mPlayers[position].getName());
            holder.FriendServer.setText(friendsServer == null ? "Unable to retrieve server's name" : friendsServer.getServerName());
        }

    }

    @Override
    public int getItemCount() {
        return mPlayers.length;
    }

    public void setFriends(@NonNull Player[] friends) {
        mPlayers = friends;
        notifyDataSetChanged();
    }

    public void setServers(@NonNull Server[] servers) {
        mServers = servers;
        notifyDataSetChanged();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView FriendName;
        TextView FriendServer;
        View Root;

        ViewHolder(View view) {
            super(view);

            FriendName = view.findViewById(R.id.friend_name);
            FriendServer = view.findViewById(R.id.friend_server);
            Root = view;
        }
    }
}
