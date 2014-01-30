package com.big_Xplosion.blazeInstaller.util;

import com.google.common.base.Joiner;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExecutionUtil
{
    public static boolean compileJava(File baseFile, String targetFile, String binDir, String... classPath)
    {
        String base = baseFile.getAbsolutePath();
        String cp = base + Joiner.on(getSplitter() + base).join(classPath).replace('/', File.separatorChar);

        try
        {
            Process process = Runtime.getRuntime().exec(String.format("javac -cp %s -d %s %s", cp, base + binDir.replace('/', File.separatorChar), base + targetFile.replace('/', File.separatorChar)));
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;

            while ((line = reader.readLine()) != null)
                System.out.println(line);

            reader.close();

            return true;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static boolean runJava(File baseFile, String targetFile, String args, String... classPath)
    {
        String base = baseFile.getAbsolutePath();
        String cp = base + Joiner.on(getSplitter() + base).join(classPath);

        try
        {
            Process process = Runtime.getRuntime().exec(String.format("java -cp %s %s %s", cp, targetFile.replace('/', File.separatorChar), args));
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;

            while ((line = reader.readLine()) != null)
                System.out.println(line);

            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            while ((line = reader.readLine()) != null)
                System.out.println(line);

            reader.close();

            return true;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static boolean runShellOrBat(File target, String script)
    {
        OS os = OS.getCurrentPlatform();
        File runtime = new File(target, "runtime");

        try
        {
            Process process;

            if (os.equals(OS.WINDOWS))
                process = Runtime.getRuntime().exec(target.getAbsolutePath() + File.separator + script + ".bat");
            else
            {
                process = Runtime.getRuntime().exec("python " + runtime.getAbsolutePath() + File.separator + script + ".py \"$@\"");
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;

            while ((line = reader.readLine()) != null)
                System.out.println(line);

            reader.close();

            return true;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private static char getSplitter()
    {
        if (OS.getCurrentPlatform().equals(OS.WINDOWS))
            return ';';

        return ':';
    }

    //how do I force my mac to use the custom python shipped with MCP? It always tries to use the normal one.
}