package zad1;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class CountryTable {
    private String fileName;

    public CountryTable(String fileName) {
        this.fileName = fileName;
    }

    public JTable create() throws Exception {
        List<String[]> data = Files.readAllLines(Paths.get(fileName))
                .stream()
                .map(line -> line.split("\t"))
                .collect(Collectors.toList());

        for (String[] row : data) {
            if (row.length > 3) {
                row[3] = new ImageIcon(row[3]).toString();
            }
        }

        String[] columnNames = data.get(0);
        data.remove(0);

       
        DefaultTableModel model = new DefaultTableModel(data.toArray(new String[0][0]), columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if(columnIndex == 2) {
                    return Integer.class;
                } else if (columnIndex == 3) {
                    return ImageIcon.class;
                }
                return String.class;
            }
        };

        JTable table = new JTable(model);
        table.getColumnModel().getColumn(2).setCellRenderer(new NumberRenderer());
        table.getColumnModel().getColumn(3).setCellRenderer(new ImageRenderer());
        table.getColumnModel().getColumn(2).setCellRenderer(new PopulationRenderer());
        table.setRowHeight(83);

        return table;
    }

    class NumberRenderer extends DefaultTableCellRenderer {
        public NumberRenderer() {
            super();
            setHorizontalAlignment(JLabel.RIGHT);
        }
    }

    class ImageRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof String) {
                String path = (String) value;
                try {
                    ImageIcon icon = new ImageIcon(new java.io.File(path).toURI().toURL());
                    setIcon(icon);
                    setText("");
                } catch (MalformedURLException e) {
                    setIcon(null);
                    setText("Resim bulunamadı");
                }
            } else {
                setIcon(null);
                setText((value == null) ? "" : value.toString());
            }
            return this;
        }
    }

    class PopulationRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            int population = Integer.parseInt(value.toString());
            if(population > 20000) {
                setForeground(Color.RED);
            } else {
                setForeground(Color.BLACK);
            }
            return this;
        }
    }
}
