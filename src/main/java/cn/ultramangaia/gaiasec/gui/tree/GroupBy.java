package cn.ultramangaia.gaiasec.gui.tree;

import cn.ultramangaia.gaiasec.gui.tree.model.AbstractTreeNode;
import cn.ultramangaia.gaiasec.gui.tree.model.LevelNode;
import cn.ultramangaia.gaiasec.gui.tree.model.LocationNode;
import cn.ultramangaia.gaiasec.gui.tree.model.RuleNode;
import cn.ultramangaia.sarif.Result;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public enum GroupBy {

    LevelRuleLocation("Level-Rule-Location"),
    RuleLevelLocation("Rule-Level-Location"),
    LevelRule("Level-Rule"),
    RuleLevel("Rule-Level"),
    LevelOnly("Level"),
    RuleOnly("Rule"),
    LocationOnly("Location");

    private final String _description;

    GroupBy(final String description) {
        _description = description;
    }

    public String getDescription() {
        return _description;
    }

    public static List<GroupBy> getGroupByList() {
        final List<GroupBy> list = new ArrayList<GroupBy>();
        list.addAll(Arrays.asList(GroupBy.values()));

        return list;
    }

    public static String getGroupName(final GroupBy groupBy, @NotNull final Result bug) {

        if (!Locale.ENGLISH.equals(Locale.getDefault())) {
            Locale.setDefault(Locale.ENGLISH);
        }

        final String groupName;
        switch (groupBy) {
            case LevelRule:
                groupName = bug.getRuleId();
                break;
            case RuleOnly:
                groupName = bug.getLevel().value();
                break;
            default:
                throw new IllegalStateException("Unknown group order: " + groupBy);
        }
        return groupName;
    }

    public static AbstractTreeNode getGroupNode(final GroupBy groupBy, @NotNull final Result bug){

        switch (groupBy) {
            case RuleOnly:
                return new RuleNode(bug.getRuleId());
            case LevelOnly:
                return new LevelNode(bug.getLevel());
            case LocationOnly:
                return new LocationNode(bug.getLocations().get(0));
            default:
                throw new IllegalStateException("Unknown group order: " + groupBy);
        }
    }

    /**
     * @param groupBy the primary group
     * @return the specific sort order group
     */
    public static GroupBy[] getSortOrderGroup(final GroupBy groupBy) {

        switch (groupBy) {
            // 不需要BugCategory和BugShortDescription
            case LevelRuleLocation:
                return new GroupBy[]{LevelOnly, RuleOnly, LocationOnly};
            case RuleLevelLocation:
                return new GroupBy[]{RuleOnly, LevelOnly, LocationOnly};
            case LevelRule:
                return new GroupBy[]{LevelOnly, RuleOnly};
            case RuleLevel:
                return new GroupBy[]{RuleOnly, LevelOnly};
            default:
                throw new IllegalStateException("Unknown sort order group: " + groupBy);
        }
    }

    // FIXME: getAvailGroupsForPrimaryGroup ??? static !!??

    public static GroupBy[] getAvailableGroups(final GroupBy[] currentGroupBy) {
        final List<GroupBy> result = new ArrayList<GroupBy>();
        final List<GroupBy> list = Arrays.asList(currentGroupBy);

        for (final GroupBy groupBy : GroupBy.values()) {
            if (!list.contains(groupBy)) {
                result.add(groupBy);
            }
        }

        return result.toArray(new GroupBy[result.size()]);
    }
}
