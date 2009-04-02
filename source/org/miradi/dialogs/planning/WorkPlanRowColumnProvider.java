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
package org.miradi.dialogs.planning;

import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.utils.CodeList;

public class WorkPlanRowColumnProvider implements RowColumnProvider
{
	public CodeList getColumnListToShow()
	{
		return new CodeList(new String[] {
				BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE,
				BaseObject.PSEUDO_TAG_WHO_TOTAL,
				BaseObject.PSEUDO_TAG_WHEN_TOTAL,
				Task.PSEUDO_TAG_TASK_BUDGET_DETAIL,
				});
	}

	public CodeList getRowListToShow()
	{
		return new CodeList(new String[] {
				ResultsChainDiagram.OBJECT_NAME,
				Strategy.OBJECT_NAME,
				Task.ACTIVITY_NAME,
				Indicator.OBJECT_NAME,
				Task.METHOD_NAME,
				Task.OBJECT_NAME, });
	}
}
