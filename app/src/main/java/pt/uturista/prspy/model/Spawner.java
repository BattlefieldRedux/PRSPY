package pt.uturista.prspy.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

import pt.uturista.bf2.SpawnerJson;
import pt.uturista.bf2.VehicleJson;


public class Spawner implements Parcelable {

    private final int minDelay;
    private final int maxDelay;
    private final boolean startDelay;
    private
    @DataTypes.Teams
    final int team;
    private final int quantity;
    private final String name;
    private final String key;
    private final String icon;

    public Spawner(SpawnerJson spawner, HashMap<String, VehicleJson> vehicles) {
        minDelay = spawner.minDelay;
        maxDelay = spawner.maxDelay;
        startDelay = spawner.startDelay;
        team = spawner.team;
        quantity = spawner.quantity;

        if(vehicles != null){
            key = spawner.vehicleKey;
            name = vehicles.get(spawner.vehicleKey).name;
            icon = vehicles.get(spawner.vehicleKey).icon;

        }else{
            key = null;
            name = null;
            icon = null;
        }
    }


    public int getQuantity() {
        return this.quantity;
    }

    @DataTypes.Teams
    public int getTeam() {
        return this.team;
    }

    public int getMinDelay() {
        return minDelay;
    }

    public int getMaxDelay() {
        return maxDelay;
    }

    public boolean hasStartDelay() {
        return startDelay;
    }

    public String getVehicleName() {
        return name;
    }
    public String getVehicleKey() {
        return key;
    }
    public String getVehicleIcon() {
        return icon;
    }

    protected Spawner(Parcel in) {
        minDelay = in.readInt();
        maxDelay = in.readInt();
        startDelay = in.readByte() != 0;
        team = in.readInt() == 1 ?  DataTypes.Opfor:DataTypes.Blufor;
        quantity = in.readInt();
        key = in.readString();
        name = in.readString();
        icon = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(minDelay);
        dest.writeInt(maxDelay);
        dest.writeByte((byte) (startDelay ? 1 : 0));
        dest.writeInt(team);
        dest.writeInt(quantity);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(icon);
    }


    public static final Creator<Spawner> CREATOR = new Creator<Spawner>() {
        @Override
        public Spawner createFromParcel(Parcel in) {
            return new Spawner(in);
        }

        @Override
        public Spawner[] newArray(int size) {
            return new Spawner[size];
        }
    };


}
