package com.big_Xplosion.blazeInstaller.action;

import java.io.File;
import java.io.IOException;

public interface IInstallerAction
{
	public boolean install(File file) throws IOException;
	public String getSuccesMessage();
}