/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

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
import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.questions.TaglessChoiceItem;

public class ThreatNameColumnTableModel extends MainThreatTableModel
{
	public ThreatNameColumnTableModel(Project projectToUse)
	{
		super(projectToUse);
	}
	
	public int getColumnCount()
	{
		return COLUMN_COUNT;
	}
	
	public String getColumnName(int column)
	{
		if (isThreatIconColumn(column))
			return "";
		
		return "<HTML><B>" + EAM.text("Threats") + "</B></HTML> ";
	}
	
	public String getColumnTag(int column)
	{
		return "";
	}
	
	public Object getValueAt(int row, int column)
	{
		if (isThreatIconColumn(column))
			return new TaglessChoiceItem(new DirectThreatIcon());
		
		return getDirectThreat(row).toString();
	}

	private boolean isThreatIconColumn(int column)
	{
		return column == THREAT_ICON_COLUMN_INDEX;
	}
	
	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return getDirectThreat(row);
	}
	
	public static final int THREAT_ICON_COLUMN_INDEX = 0;
	public static final int THREAT_NAME_COLUMN_INDEX = 1;
	private static final int COLUMN_COUNT = 2;
}
