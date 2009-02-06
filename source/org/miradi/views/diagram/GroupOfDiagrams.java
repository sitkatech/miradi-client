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
package org.miradi.views.diagram;

import java.util.Vector;

import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.project.Project;

public class GroupOfDiagrams
{	
	public static DiagramFactor[] findAllConceptualModelDiagrams(Project project)
	{
		ORefList allConceptualModels = project.getConceptualModelDiagramPool().getORefList();
		return findAllDiagramFactorsInDiagramObjects(project, allConceptualModels);
	}

	public static DiagramFactor[] findAllResultsChainDiagrams(Project project)
	{
		ORefList allConceptualModels = project.getResultsChainDiagramPool().getORefList();
		return findAllDiagramFactorsInDiagramObjects(project, allConceptualModels);
	}

	private static DiagramFactor[] findAllDiagramFactorsInDiagramObjects(Project project, ORefList diagramObjectRefs)
	{
		Vector diagramFactors = new Vector();
		for (int i = 0; i < diagramObjectRefs.size(); ++i)
		{
			DiagramObject diagramObject = (DiagramObject) project.findObject(diagramObjectRefs.get(i));
			diagramFactors.addAll(getAllDiagramFactors(project, diagramObject));
		}
		
		return (DiagramFactor[]) diagramFactors.toArray(new DiagramFactor[0]);
	}

	public static Vector getAllDiagramFactors(Project project, DiagramObject diagramObject)
	{
		Vector diagramFactors = new Vector();
		ORefList diagramFactorRefs = diagramObject.getAllDiagramFactorRefs();
		for (int i = 0; i < diagramFactorRefs.size(); ++i)
		{
			DiagramFactor diagramFactor = (DiagramFactor) project.findObject(diagramFactorRefs.get(i));
			diagramFactors.add(diagramFactor);
		}
		
		return diagramFactors;
	}
	
	public static ORefList getAllDiagramObjects(Project project)
	{
		ORefList allDiagramObjectRefs = new ORefList();
		allDiagramObjectRefs.addAll(project.getConceptualModelDiagramPool().getORefList());
		allDiagramObjectRefs.addAll(project.getResultsChainDiagramPool().getORefList());
		
		return allDiagramObjectRefs;
	}
}
