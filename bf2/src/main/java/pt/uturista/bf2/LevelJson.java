package pt.uturista.bf2;

public class LevelJson {
    public String Name;
    public String Key;
    public int Resolution;
    public int Size;
    public String Color;
    public LayoutIndex[] Layouts;

    public static class LayoutIndex {
        public String Key;
        public int Value;
    }
}
