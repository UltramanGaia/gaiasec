package cn.ultramangaia.gaiasec.gui.table.view;

import cn.ultramangaia.gaiasec.gui.util.GuiUtil;
import cn.ultramangaia.gaiasec.gui.table.model.CodeFlowTableModel;
import cn.ultramangaia.gaiasec.gui.toolwindow.CodeFlowPanel;
import cn.ultramangaia.sarif.Location;
import cn.ultramangaia.sarif.ThreadFlowLocation;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;

public class CodeFlowTable extends JTable {

    private final CodeFlowTableModel codeFlowTableModel;
    private final CodeFlowPanel codeFlowPanel;
    public CodeFlowTable(CodeFlowTableModel dm, CodeFlowPanel codeFlowPanel) {
        super(dm);
        this.codeFlowTableModel = dm;
        this.codeFlowPanel = codeFlowPanel;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        super.valueChanged(e);
        if(e.getValueIsAdjusting()) {
            try {
                ThreadFlowLocation threadFlowLocation = codeFlowTableModel.getThreadFlowLocationAt(e.getLastIndex());
                if (threadFlowLocation == null)
                    return;
                Location location = threadFlowLocation.getLocation();
                GuiUtil.navigateToLocation(location, codeFlowPanel.getProject());
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
