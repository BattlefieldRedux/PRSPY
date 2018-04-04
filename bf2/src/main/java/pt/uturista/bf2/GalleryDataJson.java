package pt.uturista.bf2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GalleryDataJson implements Serializable {
    public MapJson[] maps;
    public HashMap<String, VehicleJson> vehicles;

    public GalleryDataJson(){
        maps = new MapJson[0];
        vehicles = new HashMap<>();
    }

}
