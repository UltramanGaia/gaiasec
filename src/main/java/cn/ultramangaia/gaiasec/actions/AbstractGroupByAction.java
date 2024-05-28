package cn.ultramangaia.gaiasec.actions;

import cn.ultramangaia.gaiasec.gui.toolwindow.SarifViewerToolWindowPanel;
import cn.ultramangaia.gaiasec.gui.tree.GroupBy;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class AbstractGroupByAction extends AbstractToggleAction{

    private final GroupBy groupBy;

    AbstractGroupByAction(@NotNull final GroupBy groupBy) {
        this.groupBy = groupBy;
    }

    @Override
    boolean isSelectedImpl(@NotNull AnActionEvent e, @NotNull Project project, @NotNull ToolWindow toolWindow, @NotNull SarifViewerToolWindowPanel panel) {

        final GroupBy[] sortOrderGroup = GroupBy.getSortOrderGroup(groupBy);
        return Arrays.equals(panel.getBugTreeModel().getGroupBy(), sortOrderGroup);
    }

    @Override
    void setSelectedImpl(@NotNull AnActionEvent e, @NotNull Project project, @NotNull ToolWindow toolWindow, @NotNull SarifViewerToolWindowPanel panel, boolean select) {
        panel.getBugTreeModel().setGroupBy(GroupBy.getSortOrderGroup(groupBy));
    }
}
