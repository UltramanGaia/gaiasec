package cn.ultramangaia.gaiasec.gui.tree.view;

import cn.ultramangaia.gaiasec.gui.tree.model.AbstractTreeNode;
import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

public class BugTreeNodeCellRenderer extends JPanel implements TreeCellRenderer/*, TreeCellEditor*/ {

    private final JLabel icon;
    private final JLabel title;
    private final JLabel hits;

    public BugTreeNodeCellRenderer() {
        setOpaque(false);

        final double border = 0;
        final double colsGap = 4;
        final double[][] size = {{border, TableLayoutConstants.PREFERRED, colsGap, TableLayoutConstants.PREFERRED, colsGap, TableLayoutConstants.PREFERRED, colsGap, TableLayoutConstants.PREFERRED, border}, // Columns
                {border, TableLayoutConstants.PREFERRED, border}};// Rows
        final LayoutManager tbl = new TableLayout(size);
        setLayout(tbl);

        icon = new JLabel();
        title = new JLabel();
        hits = new JLabel();

        add(icon, "1, 1, 1, 1");
        add(title, "3, 1, 3, 1");
        add(hits, "5, 1, 5, 1");
    }

    public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean selected, final boolean expanded, final boolean leaf, final int row, final boolean hasFocus) {

        if (value != null) {
            if(value instanceof AbstractTreeNode){
                AbstractTreeNode node = (AbstractTreeNode)value;
                MaskIcon maskIcon = node.getIcon();
                maskIcon.setColorPainted(selected);
                setIcon(maskIcon);
                setTitle(node.getTitle());
                setHits(node.getHit());
                setToolTipText(node.getToolTip());
            }
        }
        return this;
    }


    private void setIcon(final Icon icon) {
        this.icon.setIcon(icon);
    }


    private void setTitle(final String description) {
        title.setText(description);
    }


    private void setHits(final String stats) {
        hits.setText(stats);
    }

}