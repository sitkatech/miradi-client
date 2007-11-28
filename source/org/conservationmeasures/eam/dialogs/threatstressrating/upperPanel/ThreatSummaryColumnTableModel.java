/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel;

import org.conservationmeasures.eam.project.Project;

public class ThreatSummaryColumnTableModel extends MainThreatTableModel
{
	public ThreatSummaryColumnTableModel(Project projectToUse)
	{
		super(projectToUse);
	}

	public String getColumnTag(int column)
	{
		return "";
	}

	public int getColumnCount()
	{
		return 1;
	}

	public Object getValueAt(int arg0, int arg1)
	{
		return "";
	}
}
