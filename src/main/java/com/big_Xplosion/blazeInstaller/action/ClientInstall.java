package com.big_Xplosion.blazeInstaller.action;

import java.io.File;

public class ClientInstall implements IInstallerAction
{
	@Override
	public boolean install(File file)
	{
		return false;
	}

	@Override
	public String getSuccesMessage()
	{
		return "Successfully installed BlazeLoader.";
	}
}
