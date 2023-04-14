package main.java;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class ResultsTableModel extends AbstractTableModel implements MutableResultModel {

    private final String[] columnNames = {"Line #", "Filename", "Original Text", "Replaced Text"};
    private final ArrayList<ResultRow> data;

    public ResultsTableModel() {
        this.data = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public ResultRow getRow(int rowIndex) {
        return data.get(rowIndex);
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public void reset() {
        this.data.clear();
        fireTableRowsDeleted(0, 0);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ResultRow item = data.get(rowIndex);
        switch (columnIndex) {
            case 0 -> {
                return item.getLineNumber();
            }
            case 1 -> {
                return item.getFileName();
            }
            case 2 -> {
                return item.getSourceText();
            }
            case 3 -> {
                return item.getReplaceText();
            }
            default -> {
                return null;
            }
        }
    }

    @Override
    public void add(ResultRow item) {
        data.add(item);
        int row = data.indexOf(item);
        fireTableRowsInserted(row, row);
    }

    @Override
    public void remove(ResultRow item) {
        if (data.contains(item)) {
            int row = data.indexOf(item);
            data.remove(row);
            fireTableRowsDeleted(row, row);
        }
    }
}
