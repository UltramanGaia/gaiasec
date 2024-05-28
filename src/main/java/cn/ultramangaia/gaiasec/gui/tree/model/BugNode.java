package cn.ultramangaia.gaiasec.gui.tree.model;

import cn.ultramangaia.gaiasec.gui.tree.view.MaskIcon;
import cn.ultramangaia.sarif.Result;
import com.intellij.icons.AllIcons;

public class BugNode extends AbstractTreeNode{
    private static final MaskIcon ClassIcon = new MaskIcon(AllIcons.Nodes.Class);
    private Result bug;
    public BugNode(Result bug, AbstractTreeNode parent) {
        this.bug = bug;
        setParent(parent);
        setIcon(ClassIcon);
        setTitle(bug.getMessage().getText());
        setHit(bug.getLocations().get(0).getPhysicalLocation().getArtifactLocation().getUri());
    }

    public Result getBug() {
        return bug;
    }
}
