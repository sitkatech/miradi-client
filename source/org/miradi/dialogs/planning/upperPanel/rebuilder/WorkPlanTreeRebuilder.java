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

package org.miradi.dialogs.planning.upperPanel.rebuilder;

import org.miradi.dialogs.planning.WorkPlanRowColumnProvider;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.PlanningTreeRowColumnProvider;
import org.miradi.project.Project;

public class WorkPlanTreeRebuilder extends NormalTreeRebuilder
{
	public WorkPlanTreeRebuilder(Project projectToUse, PlanningTreeRowColumnProvider rowColumnProviderToUse)
	{
		super(projectToUse, rowColumnProviderToUse);
	}

	@Override
	protected ORefList getChildrenOfProjectNode(ORef parentRef) throws Exception
	{
		ORefList childRefs = new ORefList();

		WorkPlanRowColumnProvider rowColumnProvider = (WorkPlanRowColumnProvider) getRowColumnProvider();
		String diagramFilter = rowColumnProvider.getDiagramFilter();

		if (!diagramFilter.isEmpty())
		{
			ORef diagramFilterObjectRef = ORef.createFromString(diagramFilter);
			childRefs.add(diagramFilterObjectRef);
		}
		else
		{
			if(getRowColumnProvider().shouldIncludeConceptualModelPage())
			{
				ORefList conceptualModelRefs = getProject().getConceptualModelDiagramPool().getORefList();
				childRefs.addAll(conceptualModelRefs);
			}
			if(getRowColumnProvider().shouldIncludeResultsChain())
			{
				ORefList resultsChainRefs = getProject().getResultsChainDiagramPool().getORefList();
				childRefs.addAll(resultsChainRefs);
			}
		}

		if(shouldTargetsBeAtSameLevelAsDiagrams())
		{
			childRefs.addAll(getProject().getTargetPool().getRefList());
			if(getProject().getMetadata().isHumanWelfareTargetMode())
				childRefs.addAll(getProject().getHumanWelfareTargetPool().getRefList());
		}

		return childRefs;
	}
	
}
