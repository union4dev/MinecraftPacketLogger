package com.cubk.nsc2.gui;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

public class AddressSwapperFrame extends JFrame {
    @Getter
    private static JCheckBox use = new JCheckBox("Use Address Swapper");
    @Getter
    private static JTextField originalHostField;
    @Getter
    private static JTextField targetServerField;
    @Getter
    private static JTextField targetPortField;

    public AddressSwapperFrame() {
        setTitle("Address Swapper");
        setLayout(new GridBagLayout());
        setAlwaysOnTop(true);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        originalHostField = new JTextField("mc.hypixel.net");
        targetServerField = new JTextField();
        targetPortField = new JTextField("25565");

        Dimension textFieldDimension = new Dimension(200, 25);
        originalHostField.setPreferredSize(textFieldDimension);
        targetServerField.setPreferredSize(textFieldDimension);
        targetPortField.setPreferredSize(textFieldDimension);

        JButton confirmButton = new JButton("Dispose");
        confirmButton.addActionListener(e -> dispose());

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Enable"), gbc);
        gbc.gridx = 1;
        add(use, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Original Host:"), gbc);
        gbc.gridx = 1;
        add(originalHostField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Target Server:"), gbc);
        gbc.gridx = 1;
        add(targetServerField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Target Port:"), gbc);
        gbc.gridx = 1;
        add(targetPortField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        add(confirmButton, gbc);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
