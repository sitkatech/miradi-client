/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.threatstressrating.upperPanel;

import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;

public class ThreatNameColumnTableModel extends MainThreatTableModel
{
	public ThreatNameColumnTableModel(Project projectToUse)
	{
		super(projectToUse);
	}
	
	public int getColumnCount()
	{
		return 1;
	}
	
	public String getColumnName(int column)
	{
		return EAM.text("Threats");
	}
	
	public String getColumnTag(int column)
	{
		return "";
	}
	
	public Object getValueAt(int row, int column)
	{
		return getDirectThreat(row).toString();
	}
	
	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return getDirectThreat(row);
	}
}
