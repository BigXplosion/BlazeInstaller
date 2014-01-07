package com.big_Xplosion.blazeInstaller.action;

import java.io.File;
import java.io.IOException;

import com.google.common.base.Throwables;

public enum InstallType
{
	CLIENT("Client-Install", "Install a new profile on the minecraft launcher.", ClientInstall.class),
	MCP("MCP-Install", "install BlazeLoader in a MCP environlent (only for developers).", MCPInstall.class);

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

	public boolean isPathValid(File targetFile)
	{
		return actionClass.isPathValid(targetFile);
	}

	public String getSuccessMessage()
	{
		return actionClass.getSuccesMessage();
	}

	public String getFileError(File targetFile)
	{
		return actionClass.getFileErrorMessage(targetFile);
	}

	public String getButtonName()
	{
		return name;
	}

	public String getDesc()
	{
		return desc;
	}
}