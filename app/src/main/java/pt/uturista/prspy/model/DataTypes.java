package pt.uturista.prspy.model;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DataTypes {
    public final static String INSURGENCY = "gpm_insurgency";
    public final static String SKIRMISH = "gpm_skirmish";
    public final static String COOP = "gpm_coop";
    public final static String CONQUEST = "gpm_cq";
    public final static String CNC = "gpm_cnc";
    public final static String VEHICLE_WARFARE = "gpm_vehicles";
    public static final String OBJECTIVE = "gpm_objective";
    public final static String UNKNOWN_MODE = "";
    public final static int INFANTRY = 16;
    public final static int ALTERNATIVE = 32;
    public final static int STANDARD = 64;
    public final static int LARGE = 128;
    public final static int UNKNOWN_LAYER = 0;
    final public static int Opfor = 1;
    final public static int Blufor = 2;


    @GameModes
    public static String getGameMode(String gameMode) {
        switch (gameMode) {
            case CONQUEST:
                return CONQUEST;
            case INSURGENCY:
                return INSURGENCY;
            case SKIRMISH:
                return SKIRMISH;
            case COOP:
                return COOP;
            case CNC:
                return CNC;
            case VEHICLE_WARFARE:
                return VEHICLE_WARFARE;
            default:
                return UNKNOWN_MODE;
        }
    }

    @GameLayers
    public static int getGameLayer(String gameMode) {
        switch (gameMode) {
            case "64":
                return STANDARD;
            case "16":
                return INFANTRY;
            case "32":
                return ALTERNATIVE;
            case "128":
                return LARGE;
            default:
                return UNKNOWN_LAYER;
        }
    }

    @GameLayers
    public static int getGameLayer(int gameMode) {
        switch (gameMode) {
            case 64:
                return STANDARD;
            case 16:
                return INFANTRY;
            case 32:
                return ALTERNATIVE;
            case 128:
                return LARGE;
            default:
                return UNKNOWN_LAYER;
        }
    }

    @Teams
    public static int getTeam(int team) {
        switch (team) {
            case Opfor:
                return Opfor;
            case Blufor:
                return Blufor;
            default:
                return Blufor;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({INSURGENCY, SKIRMISH, COOP, CONQUEST, CNC, VEHICLE_WARFARE, OBJECTIVE, UNKNOWN_MODE})
    public @interface GameModes {
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({INFANTRY, ALTERNATIVE, STANDARD, LARGE, UNKNOWN_LAYER})
    public @interface GameLayers {
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Opfor, Blufor})
    public @interface Teams {
    }
}
