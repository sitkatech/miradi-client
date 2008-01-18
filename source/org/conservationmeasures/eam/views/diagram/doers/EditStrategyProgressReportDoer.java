/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram.doers;

import org.conservationmeasures.eam.actions.ActionCreateStrategyProgressReport;
import org.conservationmeasures.eam.actions.ActionDeleteStrategyProgressReport;
import org.conservationmeasures.eam.dialogs.base.ObjectListManagementPanel;
import org.conservationmeasures.eam.dialogs.progressReport.ProgressReportManagementPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.views.umbrella.doers.AbstractPopUpEditDoer;

public class EditStrategyProgressReportDoer extends AbstractPopUpEditDoer
{
	protected int getTypeToFilterOn()
	{
		return Strategy.getObjectType();
	}

	protected String getDialogTitle()
	{
		return EAM.text("Edit Progress Reports");
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

