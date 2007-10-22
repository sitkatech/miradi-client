package org.conservationmeasures.eam.dialogs.planning.treenodes;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.ConceptualModelDiagram;
import org.conservationmeasures.eam.project.Project;

public class PlanningTreeConceptualModelNode extends AbstractPlanningTreeNode
{
	PlanningTreeConceptualModelNode(Project projectToUse) throws Exception
	{
		super(projectToUse);
		rebuild();
	}
	
	public void rebuild() throws Exception
	{
		ORefList pageRefs = project.getConceptualModelDiagramPool().getORefList();
		for(int i = 0; i < pageRefs.size(); ++i)
		{
			children.add(new PlanningTreeConceptualModelPageNode(project, pageRefs.get(i)));
		}
		
	}
	
	public BaseObject getObject()
	{
		return null;
	}

	public String getObjectTypeName()
	{
		return ConceptualModelDiagram.OBJECT_NAME;
	}

	public int getType()
	{
		return ConceptualModelDiagram.getObjectType();
	}

	public String toString()
	{
		return EAM.text("Conceptual Model");
	}
	
}
