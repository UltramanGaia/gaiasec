package cn.ultramangaia.gaiasec.actions;

import cn.ultramangaia.gaiasec.gui.common.GaiaSecConstants;
import cn.ultramangaia.gaiasec.gui.util.IdeaUtilImpl;
import cn.ultramangaia.gaiasec.gui.toolwindow.ToolWindowUtils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import org.jetbrains.annotations.NotNull;

abstract class AbstractAction extends AnAction {

    @Override
    public final void update(@NotNull final AnActionEvent e) {
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
        updateImpl(
                e,
                project,
                toolWindow
        );
    }

    abstract void updateImpl(
            @NotNull final AnActionEvent e,
            @NotNull final Project project,
            @NotNull final ToolWindow toolWindow
    );

    @Override
    public final void actionPerformed(@NotNull final AnActionEvent e) {
        final Project project = IdeaUtilImpl.getProject(e.getDataContext());
        if (project == null) {
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
        actionPerformedImpl(
                e,
                project,
                toolWindow
        );
    }

    abstract void actionPerformedImpl(
            @NotNull final AnActionEvent e,
            @NotNull final Project project,
            @NotNull final ToolWindow toolWindow
    );
}
