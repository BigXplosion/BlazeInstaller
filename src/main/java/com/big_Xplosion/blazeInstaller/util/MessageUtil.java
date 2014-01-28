package com.big_Xplosion.blazeInstaller.util;

import javax.swing.*;

public class MessageUtil
{
    public static boolean gui = Boolean.parseBoolean(System.getProperty("bli.gui"));

    public static void postMessage(String text, String title, int type)
    {
        if (!gui)
            System.out.println("> " + text);
        else
            JOptionPane.showMessageDialog(null, text, title, type);
    }

    public static void postErrorMessage(String text)
    {
        postMessage(text, "ERROR", JOptionPane.ERROR_MESSAGE);
    }
}
