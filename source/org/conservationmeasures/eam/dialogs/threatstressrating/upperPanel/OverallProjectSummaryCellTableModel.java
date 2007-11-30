/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel;

import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.SimpleModeThreatRatingFramework;

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

	public int getColumnCount()
	{
		return 1;
	}

	public Object getValueAt(int arg0, int arg1)
	{
		SimpleModeThreatRatingFramework framework = new SimpleModeThreatRatingFramework(getProject());
		int overallProjectRating = framework.getOverallProjectRating();
		
		return convertToChoiceItem("", Integer.toString(overallProjectRating));
	}
	
	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return null;
	}
}
