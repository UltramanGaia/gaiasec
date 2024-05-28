package cn.ultramangaia.gaiasec.gui.tree.model;

import cn.ultramangaia.gaiasec.gui.tree.view.MaskIcon;
import com.intellij.icons.AllIcons;

public class RuleNode extends AbstractTreeNode{


    private static final MaskIcon RuleIcon = new MaskIcon(AllIcons.Scope.Problems);
    private String ruleId;
    public RuleNode(String ruleId) {
        this.ruleId = ruleId;
        setIcon(RuleIcon);
        setTitle(ruleId);

    }

    @Override
    public String getTitle() {
        return ruleId;
    }
}
