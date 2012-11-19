/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogfields;

import javax.swing.JComponent;

import org.miradi.dialogfields.editors.WhenPopupEditorComponent;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.TimePeriodCostsMap;
import org.miradi.objects.BaseObject;
import org.miradi.utils.DateRange;

public class WhenEditorField extends ObjectDataField
{
	public WhenEditorField(MainWindow mainWindowToUse, ORef refToUse)
	{
		super(mainWindowToUse.getProject(), refToUse);
		
		whenEditor = new WhenPopupEditorComponent(mainWindowToUse);
		whenEditor.setBaseObjectForRowLabel(BaseObject.find(getProject(), refToUse));
	}

	@Override
	public JComponent getComponent()
	{
		return whenEditor;
	}
	
	@Override
	public String getTag()
	{
		return "";
	}

	@Override
	public void updateFromObject()
	{
		try
		{
			BaseObject baseObject = BaseObject.find(getProject(), getORef());
			TimePeriodCostsMap totalTimePeriodCostsMap = getProject().getTimePeriodCostsMapsCache().getTotalTimePeriodCostsMap(baseObject);
			DateRange projectStartEndDateRange = getProject().getProjectCalendar().getProjectPlanningDateRange();
			DateRange rolledUpResourceAssignmentsDateRange = totalTimePeriodCostsMap.getRolledUpDateRange(projectStartEndDateRange);
			String rolledUpResourceAssignmentsWhen = getProject().getProjectCalendar().convertToSafeString(rolledUpResourceAssignmentsDateRange);
			whenEditor.setText(rolledUpResourceAssignmentsWhen);
		}
		catch (Exception e)
		{
			EAM.alertUserOfNonFatalException(e);
		}
	}

	@Override
	public void saveIfNeeded()
	{
	}
	
	private WhenPopupEditorComponent whenEditor;
}
