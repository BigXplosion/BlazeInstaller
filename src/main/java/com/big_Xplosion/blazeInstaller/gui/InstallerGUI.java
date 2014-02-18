package com.big_Xplosion.blazeInstaller.gui;

import com.big_Xplosion.blazeInstaller.action.InstallType;
import com.big_Xplosion.blazeInstaller.util.OS;
import com.google.common.io.Resources;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class InstallerGUI extends JFrame
{
    JTabbedPane jtp = new JTabbedPane();
    OS os;

    public InstallerGUI()
    {
        super("BlazeInstaller");

        os = OS.getCurrentPlatform();

        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setSize(500, 300);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);

        initLogo();

        initClientPanel();

        initMCPPanel();

        this.getContentPane().add(jtp);

        this.pack();
    }

    private void initLogo()
    {
        try
        {
            InputStream imageStream = InstallerGUI.class.getClassLoader().getResourceAsStream("BlazeLoaderLogo.png");
            BufferedImage image = ImageIO.read(imageStream);
            JLabel label = new JLabel(new ImageIcon(image));
            this.getContentPane().add(label, BorderLayout.NORTH);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private void initClientPanel()
    {
        JPanel panel = new JPanel();

        JLabel message = new JLabel("Client Installation isn't implemented yet!");
        message.setForeground(Color.red);
        panel.add(message);

        this.jtp.add("Client-Install", panel);
    }

    private void initMCPPanel()
    {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        File targetDir = OS.getHomeDir();

        JPanel dirSelectPanel = new JPanel();

        JTextField selectedDirText = new JTextField();
        selectedDirText.setToolTipText("Path MCP directory.");
        selectedDirText.setEditable(false);
        selectedDirText.setColumns(20);
        selectedDirText.setText(targetDir.getAbsolutePath());
        dirSelectPanel.add(selectedDirText);

        JButton changeDirButton = new JButton();
        changeDirButton.setAction(new FileSelectionAction(targetDir, selectedDirText));
        changeDirButton.setText("...");
        dirSelectPanel.add(changeDirButton);

        JButton installButton = new JButton();
        installButton.setAction(new InstallAction(InstallType.MCP, targetDir));
        installButton.setText("Install");

        JButton optionButton = new JButton();
        optionButton.setAction(new OpenWindowAction(new MCPOptionsFrame()));
        optionButton.setText("Advanced Options");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(dirSelectPanel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(optionButton, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(installButton, gbc);

        this.jtp.add("MCP-Install", panel);
    }

    private class FileSelectionAction extends AbstractAction
    {
        public File targetDir;
        public JTextField textField;

        public FileSelectionAction(File targetDir, JTextField textField)
        {
            this.targetDir = targetDir;
            this.textField = textField;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setFileHidingEnabled(false);
            chooser.ensureFileIsVisible(targetDir);
            chooser.setSelectedFile(targetDir);
            int response = chooser.showOpenDialog(InstallerGUI.this);

            switch (response)
            {
                case JFileChooser.APPROVE_OPTION:
                    targetDir = chooser.getSelectedFile();
                    updateTextField();
                    break;
                default:
                    break;
            }
        }

        public void updateTextField()
        {
            textField.setText(targetDir.getAbsolutePath());
        }
    }

    private class InstallAction extends AbstractAction
    {
        public InstallType type;
        public File targetFile;

        public InstallAction(InstallType type, File targetFile)
        {
            this.type = type;
            this.targetFile = targetFile;
        }

        @Override
        public void actionPerformed(ActionEvent event)
        {
            try
            {
                type.install(this.targetFile);
            }
            catch (IOException e)
            {
                throw new RuntimeException("An unexpected error occurred while installing " + type.getButtonName(), e);
            }
        }
    }

    private class OpenWindowAction extends AbstractAction
    {
        public JFrame window;

        private OpenWindowAction(JFrame window)
        {
            this.window = window;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            window.setVisible(true);
        }
    }

    private class MCPOptionsFrame extends JFrame
    {
        private MCPOptionsFrame()
        {
            super("Advanced Options");
            this.setLocationRelativeTo(null);
            this.setResizable(false);

            initVersionPanel();

            this.pack();
        }

        private void initVersionPanel()
        {
            JPanel versionPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();

            TitledBorder border = BorderFactory.createTitledBorder("Version");
            border.setTitlePosition(TitledBorder.TOP);
            versionPanel.setBorder(border);

            ButtonGroup versionButtons = new ButtonGroup();

            JRadioButton latestButton = new JRadioButton();
            latestButton.setText("Use latest version");
            versionButtons.add(latestButton);

            JRadioButton githubButton = new JRadioButton();
            githubButton.setText("Use github version");
            versionButtons.add(githubButton);

            JRadioButton chooseButton = new JRadioButton();
            chooseButton.setText("Choose version");
            versionButtons.add(chooseButton);

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.WEST;
            versionPanel.add(latestButton, gbc);

            gbc.gridy = 1;
            versionPanel.add(githubButton, gbc);

            gbc.gridy = 2;
            versionPanel.add(chooseButton, gbc);

            this.getContentPane().add(versionPanel);
        }
    }
}