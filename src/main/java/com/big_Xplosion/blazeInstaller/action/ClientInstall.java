package com.big_Xplosion.blazeInstaller.action;

import java.io.File;
import java.io.IOException;

public class ClientInstall implements IInstallerAction
{
	@Override
	public boolean install(File file) throws IOException
	{
		return false;
	}

	@Override
	public String getSuccesMessage()
	{
		return "Successfully installed BlazeLoader.";
	}
}
