package com.big_Xplosion.blazeInstaller.util;

import java.io.File;

public enum OS
{
	WINDOWS("windows", "win"),
	OSX("osx", "mac", "macos"),
	LINUX("linux", "unix"),
	SOLARIS("solaris", "sunos"),
	UNKNOWN("unknown");

	private String osName;
	private String[] aliases;

	private OS(String osName, String... aliases)
	{
		this.osName = osName;
		this.aliases = aliases;
	}

	public static OS getCurrentPlatform()
	{
		String osName = System.getProperty("os.name").toLowerCase();

		for (OS os : values())
		{
			if (osName.contains(os.osName))
				return os;

			for (String alias : os.aliases)
				if (osName.contains(alias))
					return os;
		}

		return UNKNOWN;
	}

	public static String getOSVersion()
	{
		return System.getProperty("os.version");
	}

	public static File getHomeDir()
	{
		return new File(System.getProperty("user.home"));
	}

	public static File getMinecraftDir()
	{
		String homeDir = System.getProperty("user.home");

		switch (getCurrentPlatform())
		{
			case WINDOWS :
				String appData = System.getenv("APPDATA");
				String folder = appData != null ? appData : homeDir;
				return new File(folder, ".minecraft/");
			case OSX :
				return new File(homeDir, "Library/Application Support/minecraft");
			case LINUX :
				return new File(homeDir, ".minecraft/");
			case SOLARIS :
				return new File(homeDir, ".minecraft/");
			default :
				return new File(homeDir, "minecraft/");
		}
	}
}