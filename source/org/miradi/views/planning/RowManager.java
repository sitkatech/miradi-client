/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.views.planning;

import org.miradi.dialogs.planning.MonitoringRowColumnProvider;
import org.miradi.dialogs.planning.StrategicRowColumnProvider;
import org.miradi.dialogs.planning.WorkPlanRowColumnProvider;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.PlanningTreeConfiguration;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

public class RowManager
{
	public static CodeList getStrategicPlanRows()
	{
		return new StrategicRowColumnProvider().getRowCodesToShow();
	}

	public static CodeList getMonitoringPlanRows()
	{
		return new MonitoringRowColumnProvider().getRowCodesToShow();
	}

	public static CodeList getWorkPlanRows(Project project)
	{
		return new WorkPlanRowColumnProvider(project).getRowCodesToShow();
	}
	
	public static CodeList getVisibleRowsForCustomization(ViewData viewData)
	{
		try
		{
			ORef customizationRef = viewData.getORef(ViewData.TAG_TREE_CONFIGURATION_REF);
			if(customizationRef.isInvalid())
				return new CodeList();
			PlanningTreeConfiguration customization = (PlanningTreeConfiguration)viewData.getProject().findObject(customizationRef);
			return customization.getRowCodesToShow();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Error: Unable to read customized rows");
			return new CodeList();
		}
	}
}
