package com.big_Xplosion.blazeInstaller.util;

import java.io.File;
import java.io.IOException;

public class JavaUtil
{
	public static boolean compileJavaFile(File targetFile, File outputFile)
	{
		try
		{
			Runtime.getRuntime().exec(String.format("javac %s -d %s", targetFile.getAbsolutePath(), outputFile.getAbsolutePath()));

			return true;
		}
		catch (IOException e)
		{
			e.printStackTrace();

			return false;
		}
	}
}