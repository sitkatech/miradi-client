/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel;

import org.conservationmeasures.eam.project.Project;

public class TargetSummaryRowTableModel extends MainThreatTableModel
{
	public TargetSummaryRowTableModel(Project projectToUse)
	{
		super(projectToUse);
	}

	public String getColumnTag(int column)
	{
		return null;
	}

	public int getColumnCount()
	{
		return 0;
	}

	public Object getValueAt(int arg0, int arg1)
	{
		return null;
	}
}
