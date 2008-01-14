/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.progressReport;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.base.ObjectListTablePanel;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.project.Project;

public class ProgressReportListTablePanel extends ObjectListTablePanel
{
	public ProgressReportListTablePanel(Project projectToUse, Actions actions, ORef nodeRef, String annotationTag, Class[] editButtonClasses)
	{
		super(projectToUse, new ProgressReportListTableModel(projectToUse, nodeRef, annotationTag), actions, editButtonClasses);
	}
}
