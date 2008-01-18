/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.util.Vector;

import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.project.Project;

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
