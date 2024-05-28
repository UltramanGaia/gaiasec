package cn.ultramangaia.gaiasec.gui.util;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IdeaUtilImpl {

    @Nullable
    public static Project getProject(@NotNull final DataContext dataContext) {
        return PlatformDataKeys.PROJECT.getData(dataContext);
    }
}
