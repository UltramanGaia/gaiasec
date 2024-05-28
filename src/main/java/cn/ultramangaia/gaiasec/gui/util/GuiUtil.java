package cn.ultramangaia.gaiasec.gui.util;

import cn.ultramangaia.sarif.Location;
import cn.ultramangaia.sarif.PhysicalLocation;
import cn.ultramangaia.sarif.Region;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.OpenSourceUtil;

import java.io.File;

public final class GuiUtil {

    private static File searchFileInProject(Project project, String target){
        ModuleManager moduleManager = ModuleManager.getInstance(project);
        for (Module module : moduleManager.getModules()) {
            for(VirtualFile sourceRoot: ModuleRootManager.getInstance(module).getSourceRoots()){
                File file = new File(sourceRoot.getCanonicalPath()+"/" + target);
                if(file.exists()){
                    return file;
                }
            }
            String moduleDir = ModuleUtilCore.getModuleDirPath(module);
            File file = new File(moduleDir + "/" + target);
            if(file.exists()){
                return file;
            }
        }
        return new File(target);
    }

    public static void navigateToLocation(Location location, Project project){
        try {
            PhysicalLocation physicalLocation = location.getPhysicalLocation();
            String basePath = physicalLocation.getArtifactLocation().getUriBaseId();
            String target = physicalLocation.getArtifactLocation().getUri();
            File file = new File(basePath, target);
            if (!file.exists()) {
                file = searchFileInProject(project, target);
            }
            OpenFileDescriptor mappingFileDescriptor;
            VirtualFile vFile = VfsUtil.findFileByIoFile(file, true);
            if (vFile != null) {
                int startLine = 0;
                int startColumn = 0;
                Region region = physicalLocation.getRegion();
                if(region != null){
                    if(region.getStartLine() != null && region.getStartLine() > 0){
                        startLine = region.getStartLine() - 1;
                    }
                    if(region.getStartColumn() != null && region.getStartColumn() > 0){
                        startColumn = region.getStartColumn() - 1;
                    }
                }
                mappingFileDescriptor = new OpenFileDescriptor(project, vFile, startLine, startColumn);
                OpenSourceUtil.navigateToSource(true, false, mappingFileDescriptor);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
}
