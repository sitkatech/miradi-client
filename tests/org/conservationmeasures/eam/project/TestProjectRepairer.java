package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeTarget;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IndicatorId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objecthelpers.CreateModelNodeParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestProjectRepairer extends EAMTestCase
{
	public TestProjectRepairer(String name)
	{
		super(name);
	}

	public void testGhostIndicatorId() throws Exception
	{
		Project project = new ProjectForTesting(getName());
		try
		{
			CreateObjectParameter parameter = new CreateModelNodeParameter(new NodeTypeTarget());
			BaseId rawNodeId = project.createObject(ObjectType.MODEL_NODE, BaseId.INVALID, parameter);
			ModelNodeId nodeId = new ModelNodeId(rawNodeId.asInt());
			ConceptualModelNode node = project.findNode(nodeId);
			node.setIndicatorId(new IndicatorId(23252));
			project.writeNode(nodeId);
			
			ProjectRepairer.repairAnyProblems(project);
			node = project.findNode(nodeId);
			assertEquals("Didn't fix ghost IndicatorId?", BaseId.INVALID, node.getIndicatorId());
		}
		finally
		{
			project.close();
		}
	}
}
