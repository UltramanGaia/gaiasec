package cn.ultramangaia.gaiasec.gui.toolwindow;

import cn.ultramangaia.gaiasec.gui.common.*;
import cn.ultramangaia.gaiasec.gui.tree.model.BugTreeModel;
import cn.ultramangaia.gaiasec.gui.tree.model.RootNode;
import cn.ultramangaia.gaiasec.gui.tree.view.BugTree;
import com.intellij.ide.DefaultTreeExpander;
import com.intellij.ide.TreeExpander;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class SarifViewerToolWindowPanel extends JPanel {

    private Project project;

    private static final String LEFT_RIGHT_LAYOUT_DEF = "(ROW (LEAF name=left weight=0.4) (LEAF weight=0.6 name=right))";


    private MultiSplitPane multiSplitPane;

    // Bug Tree
    private RootNode invisibleRootNode;
    private BugTreeModel bugTreeModel;
    private Tree bugTree;

    private CodeFlowPanel codeFlowPanel;
    private TreeExpander bugTreeExpander;



    public SarifViewerToolWindowPanel(Project project) {
        this.project = project;
        initGui();

    }

    private void initGui() {
        setLayout(new NDockLayout());
        setBorder(JBUI.Borders.empty(1));
        final ActionGroup actionGroupLeft = (ActionGroup) ActionManager.getInstance().getAction(GaiaSecConstants.ACTION_GROUP_TOOLBAR_LEFT);
        final ActionToolbar toolbarLeft = ActionManager.getInstance().createActionToolbar(GaiaSecConstants.TOOL_WINDOW_ID, actionGroupLeft, false);
        toolbarLeft.setTargetComponent(this);
        final Component toolbarComponentLeft = new ActionToolbarContainer(GaiaSecConstants.TOOL_WINDOW_ID + ": ...", SwingConstants.VERTICAL, toolbarLeft, false);
        add(toolbarComponentLeft, NDockLayout.WEST);

        invisibleRootNode = new RootNode(project.getName() + ".sarif");

        bugTreeModel = new BugTreeModel(invisibleRootNode, project);
        bugTree = new BugTree(bugTreeModel, this);
        bugTreeExpander = new DefaultTreeExpander(bugTree);

        codeFlowPanel = new CodeFlowPanel(project);

        updateLayout();


        add(getMultiSplitPane(), NDockLayout.CENTER);

        autoLoadSarifResults();
    }

    private void autoLoadSarifResults() {
        final Module[] modules = ModuleManager.getInstance(project).getModules();
        for(Module module: modules){
            String modulePath = ModuleUtilCore.getModuleDirPath(module);
            try (Stream<Path> paths = Files.walk(Paths.get(modulePath), 3)) {
                paths.filter(path -> path.toString().endsWith(".sarif")).filter(Files::isRegularFile).forEach(
                   path -> {
                       bugTreeModel.load(path.toFile());
                   }
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateLayout() {
        updateMultiSplitLayout(LEFT_RIGHT_LAYOUT_DEF);
        getMultiSplitPane().add(ScrollPaneFactory.createScrollPane(bugTree), "left");
        getMultiSplitPane().add(ScrollPaneFactory.createScrollPane(codeFlowPanel), "right");
    }

    private void updateMultiSplitLayout(String layoutDef) {
        final MultiSplitLayout.Node modelRoot = MultiSplitLayout.parseModel(layoutDef);
        final MultiSplitLayout multiSplitLayout = getMultiSplitPane().getMultiSplitLayout();
        multiSplitLayout.setDividerSize(5);
        multiSplitLayout.setModel(modelRoot);
        multiSplitLayout.setFloatingDividers(false);
    }

    private MultiSplitPane getMultiSplitPane() {
        if (multiSplitPane == null) {
            multiSplitPane = new MultiSplitPane();
            multiSplitPane.setContinuousLayout(true);
        }
        return multiSplitPane;
    }


    public BugTreeModel getBugTreeModel() {
        return bugTreeModel;
    }

    public boolean canExpand() {
        return bugTreeExpander.canExpand();
    }

    public void expandAll() {
        bugTreeExpander.expandAll();
    }


    public boolean canCollapse() {
        return bugTreeExpander.canCollapse();
    }

    public void collapseAll() {
        bugTreeExpander.collapseAll();
    }

    public CodeFlowPanel getCodeFlowPanel() {
        return codeFlowPanel;
    }
}
