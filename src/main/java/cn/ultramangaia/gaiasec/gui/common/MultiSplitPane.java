package cn.ultramangaia.gaiasec.gui.common;

import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;


/**
 * <p/>
 * All properties in this class are bound: when a properties value
 * is changed, all PropertyChangeListeners are fired.
 *
 * @author Hans Muller
 * @author Andre Pfeiler - some minor adaptations
 */
public class MultiSplitPane extends JPanel {

    private AccessibleContext accessibleContext;
    private boolean continuousLayout = true;
    private transient DividerPainter dividerPainter = new DefaultDividerPainter();


    /**
     * Creates a MultiSplitPane with it's LayoutManager set to
     * to an empty MultiSplitLayout.
     */
    public MultiSplitPane() {
        super(new MultiSplitLayout());
        final InputHandler inputHandler = new InputHandler();
        addMouseListener(inputHandler);
        addMouseMotionListener(inputHandler);
        addKeyListener(inputHandler);
        setFocusable(true);
    }


    /**
     * A convenience method that returns the layout manager cast
     * to MutliSplitLayout.
     *
     * @return this MultiSplitPane's layout manager
     * @see java.awt.Container#getLayout
     * @see #setModel
     */
    public final MultiSplitLayout getMultiSplitLayout() {
        return (MultiSplitLayout) getLayout();
    }


    /**
     * A convenience method that sets the MultiSplitLayout model.
     * Equivalent to <code>getMultiSplitLayout.setModel(model)</code>
     *
     * @param model the root of the MultiSplitLayout model
     * @see #getMultiSplitLayout
     */
    public final void setModel(final MultiSplitLayout.Node model) {
        getMultiSplitLayout().setModel(model);
    }


    /**
     * A convenience method that sets the MultiSplitLayout dividerSize
     * property. Equivalent to
     * <code>getMultiSplitLayout().setDividerSize(newDividerSize)</code>.
     *
     * @param dividerSize the value of the dividerSize property
     * @see #getMultiSplitLayout
     */
    public final void setDividerSize(final int dividerSize) {
        getMultiSplitLayout().setDividerSize(dividerSize);
    }


    /**
     * Sets the value of the <code>continuousLayout</code> property.
     * If true, then the layout is revalidated continuously while
     * a divider is being moved.  The default value of this property
     * is true.
     *
     * @param continuousLayout value of the continuousLayout property
     * @see #isContinuousLayout
     */
    public void setContinuousLayout(final boolean continuousLayout) {
        final boolean oldContinuousLayout = continuousLayout;
        this.continuousLayout = continuousLayout;
        firePropertyChange("continuousLayout", oldContinuousLayout, continuousLayout);
    }


    /**
     * Returns true if dragging a divider only updates
     * the layout when the drag gesture ends (typically, when the
     * mouse button is released).
     *
     * @return the value of the <code>continuousLayout</code> property
     * @see #setContinuousLayout
     */
    public boolean isContinuousLayout() {
        return continuousLayout;
    }


    /**
     * Returns the Divider that's currently being moved, typically
     * because the user is dragging it, or null.
     *
     * @return the Divider that's being moved or null.
     */
    public MultiSplitLayout.Divider activeDivider() {
        return dragDivider;
    }


    /**
     * Draws a single Divider.  Typically used to specialize the
     * way the active Divider is painted.
     *
     * @see MultiSplitPane#getDividerPainter
     * @see MultiSplitPane#setDividerPainter
     */
    public abstract static class DividerPainter {

        /**
         * Paint a single Divider.
         *
         * @param g	   the Graphics object to paint with
         * @param divider the Divider to paint
         */
        public abstract void paint(Graphics g, MultiSplitLayout.Divider divider);
    }

    private class DefaultDividerPainter extends DividerPainter {

