/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.util.Vector;

import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.ConceptualModelDiagram;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.project.Project;

public class EntireDiagrams
{
	public static DiagramFactor[] getAllDiagramFactorsInAllDiagrams(Project project)
	{
		Vector diagramFactors = new Vector();
		ORefList conceptualModels = project.getConceptualModelDiagramPool().getORefList();
		for (int i = 0; i < conceptualModels.size(); ++i)
		{
			ConceptualModelDiagram conceptualModel = (ConceptualModelDiagram) project.findObject(conceptualModels.get(i));
			diagramFactors.addAll(getAllDiagramFactors(project, conceptualModel));
		}
		
		return (DiagramFactor[]) diagramFactors.toArray(new DiagramFactor[0]);
	}
	
	public static Vector getAllDiagramFactors(Project project, ConceptualModelDiagram conceptualModel)
	{
		Vector diagramFactors = new Vector();
		ORefList diagramFactorRefs = conceptualModel.getAllDiagramFactorRefs();
		for (int i = 0; i < diagramFactorRefs.size(); ++i)
		{
			DiagramFactor diagramFactor = (DiagramFactor) project.findObject(diagramFactorRefs.get(i));
			diagramFactors.add(diagramFactor);
		}
		
		return diagramFactors;
	}
}
