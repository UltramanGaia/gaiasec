package cn.ultramangaia.gaiasec.gui.table.model;

import cn.ultramangaia.sarif.ThreadFlow;
import cn.ultramangaia.sarif.ThreadFlowLocation;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class CodeFlowTableModel extends AbstractTableModel {

    private static final String[] ColumnNames = new String[]{"Line", "Message", "File"};

    private final List<ThreadFlowLocation> threadFlowLocations = new ArrayList<>();

    public CodeFlowTableModel() {

    }

    @Override
    public String getColumnName(int column) {
        return ColumnNames[column];
    }

    @Override
    public int getRowCount() {
        return threadFlowLocations.size();
    }

    @Override
    public int getColumnCount() {
        return ColumnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex >= getRowCount())
            return null;
        ThreadFlowLocation location = threadFlowLocations.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return location.getLocation().getPhysicalLocation().getRegion().getStartLine();
            case 1:
                return location.getLocation().getMessage().getText();
            case 2:
                return location.getLocation().getPhysicalLocation().getArtifactLocation().getUri();
        }
        return null;
    }

    public void setThreadFlow(ThreadFlow threadFlow) {
        threadFlowLocations.clear();
        threadFlowLocations.addAll(threadFlow.getLocations());
        fireTableStructureChanged();
    }

    public ThreadFlowLocation getThreadFlowLocationAt(int index) {
        if (index >= threadFlowLocations.size()) {
            return null;
        }
        return threadFlowLocations.get(index);
    }


}
