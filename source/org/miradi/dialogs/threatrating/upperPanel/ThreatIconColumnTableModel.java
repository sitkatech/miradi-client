/*
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3,
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>.
*/
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
        return " ";
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
