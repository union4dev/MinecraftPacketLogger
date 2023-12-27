package com.cubk.nsc2.plugin;


import java.io.File;
import java.util.Objects;
import java.util.function.Consumer;

public class PluginAPI {

    private static PluginLoader loader;
    private String outName;

    private PluginAPI(PluginLoader loader) {
        PluginAPI.loader = loader;
    }

    public static PluginAPI getApi(Consumer<PluginLoader> consumer, String outName) {
        consumer.accept(loader = new PluginLoader());
        PluginAPI api = new PluginAPI(loader);
        api.outName = outName;
        return api;
    }

    public PluginLoader getLoader() {
        return loader;
    }

    public void loadAll(File folder) {
        try {
            if (!folder.exists())
                folder.mkdir();

            for (File fileIndex : Objects.requireNonNull(folder.listFiles())) {
                if (fileIndex.getName().endsWith(".jar")) {
                    loader.load(fileIndex);
                    System.out.println(outName + " " + "Loaded: " + fileIndex.getName());
                }
            }
        } catch (NullPointerException e) { // 通常是没有这个文件夹

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
