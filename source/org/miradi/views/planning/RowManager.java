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
import org.miradi.dialogs.planning.ProgressReportRowColumnProvider;
import org.miradi.dialogs.planning.StrategicRowColumnProvider;
import org.miradi.dialogs.planning.WorkPlanRowColumnProvider;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Assignment;
import org.miradi.objects.Cause;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.PlanningViewConfiguration;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.objects.ViewData;
import org.miradi.utils.CodeList;

public class RowManager
{

	static public CodeList getMasterRowList()
	{
		CodeList masterRowList = new CodeList();
		masterRowList.add(ConceptualModelDiagram.OBJECT_NAME);
		masterRowList.add(ResultsChainDiagram.OBJECT_NAME);
		masterRowList.add(Target.OBJECT_NAME);
		masterRowList.add(Goal.OBJECT_NAME);
		masterRowList.add(Objective.OBJECT_NAME);
		masterRowList.add(Cause.OBJECT_NAME_THREAT);
		masterRowList.add(Cause.OBJECT_NAME_CONTRIBUTING_FACTOR);
		masterRowList.add(ThreatReductionResult.OBJECT_NAME);
		masterRowList.add(IntermediateResult.OBJECT_NAME);
		masterRowList.add(Strategy.OBJECT_NAME);
		masterRowList.add(Task.ACTIVITY_NAME);
		masterRowList.add(Indicator.OBJECT_NAME);
		masterRowList.add(Task.METHOD_NAME);
		masterRowList.add(Task.OBJECT_NAME);
		masterRowList.add(Measurement.OBJECT_NAME);
		masterRowList.add(Assignment.OBJECT_NAME);
	
		return masterRowList;
	}

	public static CodeList getVisibleRowCodes(ViewData viewData)
	{
		String style = viewData.getData(ViewData.TAG_PLANNING_STYLE_CHOICE);
		if(style.equals(PlanningView.STRATEGIC_PLAN_RADIO_CHOICE))
			return getStrategicPlanRows();
		else if(style.equals(PlanningView.MONITORING_PLAN_RADIO_CHOICE))
			return getMonitoringPlanRows();
		else if(style.equals(PlanningView.WORKPLAN_PLAN_RADIO_CHOICE))
			return getWorkPlanRows();
		else if(style.equals(PlanningView.SINGLE_LEVEL_RADIO_CHOICE))
			return getVisibleRowsForSingleType(viewData);
		else if(style.equals(PlanningView.CUSTOMIZABLE_RADIO_CHOICE))
			return getVisibleRowsForCustomization(viewData);
		else if(style.equals(""))
			return getStrategicPlanRows();
		else
		{
			EAM.logError("getVisibleRowCodes unknown style: " + style);
			return new CodeList();
		}

	}

	public static CodeList getProgressReportRows()
	{
		return new ProgressReportRowColumnProvider().getRowListToShow();
	}
	
	public static CodeList getStrategicPlanRows()
	{
		return new StrategicRowColumnProvider().getRowListToShow();
	}

	public static CodeList getMonitoringPlanRows()
	{
		return new MonitoringRowColumnProvider().getRowListToShow();
	}

	public static CodeList getWorkPlanRows()
	{
		return new WorkPlanRowColumnProvider().getRowListToShow();
	}

	public static CodeList getVisibleRowsForSingleType(ViewData viewData)
	{
		String singleType = viewData.getData(ViewData.TAG_PLANNING_SINGLE_LEVEL_CHOICE);
		if(singleType.length() == 0)
			singleType = Goal.OBJECT_NAME;
		
		CodeList codes = new CodeList();
		codes.add(singleType);
		return codes;
	}

	private static CodeList getVisibleRowsForCustomization(ViewData viewData)
	{
		try
		{
			ORef customizationRef = viewData.getORef(ViewData.TAG_PLANNING_CUSTOM_PLAN_REF);
			if(customizationRef.isInvalid())
				return new CodeList();
			PlanningViewConfiguration customization = (PlanningViewConfiguration)viewData.getProject().findObject(customizationRef);
			return customization.getRowConfiguration();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Error: Unable to read customized rows");
			return new CodeList();
		}
	}
}
