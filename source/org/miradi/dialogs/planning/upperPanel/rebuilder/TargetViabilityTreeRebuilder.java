/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.planning.upperPanel.rebuilder;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.PlanningTreeRowColumnProvider;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;

//FIXME urgent - Make new target viability tree table work
public class TargetViabilityTreeRebuilder extends AbstractTreeRebuilder
{
	public TargetViabilityTreeRebuilder(Project projectToUse, PlanningTreeRowColumnProvider rowColumnProviderToUse)
	{
		super(projectToUse, rowColumnProviderToUse);
	}

	@Override
	protected ORefList getChildRefs(ORef parentRef, DiagramObject diagram) throws Exception
	{
		if(ProjectMetadata.is(parentRef))
			return getChildrenOfProjectNode(parentRef);
		
		EAM.logDebug("Don't know how to get children of " + parentRef);
		return new ORefList();
	}
	
	private ORefList getChildrenOfProjectNode(ORef parentRef) throws Exception
	{
		ORefList childRefs = new ORefList();
		childRefs.addAll(getProject().getTargetPool().getRefList());
		if(getProject().getMetadata().isHumanWelfareTargetMode())
			childRefs.addAll(getProject().getHumanWelfareTargetPool().getRefList());

		return childRefs;
	}
}
