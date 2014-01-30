package com.big_Xplosion.blazeInstaller.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class FileUtil
{
    public static void createDirStructureForFile(File file)
    {
        if (!file.exists())
        {
            String[] dirs = file.getAbsolutePath().split(File.separator);
            String newFile = "";

            for (int index = 0; index < dirs.length - 1; index++)
                newFile += "/" + dirs[index];

            File currFile = new File(newFile);

            if (!currFile.exists())
                if (!currFile.mkdirs())
                    throw new RuntimeException("Could not create directory: " + currFile.getAbsolutePath());
        }
    }

    public static void copyAndOverwriteSources(File source, File destination) throws IOException
    {
        if (source == null || !source.exists() || !source.isDirectory() || destination == null || !destination.exists() || !destination.isDirectory())
            throw new IllegalArgumentException(String.format("source: % and destination: %s must be directories", source.getAbsolutePath(), destination.getAbsolutePath()));

        Collection<File> contents = FileUtils.listFiles(source, new String[] {"java"}, true);

        for (File f : contents)
        {
            File dst = new File(destination, new File(f.toURI().relativize(source.toURI())).getCanonicalPath());
            System.out.println(f.toURI().relativize(destination.toURI()).toString());
        }
    }

    @SuppressWarnings("unused")
    private static String arrayToString(String[] array)
    {
        StringBuilder builder = new StringBuilder((array.length * 2) + 1);
        int numProcessed = 0;
        builder.append("[");

        for (String str : array)
        {
            numProcessed++;
            builder.append(str);
            if (numProcessed < array.length)
                builder.append(",");
        }

        builder.append("]");

        return builder.toString();
    }
}
