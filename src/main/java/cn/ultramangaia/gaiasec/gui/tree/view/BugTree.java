package cn.ultramangaia.gaiasec.gui.tree.view;

import cn.ultramangaia.gaiasec.gui.util.GuiUtil;
import cn.ultramangaia.gaiasec.gui.toolwindow.SarifViewerToolWindowPanel;
import cn.ultramangaia.gaiasec.gui.tree.model.BugNode;
import cn.ultramangaia.sarif.CodeFlow;
import cn.ultramangaia.sarif.Location;
import cn.ultramangaia.sarif.ThreadFlow;
import com.intellij.ui.treeStructure.Tree;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.List;

public class BugTree extends Tree {

    private final SarifViewerToolWindowPanel panel;
    public BugTree(TreeModel treeModel, SarifViewerToolWindowPanel panel) {
        super(treeModel);
        this.panel = panel;
        init();
    }

    private void init(){
        setRootVisible(false);
        setCellRenderer(new BugTreeNodeCellRenderer());
        addTreeSelectionListener(new SelectionListenerImpl());
    }

    private class SelectionListenerImpl implements TreeSelectionListener {
        @Override
        public void valueChanged(final TreeSelectionEvent e) {
            final TreePath path = BugTree.this.getSelectionPath();
            if(path != null){
                Object treeNode = path.getLastPathComponent();
                if(treeNode instanceof BugNode){
                    BugNode bugNode = (BugNode) treeNode;
                    if (bugNode.getBug().getLocations() != null && !bugNode.getBug().getLocations().isEmpty()) {
                        Location location = bugNode.getBug().getLocations().get(0);
                        GuiUtil.navigateToLocation(location, panel.getCodeFlowPanel().getProject());
                    }
                    List<CodeFlow> codeFlows = bugNode.getBug().getCodeFlows();
                    if(!codeFlows.isEmpty()){
                        CodeFlow codeFlow = codeFlows.get(0);
                        List<ThreadFlow> threadFlows = codeFlow.getThreadFlows();
                        if(!threadFlows.isEmpty()){
                            panel.getCodeFlowPanel().setThreadFlows(threadFlows);
                        }
                    }
                }
            }
        }
    }
}
