package esgi.yvox.plugins;

import esgi.yvox.annotation.UUID_Processor;
import esgi.yvox.sdk.*;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.JarFile;

/**
 * Created by ostro on 13/06/2016.
 */
public class PluginsLoader {

    private ArrayList<String> files;

    private ArrayList<Class> classLanguagePlugins;

    public PluginsLoader(){
        classLanguagePlugins = new ArrayList();
        files = new ArrayList<>();
    }

    public PluginsLoader(ArrayList<String> files) {
        this();
        this.files = files;
    }

    public ArrayList<PluginsInfo> loadAllPlugins() throws Exception {
        ArrayList<PluginsInfo> pluginsInfos = new ArrayList<>();
        LanguagePlugins[] langagePlug = loadAllLanguagePlugins();
        for (int i = 0; i < langagePlug.length; i++) {
            pluginsInfos.add(langagePlug[i]);
        }
        return pluginsInfos;
    }

    public LanguagePlugins[] loadAllLanguagePlugins() throws Exception {

        initPlugin();

        LanguagePlugins[] langPlugins = new LanguagePlugins[classLanguagePlugins.size()];

        for (int i = 0;i < langPlugins.length;i++){
            langPlugins[i] = (LanguagePlugins) classLanguagePlugins.get(i).newInstance();
        }

        return langPlugins;
    }

    public ArrayList<String> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<String> files) {
        this.files = files;
    }

    private void initPlugin() throws Exception {
        if (files == null){
            throw new Exception("File not defined");
        }

        Path[] f = new Path[files.size()];
        URLClassLoader loader;
        String string = "";
        Enumeration enumeration;
        Class clazz = null;

        for (int i = 0; i < f.length;i++){
            f[i] = Paths.get(files.get(i));
            if (f[i] == null){
                break;
            }

            loader = new URLClassLoader(new URL[]{f[i].toUri().toURL()});
            JarFile jarFile = new JarFile(String.valueOf(f[i].toAbsolutePath()));
            enumeration = jarFile.entries(); // Get content of jar

            while (enumeration.hasMoreElements()){
                string = enumeration.nextElement().toString();

                if (string.length() > 5 && string.substring(string.length()-6).compareTo(".class") == 0){
                    string = string.substring(0, string.length()-6);
                    string = string.replaceAll("/",".");

                    clazz = Class.forName(string, true, loader);
                    Annotation[] clazzAnnotations  = clazz.getAnnotations();
                    System.out.println(clazzAnnotations.length);
                    for (int k = 0; k < clazzAnnotations.length; k++) {
                        if (clazzAnnotations[k].toString().contains("YVOXPlugin")) {
                            for (int j = 0; j < clazz.getInterfaces().length; j++) {
                                if (clazz.getInterfaces()[j].getName().toString().equals("esgi.yvox.sdk.LanguagePlugins")) {
                                    classLanguagePlugins.add(clazz);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
