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

import org.miradi.project.Project;
import org.miradi.schemas.*;
import org.miradi.utils.CodeList;

public class SharedWorkPlanRowColumnProvider extends AbstractWorkPlanRowColumnProvider
{
	public SharedWorkPlanRowColumnProvider(Project projectToUse)
	{
		super(projectToUse);
	}

	protected CodeList createMonitoringRelatedRowCodeList()
	{
		return new CodeList(new String[] {
				ConceptualModelDiagramSchema.OBJECT_NAME,
				ResultsChainDiagramSchema.OBJECT_NAME,
				StrategySchema.OBJECT_NAME,
				TaskSchema.ACTIVITY_NAME,
		});
	}

	protected CodeList createActionRelatedRowCodeList()
	{
		return new CodeList(new String[] {
				ConceptualModelDiagramSchema.OBJECT_NAME,
				ResultsChainDiagramSchema.OBJECT_NAME,
				StrategySchema.OBJECT_NAME,
				TaskSchema.ACTIVITY_NAME,
		});
	}

	protected CodeList createAllRowCodeList()
	{
		return new CodeList(new String[] {
				ConceptualModelDiagramSchema.OBJECT_NAME,
				ResultsChainDiagramSchema.OBJECT_NAME,
				StrategySchema.OBJECT_NAME,
				TaskSchema.ACTIVITY_NAME,
		});
	}
}
