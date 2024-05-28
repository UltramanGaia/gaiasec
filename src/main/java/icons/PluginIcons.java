package icons;

import cn.ultramangaia.gaiasec.resources.ResourcesLoader;
import com.intellij.icons.AllIcons;
import com.intellij.ui.LayeredIcon;

import javax.swing.*;
import java.util.Map;

public interface PluginIcons {

    Icon PLUGIN_ICON = ResourcesLoader.loadIcon("bug.svg");



    Icon GROUP_BY_PRIORITY_HIGH_ICON = ResourcesLoader.loadIcon("priorityHigh.png");
    Icon GROUP_BY_PRIORITY_MEDIUM_ICON = ResourcesLoader.loadIcon("priorityMedium.png");
    Icon GROUP_BY_PRIORITY_LOW_ICON = ResourcesLoader.loadIcon("priorityLow.png");
    Icon GROUP_BY_PRIORITY_EXP_ICON = ResourcesLoader.loadIcon("priorityExp.png");
    Icon GROUP_BY_PRIORITY_IGNORE_ICON = ResourcesLoader.loadIcon("priorityIgnore.png");

    Map<String, Icon> GROUP_BY_PRIORITY_ICONS = InitIcons.initGroupByPriorityIconsMap();


    /**
     * --------------------------------------------------------------------------------------------------
     * Tree icons
     */
    Icon TREENODE_OPEN_ICON = AllIcons.Nodes.Folder;

    Icon TREENODE_MODULE_ICON = AllIcons.Nodes.Module;
}
