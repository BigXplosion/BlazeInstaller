package com.big_Xplosion.blazeInstaller.gui;

import javax.swing.*;

public class InstallerGUI extends JFrame
{
    JTabbedPane jtp = new JTabbedPane();

    public InstallerGUI()
    {
        super ("BlazeInstaller");
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setSize(600, 300);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        this.add(this.jtp);
    }
}