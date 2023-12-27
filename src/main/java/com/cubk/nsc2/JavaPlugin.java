package com.cubk.nsc2;

import com.cubk.nsc2.plugin.PluginData;

public interface JavaPlugin {

    ThreadLocal<PluginData> descriptionFile = new ThreadLocal<>();

    void onLoad();

    void onUnload();

    default PluginData getDescriptionFile() {
        return descriptionFile.get();
    }

    default void setDescriptionFile(PluginData descriptionFile) {
        if (this.descriptionFile.get() != null) {
            throw new RuntimeException("Can't set the description file. Its already set!");
        }
        this.descriptionFile.set(descriptionFile);
    }

}