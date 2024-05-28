package cn.ultramangaia.gaiasec.actions;

import cn.ultramangaia.gaiasec.gui.toolwindow.ToolWindowUtils;
import cn.ultramangaia.gaiasec.gui.toolwindow.SarifViewerToolWindowPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import org.jetbrains.annotations.NotNull;

public class CollapseAllAction extends AbstractAction{
    @Override
    void updateImpl(@NotNull AnActionEvent e, @NotNull Project project, @NotNull ToolWindow toolWindow) {
        SarifViewerToolWindowPanel toolWindowPanel = ToolWindowUtils.getToolWindowPanel();
        if (!toolWindowPanel.canCollapse()) {
            e.getPresentation().setEnabled(false);
        }
    }

    @Override
    void actionPerformedImpl(@NotNull AnActionEvent e, @NotNull Project project, @NotNull ToolWindow toolWindow) {
        ToolWindowUtils.getToolWindowPanel().collapseAll();
    }
}
