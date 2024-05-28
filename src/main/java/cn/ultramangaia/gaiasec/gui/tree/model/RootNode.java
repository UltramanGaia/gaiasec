package cn.ultramangaia.gaiasec.gui.tree.model;

import cn.ultramangaia.gaiasec.gui.tree.view.MaskIcon;
import icons.PluginIcons;

import java.io.File;

public class RootNode extends AbstractTreeNode {
    private static final MaskIcon ModuleIcon = new MaskIcon(PluginIcons.TREENODE_MODULE_ICON);
    private String filepath;
    private int bugCount;

    public RootNode(final String filepath) {
        setParent(null);
        setIcon(ModuleIcon);
        this.filepath = filepath;
        File file = new File(filepath);
        String key = file.getName();
        if(key.indexOf('.') > 0){
            key = key.substring(0, key.indexOf('.'));
        }
        this.title = key;
        bugCount = -1;
    }

    public int getBugCount() {
        return bugCount;
    }

    public void setBugCount(int bugCount) {
        this.bugCount = bugCount;
    }

    public String getFilepath() {
        return filepath;
    }

    @Override
    public String getHit() {
        return bugCount == -1 ? "" : "(found " + bugCount + " bug items)";
    }
}
