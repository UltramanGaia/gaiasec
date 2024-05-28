package cn.ultramangaia.gaiasec.gui.tree.model;

import cn.ultramangaia.gaiasec.gui.tree.view.MaskIcon;
import icons.PluginIcons;

import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class AbstractTreeNode implements TreeNode {

    private static final MaskIcon openIcon = new MaskIcon(PluginIcons.TREENODE_OPEN_ICON);

    AbstractTreeNode parent;

    private final List<AbstractTreeNode> children = new ArrayList<>();

    String title = null;
    String hit = null;
    String toolTip = null;

    MaskIcon icon = openIcon;

    public List<AbstractTreeNode> getChildrenList(){
        return children;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public void setHit(String hit) {
        this.hit = hit;
    }

    public String getHit(){
        return hit;
    }


    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }


    public String getToolTip() {
        return toolTip;
    }

    public void setIcon(MaskIcon icon) {
        this.icon = icon;
    }

    public MaskIcon getIcon() {
        return icon;
    }

    public void setParent(AbstractTreeNode node){
        parent = node;
    }

    public void addChild(final AbstractTreeNode node) {
        getChildrenList().add(node);
        node.setParent(this);
    }

    public void remove(int index){
        getChildrenList().remove(index);
    }

    public void removeAllChilds() {
        getChildrenList().clear();
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
        return getChildrenList().get(childIndex);
    }

    @Override
    public int getChildCount() {
        return getChildrenList().size();
    }

    @Override
    public AbstractTreeNode getParent() {
        return parent;
    }

    @Override
    public int getIndex(TreeNode node) {
        return getChildrenList().indexOf(node);
    }

    @Override
    public boolean getAllowsChildren() {
        return true;
    }

    @Override
    public boolean isLeaf() {
        return children.size()==0;
    }


    @Override
    public Enumeration<? extends TreeNode> children() {
        return Collections.enumeration(getChildrenList());
    }

}
