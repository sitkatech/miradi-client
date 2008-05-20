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

import org.miradi.actions.ActionCreateObjectiveProgressPercent;
import org.miradi.actions.ActionDeleteObjectiveProgressPercent;
import org.miradi.dialogs.base.ObjectListManagementPanel;
import org.miradi.dialogs.progressPercent.ProgressPercentManagementPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Objective;
import org.miradi.views.umbrella.doers.AbstractPopUpEditDoer;

public class EditProgressPercentDoer extends AbstractPopUpEditDoer
{
	public EditProgressPercentDoer()
	{
		super(Objective.getObjectType(), EAM.text("Edit Progress Percents"));
	}

	protected ObjectListManagementPanel createManagementPanel() throws Exception
	{
		ORef parentRef = getSelectedHierarchies()[0].getRefForType(getTypeToFilterOn());
		
		return new ProgressPercentManagementPanel(getProject(), getMainWindow(), parentRef, Objective.TAG_PROGRESS_PERCENT_REFS, getMainWindow().getActions(), buttonActionClasses);
	}
	
	static Class[] buttonActionClasses = new Class[] {
		ActionCreateObjectiveProgressPercent.class,
		ActionDeleteObjectiveProgressPercent.class,
	};
}
