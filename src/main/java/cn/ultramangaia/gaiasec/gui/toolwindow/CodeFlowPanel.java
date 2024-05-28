package cn.ultramangaia.gaiasec.gui.toolwindow;

import cn.ultramangaia.gaiasec.gui.common.NDockLayout;
import cn.ultramangaia.gaiasec.gui.table.model.CodeFlowTableModel;
import cn.ultramangaia.gaiasec.gui.table.view.CodeFlowTable;
import cn.ultramangaia.sarif.ThreadFlow;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CodeFlowPanel extends JPanel {
    private Project project;
    private JButton prevFlowButton;
    private int currentPage;
    private int totalPage;
    private JLabel pagesLabel;
    private JButton nextFlowButton;

    private CodeFlowTable codeFlowTable;
    private CodeFlowTableModel codeFlowTableModel;

    private List<ThreadFlow> threadFlows = new ArrayList<>();

    public CodeFlowPanel(Project project) {
        this.project = project;
        initGui();
    }

    private void initGui() {
        setLayout(new NDockLayout());
        setBorder(JBUI.Borders.empty(1));
        currentPage = 1;
        totalPage = 1;
        pagesLabel = new JLabel();
        updatePagesLabel();
        prevFlowButton = new JButton();
        prevFlowButton.setIcon(AllIcons.General.ArrowLeft);
        prevFlowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currentPage < totalPage){
                    currentPage++;
                }
                updatePagesLabel();
                codeFlowTableModel.setThreadFlow(threadFlows.get(currentPage - 1));
            }
        });

        nextFlowButton = new JButton();
        nextFlowButton.setIcon(AllIcons.General.ArrowRight);
        nextFlowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currentPage < totalPage && currentPage > 1){
                    currentPage--;
                }
                updatePagesLabel();
                codeFlowTableModel.setThreadFlow(threadFlows.get(currentPage - 1));
            }
        });

        JPanel pagePanel = new JPanel();
        pagePanel.add(prevFlowButton);
        pagePanel.add(pagesLabel);
        pagePanel.add(nextFlowButton);
        add(pagePanel, NDockLayout.SOUTH);

        codeFlowTableModel = new CodeFlowTableModel();
        codeFlowTable = new CodeFlowTable(codeFlowTableModel, this);
        add(ScrollPaneFactory.createScrollPane(codeFlowTable), NDockLayout.CENTER);
    }

    private void updatePagesLabel(){
        pagesLabel.setText(currentPage + "/" + totalPage);
    }

    public void setThreadFlows(List<ThreadFlow> threadFlows) {
        this.threadFlows = threadFlows;
        totalPage = threadFlows.size();
        currentPage = 1;
        updatePagesLabel();
        codeFlowTableModel.setThreadFlow(threadFlows.get(currentPage - 1));

    }

    public CodeFlowTableModel getCodeFlowTableModel() {
        return codeFlowTableModel;
    }


    public Project getProject() {
        return project;
    }
}
