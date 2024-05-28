package cn.ultramangaia.gaiasec.gui.tree.model;

import cn.ultramangaia.gaiasec.gui.tree.view.MaskIcon;
import cn.ultramangaia.sarif.Location;
import com.intellij.icons.AllIcons;

public class LocationNode extends AbstractTreeNode{

    private static final MaskIcon FileIcon = new MaskIcon(AllIcons.Actions.InlayRenameInNoCodeFilesActive);
    private Location location;
    public LocationNode(Location location) {
        this.location = location;
        setIcon(FileIcon);
        setTitle(location.getPhysicalLocation().getArtifactLocation().getUri());
    }
}
