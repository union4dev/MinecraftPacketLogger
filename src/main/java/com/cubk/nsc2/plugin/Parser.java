package com.cubk.nsc2.plugin;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Parser {

    private PluginData data = null;

    public Parser(File file) {
        try {
            ZipFile zipFile = new ZipFile(file);

            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            PluginData pluginJson = null;

            while (entries.hasMoreElements() && pluginJson == null) {
                ZipEntry entry = entries.nextElement();

                if (!entry.isDirectory() && entry.getName().equals("plugin.json")) {
                    InputStream stream = zipFile.getInputStream(entry);
                    try {
                        pluginJson = new Gson().fromJson(new InputStreamReader(stream), PluginData.class);
                    } catch (JsonParseException jsonParseException) {
                        throw new RuntimeException("Failed to parse JSON:", jsonParseException);
                    }
                }
            }

            if (pluginJson == null) {
                zipFile.close();
                throw new RuntimeException("Failed to find plugin.json in the root of the jar.");
            }

            zipFile.close();
            this.data = pluginJson;
        } catch (IOException e) {
            throw new RuntimeException("Failed to open the jar as a zip:", e);
        }
    }

    public PluginData getObject() {
        return data;
    }
}
