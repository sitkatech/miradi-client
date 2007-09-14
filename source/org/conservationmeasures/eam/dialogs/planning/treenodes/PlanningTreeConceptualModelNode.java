package org.conservationmeasures.eam.dialogs.planning.treenodes;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.ConceptualModelDiagram;
import org.conservationmeasures.eam.project.Project;

public class PlanningTreeConceptualModelNode extends AbstractPlanningTreeDiagramNode
{
	PlanningTreeConceptualModelNode(Project projectToUse)
	{
		super(projectToUse);
	}
	
	public boolean attemptToAdd(ORef refToAdd) throws Exception
	{
		boolean wasAdded = false;
		
		ORefList pageRefs = project.getConceptualModelDiagramPool().getORefList();
		for(int i = 0; i < pageRefs.size(); ++i)
		{
			ConceptualModelDiagram page = (ConceptualModelDiagram) project.findObject(pageRefs.get(i));
			if(attemptToAddToPage(page, refToAdd))
				wasAdded = true;
		}
		
		return wasAdded;
	}

	public BaseObject getObject()
	{
		return null;
	}

	public String getObjectTypeName()
	{
		return ConceptualModelDiagram.OBJECT_NAME;
	}

	public int getObjectType()
	{
		return ConceptualModelDiagram.getObjectType();
	}

	public String toString()
	{
		return EAM.text("Conceptual Model");
	}
	
}
