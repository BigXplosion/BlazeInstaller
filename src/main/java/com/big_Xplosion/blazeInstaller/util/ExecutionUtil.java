package com.big_Xplosion.blazeInstaller.util;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExecutionUtil
{
    public static boolean compileJava(File baseFile, String targetFile, String binDir, String... classPath)
    {
        String base = baseFile.getAbsolutePath();
        String cp = base + Joiner.on(getSplitter() + base).join(classPath);

        try
        {
            Process process = Runtime.getRuntime().exec(String.format("javac -cp %s -d %s %s", cp, base + binDir.replace('/', File.separatorChar), base + targetFile.replace('/', File.separatorChar)));
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null)
            {
                if (Strings.isNullOrEmpty(line))
                    continue;

                System.out.println(line);
            }

            reader.close();

            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();

            return false;
        }
    }

    public static boolean runJava()
    {
        return false;
    }

    public static boolean runPython()
    {
        return false;
    }

    private static char getSplitter()
    {
        if (OS.getCurrentPlatform().equals(OS.WINDOWS))
            return ';';

        return ':';
    }
}