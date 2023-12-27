package com.cubk.nsc2.plugin;

import com.cubk.nsc2.JavaPlugin;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;

public class PluginLoader {
	private final HashMap<File, JavaPlugin> map = new HashMap<>();

	public JavaPlugin load(File file) {
		if (!(file.getName().endsWith(".jar"))) throw new RuntimeException("File have to be a Jar! " + file.getName());
		try {
			if (map.containsKey(file)) {
				throw new RuntimeException(file.getName() + " " + "Plugin already loaded.");
			}
			PluginData dataFile = new Parser(file).getObject();
			ClassLoader loader = URLClassLoader.newInstance(new URL[]{file.toURI().toURL()}, getClass().getClassLoader());
			Class<?> clazz = Class.forName(dataFile.getMain(), true, loader);
			Class<? extends JavaPlugin> instanceClass = clazz.asSubclass(JavaPlugin.class);
			Constructor<? extends JavaPlugin> instanceClassConstructor = instanceClass.getConstructor();
			JavaPlugin addon = instanceClassConstructor.newInstance();
			addon.setDescriptionFile(dataFile);
			map.put(file, addon);
			addon.onLoad();
			return addon;
		} catch (MalformedURLException e) {
			throw new RuntimeException("Failed to convert the file path to a URL.", e);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException |
				 IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException("Failed to create a new instance of the addon.", e);
		}
	}


	public JavaPlugin unload(File file) {
		if (!(file.getName().endsWith(".jar"))) throw new RuntimeException("File have to be a Jar! " + file.getName());
		if (!map.containsKey(file)) {
			throw new RuntimeException("Can't unload a addon that wasn't loaded in the first place.");
		}
		JavaPlugin addon = map.get(file);
		addon.onUnload();
		map.remove(file);
		return addon;
	}

	public void reload(File file) {
		unload(file);
		load(file);
	}
}
