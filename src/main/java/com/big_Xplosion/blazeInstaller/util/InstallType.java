package com.big_Xplosion.blazeInstaller.util;

import java.io.File;
import java.io.IOException;

import com.big_Xplosion.blazeInstaller.action.ClientInstall;
import com.big_Xplosion.blazeInstaller.action.IInstallerAction;
import com.big_Xplosion.blazeInstaller.action.MCPInstall;
import com.google.common.base.Throwables;

public enum InstallType
{
	CLIENT("Install Client", "Install a new profile on the minecraft launcher.", ClientInstall.class),
	MCP("Install MCP", "install BlazeLoader in a MCP environlent (only for developers).", MCPInstall.class);

	private String name;
	private String desc;
	private IInstallerAction actionClass;

	private InstallType(String name, String desc, Class<? extends IInstallerAction> actionClass)
	{
		this.name = name;
		this.desc = desc;

		try
		{
			this.actionClass = actionClass.newInstance();
		}
		catch (Exception e)
		{
			throw Throwables.propagate(e);
		}
	}

	public boolean install(File targetFile) throws IOException
	{
		return actionClass.install(targetFile);
	}

	public String getTabName()
	{
		return name;		//For UI later
	}

	public String getDesc()
	{
		return desc;		//For UI later
	}
}