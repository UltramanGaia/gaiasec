package cn.ultramangaia.gaiasec.gui.toolwindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class SarifViewerToolWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        SarifViewerToolWindowPanel sarifViewerToolWindowPanel = new SarifViewerToolWindowPanel(project);
        ToolWindowUtils.setToolWindowPanel(sarifViewerToolWindowPanel);
        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(sarifViewerToolWindowPanel, "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
