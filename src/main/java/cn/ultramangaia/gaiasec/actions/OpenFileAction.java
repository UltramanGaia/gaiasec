package cn.ultramangaia.gaiasec.actions;

import cn.ultramangaia.gaiasec.gui.toolwindow.ToolWindowUtils;
import cn.ultramangaia.gaiasec.gui.toolwindow.SarifViewerToolWindowPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class OpenFileAction extends AbstractAction {
    @Override
    void updateImpl(@NotNull AnActionEvent e, @NotNull Project project, @NotNull ToolWindow toolWindow) {

    }

    @Override
    void actionPerformedImpl(@NotNull AnActionEvent e, @NotNull Project project, @NotNull ToolWindow toolWindow) {
        VirtualFile file = FileChooser.chooseFile(FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor(),
                project, ProjectUtil.guessProjectDir(project));
        if (file != null) {
            File input = new File(file.getCanonicalPath());
            SarifViewerToolWindowPanel sarifViewerToolWindowPanel = ToolWindowUtils.getToolWindowPanel();
            if(sarifViewerToolWindowPanel != null){
                sarifViewerToolWindowPanel.getBugTreeModel().load(input);
            }
        }
    }


}
