package pt.uturista.prspy.view.servers.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import pt.uturista.prspy.R;
import pt.uturista.prspy.model.Player;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerHolder> {
    private final static String TAG = "PlayersAdapter";
    private static final int LAYOUT_ROW = R.layout.server_player_row;


    private Player[] mCollection;
    private final PlayerClicked mListener;

    public PlayerAdapter(PlayerClicked listener) {
        super();
        mCollection = new Player[0];
        mListener = listener;

    }

    @Override
    public PlayerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(LAYOUT_ROW, parent, false);

        return new PlayerHolder(v);
    }

    @Override
    public void onBindViewHolder(PlayerHolder holder, int position) {
        if (position == 0) {
            holder.setIsRecyclable(false);
            return;
        }

        Log.d(TAG, "onBindViewHolder: " + position);
        holder.player = mCollection[position - 1];
        String player_name = holder.player.getName();

        // Player Name
        holder.name.setText(player_name);

        // Player Object (TAG)
        holder.name.setTag(holder.player);

        // Player KILLS
        holder.kills.setText(String.format(Locale.US, "%d", holder.player.getKills()));

        // Player DEATHS
        holder.deaths.setText(String.format(Locale.US, "%d", holder.player.getDeaths()));

        // Player Score
        holder.score.setText(String.format(Locale.US, "%d", holder.player.getScore()));

        // Player Position
        holder.position.setText(String.format(Locale.US, "%d", position));

        if (holder.player.isFriend())
            holder.root.setBackgroundResource(R.color.blue);
        else
            holder.root.setBackgroundResource(R.color.gray_light);

    }

    @Override
    public int getItemCount() {
        return mCollection.length + 1;
    }


    public void setPlayers(Player[] players) {
        Log.d(TAG, "setPlayers: " + players.length);
        mCollection = players;
    }


    class PlayerHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        View root;
        TextView name;
        TextView score;
        TextView position;
        TextView kills;
        TextView deaths;
        Player player;


        PlayerHolder(View view) {
            super(view);


            view.setOnLongClickListener(this);

            root = view;
            kills = view.findViewById(R.id.player_kills);
            deaths = view.findViewById(R.id.player_deaths);
            position = view.findViewById(R.id.position);
            name = view.findViewById(R.id.player_name);
            score = view.findViewById(R.id.player_score);
        }

        @Override
        public boolean onLongClick(View v) {
            mListener.onPlayerClicked(player);
            return true;
        }


    }

    public interface PlayerClicked {
        void onPlayerClicked(Player player);
    }
}
