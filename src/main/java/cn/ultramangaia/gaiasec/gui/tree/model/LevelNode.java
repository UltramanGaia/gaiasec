package cn.ultramangaia.gaiasec.gui.tree.model;

import cn.ultramangaia.gaiasec.gui.tree.view.MaskIcon;
import cn.ultramangaia.sarif.Result;

import static icons.PluginIcons.GROUP_BY_PRIORITY_ICONS;

public class LevelNode extends AbstractTreeNode{

    private static final MaskIcon HighIcon = new MaskIcon(GROUP_BY_PRIORITY_ICONS.get("High"));
    private static final MaskIcon MediumIcon = new MaskIcon(GROUP_BY_PRIORITY_ICONS.get("Medium"));
    private static final MaskIcon LowIcon = new MaskIcon(GROUP_BY_PRIORITY_ICONS.get("Low"));

    private Result.Level level;
    public LevelNode(Result.Level level) {
        this.level = level;
        switch (level.value()){
            case "error":
                setIcon(HighIcon);
                break;
            case "warning":
                setIcon(MediumIcon);
                break;
            default:// note
                setIcon(LowIcon);
        }
    }

    @Override
    public String getTitle() {
        return level.value();
    }

    @Override
    public String getHit() {
        int childCount = getChildCount();
        return "(" + childCount + (childCount == 1 ? " item" : " items") + ')';
    }
}
