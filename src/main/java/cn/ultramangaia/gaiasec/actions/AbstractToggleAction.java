package cn.ultramangaia.gaiasec.actions;

import cn.ultramangaia.gaiasec.gui.common.GaiaSecConstants;
import cn.ultramangaia.gaiasec.gui.util.IdeaUtilImpl;
import cn.ultramangaia.gaiasec.gui.toolwindow.ToolWindowUtils;
import cn.ultramangaia.gaiasec.gui.toolwindow.SarifViewerToolWindowPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import org.jetbrains.annotations.NotNull;


abstract class AbstractToggleAction extends ToggleAction {

    @Override
    public final void update(@NotNull AnActionEvent e) {
        final Project project = IdeaUtilImpl.getProject(e.getDataContext());
        if (project == null || !project.isInitialized() || !project.isOpen()) {
            e.getPresentation().setEnabled(false);
            e.getPresentation().setVisible(false);
            return;
        }
        final ToolWindow toolWindow = ToolWindowUtils.getWindow(project, GaiaSecConstants.TOOL_WINDOW_ID);
        if (toolWindow == null || !toolWindow.isAvailable()) {
            e.getPresentation().setEnabled(false);
            e.getPresentation().setVisible(false);
            return;
        }
        final SarifViewerToolWindowPanel panel = ToolWindowUtils.getToolWindowPanel();
        if (panel == null) {
            e.getPresentation().setEnabled(false);
            e.getPresentation().setVisible(false);
            return;
        }

        final boolean select = isSelectedImpl(
                e,
                project,
                toolWindow,
                panel
        );
        final Boolean selected = select ? Boolean.TRUE : Boolean.FALSE;
        e.getPresentation().putClientProperty(SELECTED_PROPERTY, selected);
        e.getPresentation().setEnabled(true);
        e.getPresentation().setVisible(true);
    }

    @Override
    public final boolean isSelected(@NotNull AnActionEvent e) {
        final Project project = IdeaUtilImpl.getProject(e.getDataContext());
        if (project == null || !project.isInitialized() || !project.isOpen()) {
            e.getPresentation().setEnabled(false);
            e.getPresentation().setVisible(false);
            return false;
        }
        final ToolWindow toolWindow = ToolWindowUtils.getWindow(project, GaiaSecConstants.TOOL_WINDOW_ID);
        if (toolWindow == null || !toolWindow.isAvailable()) {
            e.getPresentation().setEnabled(false);
            e.getPresentation().setVisible(false);
            return false;
        }
        final SarifViewerToolWindowPanel panel = ToolWindowUtils.getToolWindowPanel();
        if (panel == null) {
            e.getPresentation().setEnabled(false);
            e.getPresentation().setVisible(false);
            return false;
        }

        return isSelectedImpl(
                e,
                project,
                toolWindow,
                panel
        );
    }

    abstract boolean isSelectedImpl(
            @NotNull final AnActionEvent e,
            @NotNull final Project project,
            @NotNull final ToolWindow toolWindow,
            @NotNull final SarifViewerToolWindowPanel panel
    );

    @Override
    public final void setSelected(@NotNull AnActionEvent e, boolean select) {
        final Project project = IdeaUtilImpl.getProject(e.getDataContext());
        if (project == null || !project.isInitialized() || !project.isOpen()) {
            e.getPresentation().setEnabled(false);
            e.getPresentation().setVisible(false);
            return;
        }
        final ToolWindow toolWindow = ToolWindowUtils.getWindow(project, GaiaSecConstants.TOOL_WINDOW_ID);
        if (toolWindow == null || !toolWindow.isAvailable()) {
            e.getPresentation().setEnabled(false);
            e.getPresentation().setVisible(false);
            return;
        }
        final SarifViewerToolWindowPanel panel = ToolWindowUtils.getToolWindowPanel();
        if (panel == null) {
            e.getPresentation().setEnabled(false);
            e.getPresentation().setVisible(false);
            return;
        }

        setSelectedImpl(
                e,
                project,
                toolWindow,
                panel,
                select
        );
    }

    abstract void setSelectedImpl(
            @NotNull final AnActionEvent e,
            @NotNull final Project project,
            @NotNull final ToolWindow toolWindow,
            @NotNull final SarifViewerToolWindowPanel panel,
            boolean select
    );
}
