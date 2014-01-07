package com.big_Xplosion.blazeInstaller.action;

import java.io.File;
import java.io.IOException;

public interface IInstallerAction
{
	public boolean install(File targetFile) throws IOException;
	public boolean isPathValid(File targetFile);
	public String getSuccesMessage();
	public String getFileErrorMessage(File targetFile);
}