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
	public boolean isPathValid(File targetFile)
	{
		return true;
	}

	@Override
	public String getSuccessMessage()
	{
		return "Successfully installed BlazeLoader.";
	}

	@Override
	public String getFileErrorMessage(File targetFile)
	{
		if (targetFile.exists())
			return "The directory is missing a launcher profile. Please run the minecraft launcher first";
		else
			return "There is no minecraft directory set up. Either choose an alternative, or run the minecraft launcher to create one";

	}
}
