/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.progressReport;

import org.miradi.actions.Actions;
import org.miradi.dialogs.base.ObjectListTablePanel;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;

public class ProgressReportListTablePanel extends ObjectListTablePanel
{
	public ProgressReportListTablePanel(Project projectToUse, Actions actions, ORef nodeRef, String annotationTag, Class[] editButtonClasses)
	{
		super(projectToUse, new ProgressReportListTableModel(projectToUse, nodeRef, annotationTag), actions, editButtonClasses);
	}
}
