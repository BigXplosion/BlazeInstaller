package com.big_Xplosion.blazeInstaller.util;

public enum InstallType
{
	CLIENT("Install Client", "Install a new profile on the minecraft launcher."),
	MCP("Install MCP", "install BlazeLoader in a MCP environlent (only for developers).");

	private String name;
	private String desc;

	private InstallType(String name, String desc)
	{
		this.name = name;
		this.desc = desc;
	}
}