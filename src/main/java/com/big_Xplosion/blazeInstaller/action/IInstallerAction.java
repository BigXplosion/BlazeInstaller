package com.big_Xplosion.blazeInstaller.action;

import java.io.File;

public interface IInstallerAction
{
	public boolean install(File file);
	public String getSuccesMessage();
}