        @Override
        public void paint(final Graphics g, final MultiSplitLayout.Divider divider) {
            if (divider == activeDivider() && !isContinuousLayout()) {
                final Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.black);
                g2d.fill(divider.getBounds());
            }
        }
    }


    /**
     * The DividerPainter that's used to paint Dividers on this MultiSplitPane.
     * This property may be null.
     *
     * @return the value of the dividerPainter Property
     * @see #setDividerPainter
     */
    public DividerPainter getDividerPainter() {
        return dividerPainter;
    }


    /**
     * Sets the DividerPainter that's used to paint Dividers on this
     * MultiSplitPane.  The default DividerPainter only draws
     * the activeDivider (if there is one) and then, only if
     * continuousLayout is false.  The value of this property is
     * used by the paintChildren method: Dividers are painted after
     * the MultiSplitPane's children have been rendered so that
     * the activeDivider can appear "on top of" the children.
     *
     * @param dividerPainter the value of the dividerPainter property, can be null
     * @see #paintChildren
     * @see #activeDivider
     */
    public void setDividerPainter(final DividerPainter dividerPainter) {
        this.dividerPainter = dividerPainter;
    }


    /**
     * Uses the DividerPainter (if any) to paint each Divider that
     * overlaps the clip Rectangle.  This is done after the call to
     * <code>super.paintChildren()</code> so that Dividers can be
     * rendered "on top of" the children.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    protected void paintChildren(final Graphics g) {
        super.paintChildren(g);
        final DividerPainter dp = getDividerPainter();
        final Rectangle clipR = g.getClipBounds();
        if (dp != null && clipR != null) {
            final Graphics dpg = g.create();
            try {
                final MultiSplitLayout msl = getMultiSplitLayout();
                for (final MultiSplitLayout.Divider divider : msl.dividersThatOverlap(clipR)) {
                    dp.paint(dpg, divider);
                }
            } finally {
                dpg.dispose();
            }
        }
    }


    private boolean dragUnderway;
    private transient MultiSplitLayout.Divider dragDivider;
    private Rectangle initialDividerBounds;
    private boolean oldFloatingDividers = true;
    private int dragOffsetX;
    private int dragOffsetY;
    private int dragMin = -1;
    private int dragMax = -1;


    private void startDrag(final int mx, final int my) {
        requestFocusInWindow();
        final MultiSplitLayout msl = getMultiSplitLayout();
        final MultiSplitLayout.Divider divider = msl.dividerAt(mx, my);
        if (divider != null) {
            final MultiSplitLayout.Node prevNode = divider.previousSibling();
            final MultiSplitLayout.Node nextNode = divider.nextSibling();
            if (prevNode == null || nextNode == null) {
                dragUnderway = false;
            } else {
                initialDividerBounds = divider.getBounds();
                dragOffsetX = mx - initialDividerBounds.x;
                dragOffsetY = my - initialDividerBounds.y;
                dragDivider = divider;
                final Rectangle prevNodeBounds = prevNode.getBounds();
                final Rectangle nextNodeBounds = nextNode.getBounds();
                if (dragDivider.isVertical()) {
                    dragMin = prevNodeBounds.x;
                    dragMax = nextNodeBounds.x + nextNodeBounds.width;
                    dragMax -= dragDivider.getBounds().width;
                } else {
                    dragMin = prevNodeBounds.y;
                    dragMax = nextNodeBounds.y + nextNodeBounds.height;
                    dragMax -= dragDivider.getBounds().height;
                }
                oldFloatingDividers = getMultiSplitLayout().getFloatingDividers();
                getMultiSplitLayout().setFloatingDividers(false);
                dragUnderway = true;
            }
        } else {
            dragUnderway = false;
        }
    }


    private void repaintDragLimits() {
        final Rectangle damageR = dragDivider.getBounds();
        if (dragDivider.isVertical()) {
            damageR.x = dragMin;
            damageR.width = dragMax - dragMin;
        } else {
            damageR.y = dragMin;
            damageR.height = dragMax - dragMin;
        }
        repaint(damageR);
    }


    private void updateDrag(final int mx, final int my) {
        if (!dragUnderway) {
            return;
        }
        final Rectangle oldBounds = dragDivider.getBounds();
        final Rectangle bounds = new Rectangle(oldBounds);
        if (dragDivider.isVertical()) {
            bounds.x = mx - dragOffsetX;
            bounds.x = Math.max(bounds.x, dragMin);
            bounds.x = Math.min(bounds.x, dragMax);
        } else {
            bounds.y = my - dragOffsetY;
            bounds.y = Math.max(bounds.y, dragMin);
            bounds.y = Math.min(bounds.y, dragMax);
        }
        dragDivider.setBounds(bounds);
        if (isContinuousLayout()) {
            revalidate();
            repaintDragLimits();
        } else {
            repaint(oldBounds.union(bounds));
        }
    }


    @SuppressWarnings({"AssignmentToNull", "NestedAssignment"})
    private void clearDragState() {
        dragDivider = null;
        initialDividerBounds = null;
        oldFloatingDividers = true;
        dragOffsetX = dragOffsetY = 0;
        dragMin = dragMax = -1;
        dragUnderway = false;
    }


    private void finishDrag(final int x, final int y) {
        if (dragUnderway) {
            clearDragState();
            if (!isContinuousLayout()) {
                revalidate();
                repaint();
            }
        }
    }


    private void cancelDrag() {
        if (dragUnderway) {
            dragDivider.setBounds(initialDividerBounds);
            getMultiSplitLayout().setFloatingDividers(oldFloatingDividers);
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            repaint();
            revalidate();
            clearDragState();
        }
    }


    private void updateCursor(final int x, final int y, final boolean show) {
        if (dragUnderway) {
            return;
        }
        int cursorID = Cursor.DEFAULT_CURSOR;
        if (show) {
            final MultiSplitLayout.Divider divider = getMultiSplitLayout().dividerAt(x, y);
            if (divider != null) {
                cursorID = divider.isVertical() ? Cursor.E_RESIZE_CURSOR : Cursor.N_RESIZE_CURSOR;
            }
        }
        setCursor(Cursor.getPredefinedCursor(cursorID));
    }


    private class InputHandler extends MouseInputAdapter implements KeyListener {

        @Override
        public void mouseEntered(final MouseEvent e) {
            updateCursor(e.getX(), e.getY(), true);
        }


        @Override
        public void mouseMoved(final MouseEvent e) {
            updateCursor(e.getX(), e.getY(), true);
        }


        @Override
        public void mouseExited(final MouseEvent e) {
            updateCursor(e.getX(), e.getY(), false);
        }


        @Override
        public void mousePressed(final MouseEvent e) {
            startDrag(e.getX(), e.getY());
        }


        @Override
        public void mouseReleased(final MouseEvent e) {
            finishDrag(e.getX(), e.getY());
        }


        @Override
        public void mouseDragged(final MouseEvent e) {
            updateDrag(e.getX(), e.getY());
        }


        public void keyPressed(final KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                cancelDrag();
            }
        }


        public void keyReleased(final KeyEvent e) {
        }


        public void keyTyped(final KeyEvent e) {
        }
    }


    @Override
    public AccessibleContext getAccessibleContext() {
        if (accessibleContext == null) {
            accessibleContext = new AccessibleMultiSplitPane();
        }
        return accessibleContext;
    }


    private class AccessibleMultiSplitPane extends AccessibleJPanel {

        private static final long serialVersionUID = 0L;


        @Override
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.SPLIT_PANE;
        }
    }
}
