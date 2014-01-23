package com.big_Xplosion.blazeInstaller.util;

import java.io.File;
import java.io.IOException;

public class JavaUtil
{
    public static boolean compileJavaFile(File targetFile, File classPath, File srcPath, File outputFile)
    {
        try
        {
            Runtime.getRuntime().exec(String.format("javac -target 1.6 -source 1.6 -cp %s -sourcepath %s -d %s %s", classPath.getAbsolutePath(), srcPath.getAbsolutePath(), outputFile.getAbsolutePath(), targetFile.getAbsolutePath()));

            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();

            return false;
        }
    }
}