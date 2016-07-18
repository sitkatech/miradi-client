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

package org.miradi.dialogs.planning;

import org.miradi.objects.BaseObject;
import org.miradi.objects.Desire;
import org.miradi.objects.Indicator;
import org.miradi.project.Project;
import org.miradi.schemas.GoalSchema;
import org.miradi.schemas.IndicatorSchema;
import org.miradi.schemas.ObjectiveSchema;
import org.miradi.utils.CodeList;

public class MonitoringPlanSubViewEffectiveRowColumnProvider extends AbstractPlanningTreeRowColumnProvider
{
	public MonitoringPlanSubViewEffectiveRowColumnProvider(Project projectToUse)
	{
		super(projectToUse);
	}

	public CodeList getColumnCodesToShow() throws Exception
	{
		return new CodeList(new String[] {
				BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE,
				Desire.TAG_FULL_TEXT,
				Indicator.TAG_PRIORITY,
		});
	}

	public CodeList getRowCodesToShow() throws Exception
	{
		return new CodeList(new String[] {
				GoalSchema.OBJECT_NAME,
				ObjectiveSchema.OBJECT_NAME,
				IndicatorSchema.OBJECT_NAME, });
	}

	public boolean shouldIncludeResultsChain() throws Exception
	{
		return true;
	}

	public boolean shouldIncludeConceptualModelPage() throws Exception
	{
		return true;
	}
}
