package com.big_Xplosion.blazeInstaller.util;

import com.google.common.io.Files;
import com.google.common.io.InputSupplier;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ExtractUtil
{
    public static boolean extractZip(File zip, File outputFile)
    {
        try
        {
            ZipFile zipFile = new ZipFile(zip);

            for (Enumeration<? extends ZipEntry> e = zipFile.entries(); e.hasMoreElements(); )
            {
                ZipEntry entry = e.nextElement();

                if (entry.isDirectory())
                    continue;

                final InputStream inStream = zipFile.getInputStream(entry);

                InputSupplier<InputStream> supplier = new InputSupplier<InputStream>()
                {
                    @Override
                    public InputStream getInput() throws IOException
                    {
                        return inStream;
                    }
                };

                File currFile = new File(outputFile, entry.getName());
                FileUtil.createDirStructureForFile(currFile);
                Files.copy(supplier, currFile);
            }

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();

            return false;
        }
    }
}