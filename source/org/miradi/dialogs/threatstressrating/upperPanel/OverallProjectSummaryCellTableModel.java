/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.threatstressrating.upperPanel;

import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.project.StressBasedThreatRatingFramework;

public class OverallProjectSummaryCellTableModel extends MainThreatTableModel
{
	public OverallProjectSummaryCellTableModel(Project projectToUse)
	{
		super(projectToUse);
	}

	public String getColumnTag(int column)
	{
		return "";
	}

	public int getRowCount()
	{
		return 1;
	}

	public int getColumnCount()
	{
		return 1;
	}

	public Object getValueAt(int arg0, int arg1)
	{
		StressBasedThreatRatingFramework framework = new StressBasedThreatRatingFramework(getProject());
		int overallProjectRating = framework.getOverallProjectRating();
		
		return convertToChoiceItem("", Integer.toString(overallProjectRating));
	}
	
	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return null;
	}
}
