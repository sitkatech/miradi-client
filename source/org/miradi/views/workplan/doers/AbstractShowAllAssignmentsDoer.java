/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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


import org.miradi.commands.CommandSetObjectData;
import org.miradi.objecthelpers.CodeToCodeListMap;
import org.miradi.objects.TableSettings;
import org.miradi.utils.CodeList;

public abstract class AbstractShowAllAssignmentsDoer extends AbstractShowHideAllAssignmentsDoer
{
	@Override
	public boolean isAvailable()
	{
		if(!super.isAvailable())
			return false;

		return !isRowCodeVisible();
	}

	@Override
	protected void doIt() throws Exception
	{
		if (!isAvailable())
			return;

		getProject().executeBeginTransaction();
		try
		{
			showAssignmentRows();
		}
		finally
		{
			getProject().executeEndTransaction();
		}
	}

	private void showAssignmentRows() throws Exception
	{
		TableSettings tableSettings = getWorkPlanTableSettings();
		CodeList rowCodes = tableSettings.getCodeListFromTableSettingsMap(TableSettings.WORK_PLAN_ROW_CONFIGURATION_CODELIST_KEY);

		String rowCode = getRowCode();
		if (!rowCodes.contains(rowCode))
			rowCodes.add(rowCode);

		CodeToCodeListMap tableSettingsMap = tableSettings.getTableSettingsMap();
		CodeToCodeListMap newTableSettingsMap = new CodeToCodeListMap(tableSettingsMap);
		newTableSettingsMap.putCodeList(TableSettings.WORK_PLAN_ROW_CONFIGURATION_CODELIST_KEY, rowCodes);

		CommandSetObjectData setRowCodes = new CommandSetObjectData(tableSettings, TableSettings.TAG_TABLE_SETTINGS_MAP, newTableSettingsMap.toJsonString());
		getProject().executeCommand(setRowCodes);
	}
}
