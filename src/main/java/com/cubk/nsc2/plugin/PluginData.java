package com.cubk.nsc2.plugin;


import lombok.Getter;

@Getter
public class PluginData {

	private final String main;
	private final String name;
	private final String version;

	public PluginData(String main, String name, String version) {
		this.main = main;
		this.name = name;
		this.version = version;
	}

}
