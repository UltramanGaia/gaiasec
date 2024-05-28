package cn.ultramangaia.gaiasec.gui.tree.model;

import cn.ultramangaia.gaiasec.gui.tree.GroupBy;
import cn.ultramangaia.sarif.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class BugTreeModel implements TreeModel {

    private Project project;
    private AbstractTreeNode invisibleRoot;

    private Map<String, SarifSchema210> dataMap = new HashMap<>();// Key: filename
    private String toolName;
    private HashMap<String, ReportingDescriptor> rules = new HashMap<>();
    private GroupBy[] groupBy;

    private Vector<TreeModelListener> treeModelListeners = new Vector<TreeModelListener>();
    private final static Logger logger = Logger.getInstance(BugTreeModel.class);


    public BugTreeModel(AbstractTreeNode invisibleRoot, Project project){
        this.invisibleRoot = invisibleRoot;
        this.groupBy = GroupBy.getSortOrderGroup(GroupBy.LevelRuleLocation);
        this.project =project;
    }

    public void addChild(AbstractTreeNode parent, AbstractTreeNode newChild){
        int index = parent.getChildCount();
        parent.addChild(newChild);
        nodesWereInserted(parent, new int[]{index});
    }

    public void addNode(AbstractTreeNode resultRoot, @NotNull final Result bug) {
        // 按照分组添加到树
        AbstractTreeNode tmpNode = resultRoot;
        boolean isNewNode = false;
        for(GroupBy group: groupBy){
            AbstractTreeNode node = GroupBy.getGroupNode(group, bug);
            if(isNewNode){
                addChild(tmpNode, node);
                tmpNode = node;
                continue;
            }
            boolean flag=true;
            for(AbstractTreeNode child: tmpNode.getChildrenList()){
                if(child.getTitle().equals(node.getTitle())){
                    tmpNode = child;
                    flag=false;
                    break;
                }
            }
            if(flag){
                addChild(tmpNode, node);
                tmpNode = node;
                isNewNode = true;
            }
        }

        // 添加BugInstanceNode
        BugNode bugNode = new BugNode(bug, tmpNode);
        addChild(tmpNode, bugNode);
    }

    public AbstractTreeNode getParentNode(final AbstractTreeNode child) {
        return child.getParent();
    }

    public AbstractTreeNode getChildNode(final AbstractTreeNode parent, final int index) {
        return (AbstractTreeNode) parent.getChildAt(index);
    }

    public final AbstractTreeNode[] getPathToRoot(final AbstractTreeNode aNode) {
        return getPathToRoot(aNode, 0);
    }

    protected final AbstractTreeNode[] getPathToRoot(final AbstractTreeNode aNode, int depth) {
        // This method recurses, traversing towards the root in order
        // size the array. On the way back, it fills in the nodes,
        // starting from the root and working back to the original node.

        final AbstractTreeNode[] retNodes;

        if (aNode == null) {
            if (depth == 0) {
                return createEmptyArray(0);
            } else {
                retNodes = createEmptyArray(depth);
            }
        } else {
            //noinspection AssignmentToMethodParameter
            depth++;
            if (aNode == invisibleRoot) {
                retNodes = createEmptyArray(depth);
            } else {
                retNodes = getPathToRoot(getParentNode(aNode), depth);
            }
            retNodes[retNodes.length - depth] = aNode;
        }
        return retNodes;
    }

    private AbstractTreeNode[] createEmptyArray(final int size) {
        return (AbstractTreeNode[]) Array.newInstance(AbstractTreeNode.class, size);
    }

    public void clearData(Module module) {
        AbstractTreeNode tmpNode=null;
        String moduleName = module.getName();
        for(AbstractTreeNode child: invisibleRoot.getChildrenList()){
            if(child.getTitle().equals(moduleName)){
                tmpNode = child;
                break;
            }
        }
        if(tmpNode!=null){
            tmpNode.removeAllChilds();
            nodeStructureChanged(tmpNode);
        }
    }

    @Override
    public Object getRoot() {
        return invisibleRoot;
    }



    @Override
    public Object getChild(Object parent, int index) {
        return ((AbstractTreeNode)parent).getChildAt(index);
    }

    @Override
    public int getChildCount(Object parent) {
        return ((AbstractTreeNode)parent).getChildCount();
    }

    @Override
    public boolean isLeaf(Object node) {
        return ((AbstractTreeNode)node).isLeaf();
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        AbstractTreeNode   aNode = (AbstractTreeNode)path.getLastPathComponent();
        logger.warn("Change Path " + aNode + " to " + newValue);
        nodeChanged(aNode);
    }


    @Override
    public int getIndexOfChild(Object parent, Object child) {
        return ((AbstractTreeNode)parent).getIndex((TreeNode) child);
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        // 把后加入的直接加到最前面，优先级更高
        treeModelListeners.insertElementAt(l, 0);
//        treeModelListeners.addElement(l);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        treeModelListeners.removeElement(l);
    }

    public void setInvisibleRoot(AbstractTreeNode invisibleRoot) {
        Object oldRoot = this.invisibleRoot;
        this.invisibleRoot = invisibleRoot;
        if (invisibleRoot == null && oldRoot != null) {
            fireTreeStructureChanged(this, null);
        }
        else {
            nodeStructureChanged(invisibleRoot);
        }
    }

    public void reload() {
        reload(invisibleRoot);
    }


    public void removeNodeFromParent(AbstractTreeNode node) {
        AbstractTreeNode  parent = (AbstractTreeNode)node.getParent();

        if(parent == null)
            throw new IllegalArgumentException("node does not have a parent.");

        int[]            childIndex = new int[1];
        Object[]         removedArray = new Object[1];

        childIndex[0] = parent.getIndex(node);
        parent.remove(childIndex[0]);
        removedArray[0] = node;
        nodesWereRemoved(parent, childIndex, removedArray);
    }

    public void nodeChanged(AbstractTreeNode node) {
        if(!treeModelListeners.isEmpty() && node != null) {
            AbstractTreeNode  parent = node.getParent();

            if(parent != null) {
                int        anIndex = parent.getIndex(node);
                if(anIndex != -1) {
                    int[]        cIndexs = new int[1];

                    cIndexs[0] = anIndex;
                    nodesChanged(parent, cIndexs);
                }
            }
            else if (node == getRoot()) {
                nodesChanged(node, null);
            }
        }
    }

    public void reload(AbstractTreeNode node) {
        if(node != null) {
            fireTreeStructureChanged(this, getPathToRoot(node), null, null);
        }
    }

    public void nodesWereInserted(AbstractTreeNode node, int[] childIndices) {
        if(!treeModelListeners.isEmpty() && node != null && childIndices != null
                && childIndices.length > 0) {
            int               cCount = childIndices.length;
            Object[]          newChildren = new Object[cCount];
            for(int counter = 0; counter < cCount; counter++)
                newChildren[counter] = node.getChildAt(childIndices[counter]);
            fireTreeNodesInserted(this, getPathToRoot(node), childIndices,
                    newChildren);
        }
    }

    public void nodesWereRemoved(AbstractTreeNode node, int[] childIndices,
                                 Object[] removedChildren) {
        if(node != null && childIndices != null) {
            fireTreeNodesRemoved(this, getPathToRoot(node), childIndices,
                    removedChildren);
        }
    }

    public void nodesChanged(AbstractTreeNode node, int[] childIndices) {
        if(node != null) {
            if (childIndices != null) {
                int            cCount = childIndices.length;

                if(cCount > 0) {
                    Object[]       cChildren = new Object[cCount];

                    for(int counter = 0; counter < cCount; counter++)
                        cChildren[counter] = node.getChildAt
                                (childIndices[counter]);
                    fireTreeNodesChanged(this, getPathToRoot(node),
                            childIndices, cChildren);
                }
            }
            else if (node == getRoot()) {
                fireTreeNodesChanged(this, getPathToRoot(node), null, null);
            }
        }
    }

    public void nodeStructureChanged(AbstractTreeNode node) {
        if(node != null) {
            fireTreeStructureChanged(this, getPathToRoot(node), null, null);
        }
    }

    protected void fireTreeNodesChanged(Object source, Object[] path,
                                        int[] childIndices,
                                        Object[] children) {
        TreeModelEvent e = new TreeModelEvent(source, path, childIndices, children);
        for(TreeModelListener listener: treeModelListeners){
            listener.treeNodesChanged(e);
        }
    }

    protected void fireTreeNodesInserted(Object source, Object[] path,
                                         int[] childIndices,
                                         Object[] children) {
        TreeModelEvent e = new TreeModelEvent(source, path, childIndices, children);
        for(TreeModelListener listener: treeModelListeners){
            listener.treeNodesInserted(e);
        }
    }

    protected void fireTreeNodesRemoved(Object source, Object[] path,
                                        int[] childIndices,
                                        Object[] children) {
        TreeModelEvent e = new TreeModelEvent(source, path, childIndices, children);
        for(TreeModelListener listener: treeModelListeners){
            listener.treeNodesRemoved(e);
        }
    }

    protected void fireTreeStructureChanged(Object source, Object[] path,
                                            int[] childIndices,
                                            Object[] children) {
        TreeModelEvent e = new TreeModelEvent(source, path, childIndices, children);
        for(TreeModelListener listener: treeModelListeners){
            listener.treeStructureChanged(e);
        }
    }

    private void fireTreeStructureChanged(Object source, TreePath path) {
        TreeModelEvent e = new TreeModelEvent(source, path);
        for(TreeModelListener listener: treeModelListeners){
            listener.treeStructureChanged(e);
        }
    }


    public void load(File file) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            // 首先进行反序列化，反序列化失败则不展示
            SarifSchema210 data = objectMapper.readValue(file, SarifSchema210.class);

            String filepath = file.getAbsolutePath();
            AbstractTreeNode resultRoot = null;
            for(AbstractTreeNode node: invisibleRoot.getChildrenList()){
                if(node instanceof RootNode){
                    if(((RootNode)node).getFilepath().equals(filepath)){
                        resultRoot = node;
                    }
                }
            }
            if(resultRoot==null){
                resultRoot = new RootNode(filepath);
                invisibleRoot.addChild(resultRoot);
            }
            resultRoot.removeAllChilds();
            reload();

            dataMap.put(filepath, data);
            rules.clear();
            for(Run run: data.getRuns()){
                ToolComponent tool = run.getTool().getDriver();
                toolName = tool.getName();
                for(ReportingDescriptor rule: tool.getRules()){
                    rules.put(rule.getId(), rule);
                }
                List<Result> resultList = data.getRuns().get(0).getResults();
                ((RootNode) resultRoot).setBugCount(resultList.size());
                for(Result result: resultList){
                    addNode(resultRoot, result);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        reload();
    }

    public GroupBy[] getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(GroupBy[] groupBy) {
        this.groupBy = groupBy;
        for(AbstractTreeNode resultRoot: invisibleRoot.getChildrenList()){
            resultRoot.removeAllChilds();
            if(resultRoot instanceof RootNode){
                SarifSchema210 data = dataMap.get(((RootNode)resultRoot).getFilepath());
                List<Result> resultList = data.getRuns().get(0).getResults();
                for(Result result: resultList){
                    addNode(resultRoot, result);
                }
            }

        }
        reload();
    }
}
