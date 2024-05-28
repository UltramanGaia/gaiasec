package icons;

import javax.swing.*;
import java.util.*;

import static icons.PluginIcons.*;

class InitIcons {

    static Map<String, Icon> initGroupByPriorityIconsMap() {
        Map<String, Icon> iconMap = new HashMap<>();
        iconMap.put("Low", GROUP_BY_PRIORITY_LOW_ICON);
        iconMap.put("Medium", GROUP_BY_PRIORITY_MEDIUM_ICON);
        iconMap.put("High", GROUP_BY_PRIORITY_HIGH_ICON);
        iconMap.put("Exp", GROUP_BY_PRIORITY_EXP_ICON);
        iconMap.put("Ignore", GROUP_BY_PRIORITY_IGNORE_ICON);
        return Collections.unmodifiableMap(iconMap);
    }
}
