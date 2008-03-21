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
package org.miradi.dialogs.planning;

import org.miradi.objects.BaseObject;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.Objective;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Target;
import org.miradi.utils.CodeList;
import org.miradi.views.planning.PlanningView;

public class MonitoringRowColumnProvider implements RowColumnProvider
{
	public CodeList getColumnListToShow()
	{
		return new CodeList(new String[] {
				Indicator.PSEUDO_TAG_METHODS, 
				Indicator.PSEUDO_TAG_FACTOR,
				Indicator.TAG_PRIORITY,
				BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE,
				BaseObject.PSEUDO_TAG_BUDGET_TOTAL,
				});
	}

	public CodeList getRowListToShow()
	{
		return new CodeList(new String[] {
				ConceptualModelDiagram.OBJECT_NAME,
				ResultsChainDiagram.OBJECT_NAME,
				Target.OBJECT_NAME,
				Goal.OBJECT_NAME,
				Objective.OBJECT_NAME,
				Indicator.OBJECT_NAME, });
	}
	
	public String getPropertyName()
	{
		return PlanningView.MONITORING_PLAN_RADIO_CHOICE;
	}
}
