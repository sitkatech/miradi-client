/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.views.diagram.doers;

import org.miradi.actions.ActionCreateActivityProgressReport;
import org.miradi.actions.ActionDeleteActivityProgressReport;
import org.miradi.dialogs.base.ObjectListManagementPanel;
import org.miradi.dialogs.progressReport.ProgressReportManagementPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Task;
import org.miradi.views.umbrella.doers.AbstractPopUpEditDoer;

public class EditActivityProgressReportDoer extends AbstractPopUpEditDoer
{
	public EditActivityProgressReportDoer()
	{
		super(Task.getObjectType(), EAM.text("Edit Progress Reports"));
	}

	protected ObjectListManagementPanel createManagementPanel() throws Exception
	{
		ORef parentRef = getSelectedHierarchies()[0].getRefForType(getTypeToFilterOn());
		
		
		return new ProgressReportManagementPanel(getProject(), getMainWindow(), parentRef, Task.TAG_PROGRESS_REPORT_REFS, getMainWindow().getActions(), buttonActionClasses);
	}
	
	static Class[] buttonActionClasses = new Class[] {
		ActionCreateActivityProgressReport.class,
		ActionDeleteActivityProgressReport.class,
	};

}
