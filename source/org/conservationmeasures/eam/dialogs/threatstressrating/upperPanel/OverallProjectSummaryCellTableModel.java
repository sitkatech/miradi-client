/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel;

import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.StressBasedThreatRatingFramework;

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
