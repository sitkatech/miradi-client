/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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
package org.miradi.dialogs.planning;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.CodeToCodeListMap;
import org.miradi.objects.TableSettings;
import org.miradi.project.Project;
import org.miradi.schemas.ConceptualModelDiagramSchema;
import org.miradi.schemas.ResultsChainDiagramSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.utils.CodeList;

public class WorkPlanRowColumnProvider extends AbstractWorkPlanRowColumnProvider
{
	public WorkPlanRowColumnProvider(Project projectToUse)
	{
		super(projectToUse);
	}

	protected CodeList createMonitoringRelatedRowCodeList()
	{
		return getRowCodeList();
	}

	protected CodeList createActionRelatedRowCodeList()
	{
		return getRowCodeList();
	}

	protected CodeList createAllRowCodeList()
	{
		return getRowCodeList();
	}

	private CodeList getRowCodeList()
	{
		CodeList codeList = new CodeList();
		codeList.addAll(getDefaultRowCodeList());
		codeList.addAll(getConfiguredRowCodeList());
		return codeList;
	}

	private CodeList getDefaultRowCodeList()
	{
		return new CodeList(new String[] {
				ConceptualModelDiagramSchema.OBJECT_NAME,
				ResultsChainDiagramSchema.OBJECT_NAME,
				StrategySchema.OBJECT_NAME,
				TaskSchema.ACTIVITY_NAME,
				TaskSchema.MONITORING_ACTIVITY_NAME,
				TaskSchema.OBJECT_NAME,
		});
	}

	private CodeList getConfiguredRowCodeList()
	{
		return getRowCodeListFromTableSettingsMap();
	}

	private CodeList getRowCodeListFromTableSettingsMap()
	{
		try
		{
			TableSettings tableSettings = getWorkPlanTableSettings();
			CodeToCodeListMap tableSettingsMap = tableSettings.getTableSettingsMap();
			return tableSettingsMap.getCodeList(TableSettings.WORK_PLAN_ROW_CONFIGURATION_CODELIST_KEY);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return new CodeList();
		}
	}
}
