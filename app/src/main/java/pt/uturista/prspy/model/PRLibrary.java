package pt.uturista.prspy.model;

import android.util.Log;

import pt.uturista.prspy.R;


public class PRLibrary {

    private static final String TAG = "PRLibrary";

    public static int gameMode(@DataTypes.GameModes String gamemode, Boolean extended) {
        switch (gamemode) {
            case DataTypes.CONQUEST:
                return extended ? R.string.conquest_extended : R.string.conquest;
            case DataTypes.INSURGENCY:
                return extended ? R.string.insurgency_extended :R.string.insurgency;
            case DataTypes.SKIRMISH:
                return extended ? R.string.skirmish_extended :R.string.skirmish;
            case DataTypes.COOP:
                return extended ? R.string.coop_extended :R.string.coop;
            case DataTypes.CNC:
                return extended ? R.string.cnc_extended :R.string.cnc;
            case DataTypes.VEHICLE_WARFARE:
                return extended ? R.string.vehicles_extended :R.string.vehicles;
            case DataTypes.OBJECTIVE:
                return extended ? R.string.objective_extended : R.string.objective;
            default:
                Log.e(TAG, "Unknown GameMode: " + gamemode);
                return R.string.unknown;

        }
    }

    public static int gameLayer(@DataTypes.GameLayers int layer, Boolean extended) {
        switch (layer) {
            case DataTypes.STANDARD:
                return  extended ? R.string.standard_extended : R.string.standard;
            case DataTypes.INFANTRY:
                return extended ? R.string.infantry_extended : R.string.infantry;
            case DataTypes.ALTERNATIVE:
                return extended ? R.string.alternative_extended : R.string.alternative;
            case DataTypes.LARGE:
                return extended ? R.string.large_extended :  R.string.large;
            default:
                Log.e(TAG, "Unknown GameLayer: " + layer);
                return R.string.unknown;

        }
    }

    public static int factionName(String faction) {
        switch (faction.toLowerCase()) {
            case "nl":
                return R.string.faction_nl;
            case "us":
                return R.string.faction_us;
            case "arf":
                return R.string.faction_arf;
            case "fsa":
                return R.string.faction_fca;
            case "mec":
                return R.string.faction_mec;
            case "usa":
                return R.string.faction_usa;
            case "ru":
                return R.string.faction_ru;
            case "gb":
                return R.string.faction_gb;
            case "fr":
                return R.string.faction_fr;
            case "chinsurgent":
                return R.string.faction_chinsurgent;
            case "hamas":
                return R.string.faction_hamas;
            case "meinsurgent":
                return R.string.faction_meinsurgent;
            case "insurgent":
                return R.string.faction_meinsurgent;
            case "taliban":
                return R.string.faction_taliban;
            case "ch":
                return R.string.faction_ch;
            case "idf":
                return R.string.faction_idf;
            case "ger":
                return R.string.faction_ger;
            case "vnnva":
                return R.string.faction_vn;
            case "vnusa":
                return R.string.faction_usa;
            case "vnusmc":
                return R.string.faction_usa;
            case "cf":
                return R.string.faction_cf;
            default:
                Log.e(TAG, "Unknown Faction: " + faction);
                return R.string.unknown;
        }
    }
}
