package com.cubk.nsc2.gui;

import com.cubk.nsc2.packet.PacketData;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DebugFrame extends JFrame {
    private final JTable packetTable;
    private final JTable packetInfoTable;
    private final JTextArea logTextArea;
    private final JTextArea otherInfoTextArea;
    private final JTextField searchField;
    private final JCheckBox clientCheckBox;
    private final JCheckBox serverCheckBox;
    private final JButton startPauseButton;
    private JLabel data;
    private final Map<Integer, List<PacketData.Data>> dataMap = new HashMap<>();
    private boolean isRunning = false;

    public DebugFrame() {
        super("N.S.C. 2.0");
        String[] columnNames = {"#", "Event Type", "Event Data", "Side", "Timestamp"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        packetTable = new JTable(tableModel);

        packetTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = packetTable.getSelectedRow();
                    DefaultTableModel infoTableModel = (DefaultTableModel) packetInfoTable.getModel();
                    infoTableModel.setRowCount(0);
                    for (PacketData.Data data : dataMap.get(Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString()))) {
                        infoTableModel.addRow(new Object[]{data.type.getSimpleName(), data.name, data.value});
                    }
                }
            }
        });

        searchField = new JTextField(20);
        clientCheckBox = new JCheckBox("Client");
        clientCheckBox.setSelected(true);
        serverCheckBox = new JCheckBox("Server");
        serverCheckBox.setSelected(false);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchPackets());

        String[] infoColumnNames = {"Data Type", "Data Name", "Data"};
        DefaultTableModel infoTableModel = new DefaultTableModel(infoColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        packetInfoTable = new JTable(infoTableModel);

        JTabbedPane tabbedPane = new JTabbedPane();
        logTextArea = new JTextArea();
        logTextArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        otherInfoTextArea = new JTextArea();

        tabbedPane.addTab("Log", new JScrollPane(logTextArea));
        tabbedPane.addTab("Packet Information", new JScrollPane(packetInfoTable));
        tabbedPane.addTab("Other Information", new JScrollPane(otherInfoTextArea));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(createUpperPanel());
        splitPane.setBottomComponent(tabbedPane);
        splitPane.setResizeWeight(0.7);

        startPauseButton = new JButton("Start");
        startPauseButton.addActionListener(e -> {
            if (isRunning) {
                stop();
            } else {
                run();
            }
        });

        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);
        add(startPauseButton, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private JPanel createUpperPanel() {
        JPanel upperPanel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(searchField);
        topPanel.add(clientCheckBox);
        topPanel.add(serverCheckBox);
        topPanel.add(new JButton("Search"));
        topPanel.add(data = new JLabel("Data"));
        upperPanel.add(topPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(packetTable);
        scrollPane.setAutoscrolls(true);
        upperPanel.add(scrollPane, BorderLayout.CENTER);

        return upperPanel;
    }


    private void searchPackets() {
        String searchText = searchField.getText().toLowerCase();

        // TODO
    }

    private void run() {
        isRunning = true;
        startPauseButton.setText("Pause");
    }

    private void stop() {
        isRunning = false;
        startPauseButton.setText("Start");
    }

    public void addPacket(PacketData packetData) {
        if (!isRunning) return;

        if (!serverCheckBox.isSelected() && !packetData.outgoing) return;
        if (!clientCheckBox.isSelected() && packetData.outgoing) return;

        DefaultTableModel tableModel = (DefaultTableModel) packetTable.getModel();
        String[] rowData = {String.valueOf(tableModel.getRowCount() + 1), packetData.packetClass.getSimpleName(), packetData.packetName, packetData.outgoing ? "Outgoing" : "Incoming", String.valueOf(packetData.time)};
        tableModel.addRow(rowData);

        dataMap.put(tableModel.getRowCount(), packetData.getDataList());

        int rowCount = packetTable.getRowCount();
        if (rowCount > 0) {
            Rectangle rect = packetTable.getCellRect(rowCount - 1, 0, true);
            packetTable.scrollRectToVisible(rect);
        }
    }

    public void log(String message, String suffix) {
        logTextArea.append(String.format("[%s][%s] %s", System.currentTimeMillis(), suffix, message) + "\n");
    }

    public void setOtherInfo(String info) {
        otherInfoTextArea.setText(info);
    }

    public void setData(String info) {
        data.setText(info);
    }

}
