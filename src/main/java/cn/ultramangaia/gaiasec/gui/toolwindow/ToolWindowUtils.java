package cn.ultramangaia.gaiasec.gui.toolwindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ToolWindowUtils {

    private static SarifViewerToolWindowPanel sarifViewerToolWindowPanel =null;

    @Nullable
    public static ToolWindow getWindow(@NotNull final Project project, final String windowId) {
        return ToolWindowManager.getInstance(project).getToolWindow(windowId);
    }

    public static void setToolWindowPanel(SarifViewerToolWindowPanel sarifViewerToolWindowPanel){
        ToolWindowUtils.sarifViewerToolWindowPanel = sarifViewerToolWindowPanel;
    }

    public static SarifViewerToolWindowPanel getToolWindowPanel(){
        return sarifViewerToolWindowPanel;
    }

}
