package pt.uturista.flags;


import com.sun.codemodel.JArray;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WorldGenerator {


    private static final int UNKNOWN = 0;
    private static final int ASIA = 1;
    private static final int EUROPE = 2;
    private static final int AFRICA = 3;
    private static final int OCEANIA = 4;
    private static final int AMERICAS = 5;

    public static void main(String[] args) {
        generateTldListClass(null, false);
    }

    public static void generateTldListClass(final File outputDir, final boolean useSavedVersion) {


        try {

            BufferedReader reader1 = new BufferedReader(new FileReader(new File("E:\\Pessoal\\Projectos\\PRSPY2\\flags\\src\\main\\res\\countries.csv")));


            JCodeModel codeModel = new JCodeModel();
            codeModel.ref("import pt.uturista.flags.R");
            JPackage jp = codeModel._package("pt.uturista.flags");
            JDefinedClass jc = jp._class("World");

            jc.field(JMod.PUBLIC | JMod.FINAL | JMod.STATIC, int.class, "UNKNOWN", JExpr.lit(UNKNOWN));
            jc.field(JMod.PUBLIC | JMod.FINAL | JMod.STATIC, int.class, "ASIA", JExpr.lit(ASIA));
            jc.field(JMod.PUBLIC | JMod.FINAL | JMod.STATIC, int.class, "EUROPE", JExpr.lit(EUROPE));
            jc.field(JMod.PUBLIC | JMod.FINAL | JMod.STATIC, int.class, "AFRICA", JExpr.lit(AFRICA));
            jc.field(JMod.PUBLIC | JMod.FINAL | JMod.STATIC, int.class, "OCEANIA", JExpr.lit(OCEANIA));
            jc.field(JMod.PUBLIC | JMod.FINAL | JMod.STATIC, int.class, "AMERICAS", JExpr.lit(AMERICAS));

            JClass mapClass = codeModel.ref(Map.class);
            JClass mapNarrowedClass = mapClass.narrow(codeModel.ref(String.class), codeModel.ref(Country.class));

            JClass hashMapClass = codeModel.ref(HashMap.class);
            JClass hashMapNarrowedClass = hashMapClass.narrow(codeModel.ref(String.class), codeModel.ref(Country.class));


            JFieldVar worldMap = jc.field(JMod.PUBLIC | JMod.FINAL | JMod.STATIC, mapNarrowedClass, "WORLD_MAP", JExpr._new(hashMapNarrowedClass));

            JArray world = JExpr.newArray(codeModel.ref(Country.class));


            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("E:\\Pessoal\\Projectos\\PRSPY2\\flags\\src\\main\\res\\values\\countries.xml")));
            writer.write("<resources>");
            writer.newLine();

            String line;
            while ((line = reader1.readLine()) != null) {
                String[] entries;
                entries = line.split(",");

                if (entries.length < 6)
                    continue;

                String code = entries[0].toLowerCase();
                String enName = entries[1];
                String domain = entries[2].toLowerCase();
                String code2 = entries[3].toLowerCase();
                String code3 = entries[4].toLowerCase();
                String region = getRegion(entries[5].toLowerCase());


                // new Country(String code2, String code3, String domain, @StringRes int name, @DrawableRes int flag, int continent)
                JInvocation obj = JExpr._new(codeModel.ref(Country.class));
                //

                obj.arg(code); // Code-2
                obj.arg(code2); // Code-2
                obj.arg(code3); // Code-3
                obj.arg(domain); // Domain
                obj.arg(JExpr.direct("R.string." + code)); // Name
                obj.arg(JExpr.direct("R.drawable." + code)); // Flag
                obj.arg(JExpr.direct(region)); // Continent

                JFieldVar country = jc.field(JMod.PUBLIC | JMod.FINAL | JMod.STATIC, Country.class, code.toUpperCase(), obj);

                jc.init().add(worldMap.invoke("put").arg(JExpr.lit(code2)).arg(country ));
                world.add(country);

                writer.write(String.format("    <string name=\"%s\">%s</string>", code, enName));
                writer.newLine();
            }

            // new Country(String code2, String code3, String domain, @StringRes int name, @DrawableRes int flag, int continent)
            JInvocation obj = JExpr._new(codeModel.ref(Country.class));
            //

            obj.arg("united_nations"); // Code-2
            obj.arg(""); // Code-2
            obj.arg(""); // Code-3
            obj.arg(""); // Domain
            obj.arg(JExpr.direct("R.string.united_nations" )); // Name
            obj.arg(JExpr.direct("R.drawable.united_nations")); // Flag
            obj.arg(JExpr.direct("UNKNOWN")); // Continent

            JFieldVar country = jc.field(JMod.PUBLIC | JMod.FINAL | JMod.STATIC, Country.class, "united_nations".toUpperCase(), obj);

            jc.field(JMod.PUBLIC | JMod.FINAL | JMod.STATIC, Country[].class, "WORLD", world);




            // Generate the code
            codeModel.build(new File("E:\\Pessoal\\Projectos\\PRSPY2\\flags\\src\\main\\java"));

            writer.write("<string name=\"united_nations\">United Nations</string>");
            writer.write("</resources>");
            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JClassAlreadyExistsException e) {
            e.printStackTrace();
        }


    }

    private static String getRegion(String region) {
        switch (region) {
            case "asia":
                return "ASIA";

            case "europe":
                return "EUROPE";

            case "africa":
                return "AFRICA";

            case "oceania":
                return "OCEANIA";

            case "americas":
                return "AMERICAS";

            default:
                return "UNKNOWN";
        }
    }
}
