package org.miradi.dialogs.threatrating.upperPanel;

import org.miradi.icons.DirectThreatIcon;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.TaglessChoiceItem;

public class ThreatIconColumnTableModel extends AbstractThreatPerRowTableModel
{
    public ThreatIconColumnTableModel(Project projectToUse)
    {
        super(projectToUse);
    }

    @Override
    public String getUniqueTableModelIdentifier()
    {
        return UNIQUE_IDENTIFIER;
    }

    public int getColumnCount()
    {
        return COLUMN_COUNT;
    }

    @Override
    public String getColumnGroupCode(int column)
    {
        return getColumnName(column);
    }

    @Override
    public String getColumnName(int column)
    {
        return "";
    }

    public Object getValueAt(int row, int column)
    {
        return getChoiceItemAt(row, column);
    }

    public ChoiceItem getChoiceItemAt(int row, int column)
    {
        return createTaglessChoiceItemWithThreatIcon();
    }

    public TaglessChoiceItem createTaglessChoiceItemWithThreatIcon()
    {
        return new TaglessChoiceItem(new DirectThreatIcon());
    }

    private static final String UNIQUE_IDENTIFIER = "ThreatIconColumnTableModel";

    public static final int THREAT_ICON_COLUMN_INDEX = 0;
    public static final int COLUMN_COUNT = 1;
}
