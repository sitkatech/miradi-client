package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeTarget;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.GoalIds;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.IndicatorId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.CreateModelNodeParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.ProjectMetadata;
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
			IdList bogusIndicators = new IdList();
			bogusIndicators.add(new IndicatorId(23252));
			node.setIndicators(bogusIndicators);
			project.writeNode(nodeId);
			
			EAM.setLogToString();
			ProjectRepairer.repairAnyProblems(project);
			assertContains("ghost", EAM.getLoggedString());
			node = project.findNode(nodeId);
			assertEquals("Didn't fix ghost IndicatorId?", 0, node.getIndicators().size());
		}
		finally
		{
			EAM.setLogToConsole();
			project.close();
		}
	}

	public void testInvalidGoalId() throws Exception
	{
		Project project = new ProjectForTesting(getName());
		try
		{
			CreateObjectParameter parameter = new CreateModelNodeParameter(new NodeTypeTarget());
			BaseId rawNodeId = project.createObject(ObjectType.MODEL_NODE, BaseId.INVALID, parameter);
			ModelNodeId nodeId = new ModelNodeId(rawNodeId.asInt());
			ConceptualModelNode node = project.findNode(nodeId);
			GoalIds bogusGoals = new GoalIds();
			bogusGoals.add(BaseId.INVALID);
			node.setGoals(bogusGoals);
			project.writeNode(nodeId);
			
			EAM.setLogToString();
			ProjectRepairer.repairAnyProblems(project);
			assertContains("invalid goal", EAM.getLoggedString());
			node = project.findNode(nodeId);
			assertEquals("Didn't fix invalid GoalId?", 0, node.getGoals().size());
		}
		finally
		{
			EAM.setLogToConsole();
			project.close();
		}
	}
	public void testDeletedTeamMemberId() throws Exception
	{
		Project project = new ProjectForTesting(getName());
		try
		{
			BaseId realResourceId = project.createObject(ObjectType.PROJECT_RESOURCE);
			IdList oneGoodOneBad = new IdList();
			oneGoodOneBad.add(realResourceId);
			oneGoodOneBad.add(new BaseId(2329));
			project.setMetadata(ProjectMetadata.TAG_TEAM_RESOURCE_IDS, oneGoodOneBad.toString());
			
			EAM.setLogToString();
			ProjectRepairer.repairAnyProblems(project);
			assertContains("deleted team member", EAM.getLoggedString());
			IdList fixed = new IdList(project.getMetadata().getData(ProjectMetadata.TAG_TEAM_RESOURCE_IDS));
			assertEquals("Didn't remove the bad resource?", 1, fixed.size());
			assertTrue("Lost the good resource?", fixed.contains(realResourceId));
		}
		finally
		{
			EAM.setLogToConsole();
			project.close();
		}
	}
}
