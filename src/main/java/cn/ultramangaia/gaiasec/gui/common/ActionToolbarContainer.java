package cn.ultramangaia.gaiasec.gui.common;

import com.intellij.openapi.actionSystem.ActionToolbar;

import java.awt.Component;


/**
 * $Date$
 *
 * @author Andre Pfeiler<andrep@twodividedbyzero.org>
 * @version $Revision$
 * @since 0.9.29-dev
 */
public class ActionToolbarContainer extends AbstractBar {

    private final ActionToolbar _childBar;
    private int _orientation;


    public ActionToolbarContainer(final String floatedName, final int orientation, final ActionToolbar childBar, final boolean floatable) {
        super(orientation, floatable);
        setName(floatedName);
        _childBar = childBar;
        add(_childBar.getComponent());
        setOrientation(orientation);
        _childBar.setOrientation(orientation);
    }


    @Override
    public final Component add(final Component comp) {
        return super.add(comp);
    }


    @Override
    public final void setOrientation(final int o) {
        super.setOrientation(o);

        if (_orientation != o) {
            final int old = _orientation;
            _orientation = o;

            if (_childBar != null) {
                _childBar.setOrientation(o);
                _childBar.getComponent().firePropertyChange("orientation", old, o);
                _childBar.getComponent().repaint();
                _childBar.getComponent().revalidate();
            }

            firePropertyChange("orientation", old, o);
            revalidate();
            repaint();
        }
    }
}
