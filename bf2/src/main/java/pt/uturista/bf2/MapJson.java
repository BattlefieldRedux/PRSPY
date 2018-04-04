package pt.uturista.bf2;

import java.io.Serializable;
import java.util.List;

public class MapJson implements Serializable {
    public String key;
    public String name;
    public int size;
    public LayoutJson[] layouts;
    public String color;
}
