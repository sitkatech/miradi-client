/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
package org.miradi.views.workplan.doers;


import org.miradi.dialogs.planning.AbstractWorkPlanRowColumnProvider;
import org.miradi.main.EAM;
import org.miradi.objects.TableSettings;
import org.miradi.utils.CodeList;
import org.miradi.views.ViewDoer;

public abstract class AbstractShowHideAllAssignmentsDoer extends ViewDoer
{
	abstract String getRowCode();

	protected boolean isRowCodeVisible()
	{
		try
		{
			if (!getProject().isOpen())
				return false;

			TableSettings tableSettings = getWorkPlanTableSettings();
			CodeList rowCodes = tableSettings.getCodeListFromTableSettingsMap(TableSettings.WORK_PLAN_ROW_CONFIGURATION_CODELIST_KEY);
			return rowCodes.contains(getRowCode());
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return false;
		}
	}

	protected TableSettings getWorkPlanTableSettings() throws Exception
	{
		return AbstractWorkPlanRowColumnProvider.getWorkPlanTableSettings(getProject());
	}
}
