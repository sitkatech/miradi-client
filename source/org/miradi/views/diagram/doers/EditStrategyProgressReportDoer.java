/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram.doers;

import org.miradi.actions.ActionCreateStrategyProgressReport;
import org.miradi.actions.ActionDeleteStrategyProgressReport;
import org.miradi.dialogs.base.ObjectListManagementPanel;
import org.miradi.dialogs.progressReport.ProgressReportManagementPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Strategy;
import org.miradi.views.umbrella.doers.AbstractPopUpEditDoer;

public class EditStrategyProgressReportDoer extends AbstractPopUpEditDoer
{
	public EditStrategyProgressReportDoer()
	{
		super(Strategy.getObjectType(), EAM.text("Edit Progress Reports"));
	}

	protected ObjectListManagementPanel getManagementPanel() throws Exception
	{
		ORef strategyRef = getSelectedHierarchies()[0].getRefForType(getTypeToFilterOn());
		
		return new ProgressReportManagementPanel(getProject(), getMainWindow(), strategyRef, Strategy.TAG_PROGRESS_REPORT_REFS, getMainWindow().getActions(), buttonActionClasses);
	}
	
	static Class[] buttonActionClasses = new Class[] {
		ActionCreateStrategyProgressReport.class,
		ActionDeleteStrategyProgressReport.class,
	};
}	

