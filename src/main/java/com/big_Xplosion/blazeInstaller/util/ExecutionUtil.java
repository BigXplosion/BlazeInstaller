package com.big_Xplosion.blazeInstaller.util;

import com.google.common.base.Joiner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ExecutionUtil
{
    public static boolean compileJava(File baseFile, String targetFile, String binDir, String... classPath)
    {
        String base = baseFile.getAbsolutePath();
        String cp = base + Joiner.on(getSplitter() + base).join(classPath).replace('/', File.separatorChar);

        try
        {
            Process process = Runtime.getRuntime().exec(String.format("javac -cp %s -d %s %s", cp, base + binDir.replace('/', File.separatorChar), base + targetFile.replace('/', File.separatorChar)));
            printInStream(process.getErrorStream());
            printInStream(process.getInputStream());

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
            printInStream(process.getErrorStream());
            printInStream(process.getInputStream());

            return true;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void runShellOrBat(File target, String script)
    {
        OS os = OS.getCurrentPlatform();

        try
        {
            Process process;

            if (os.equals(OS.WINDOWS))
            {
                ProcessBuilder pb = new ProcessBuilder("cmd.exe", script + ".bat");
                pb.directory(target);
                pb.redirectErrorStream(true);
                process = pb.start();
            }
            else
            {
                ProcessBuilder pb = new ProcessBuilder("/bin/bash", script + ".sh");
                pb.directory(target);
                pb.redirectErrorStream(true);
                process = pb.start();
            }

            printInStream(process.getInputStream());
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

    private static void printInStream(InputStream stream)
    {
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;

            while ((line = reader.readLine()) != null)
                System.out.println(line);

            reader.close();
        }
        catch (IOException e)
        {
            // pass, there is no problem if this doesn't work.
        }
    }
}