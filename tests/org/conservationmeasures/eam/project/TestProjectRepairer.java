package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;

public class TestProjectRepairer extends EAMTestCase
{
	public TestProjectRepairer(String name)
	{
		super(name);
	}

	public void testDeleteOrphanObjectives() throws Exception
	{
		int annotationType = ObjectType.OBJECTIVE;
		String nodeTagForAnnotationList = Factor.TAG_OBJECTIVE_IDS;

		verifyDeleteOrphanAnnotations(annotationType, ObjectType.CAUSE, nodeTagForAnnotationList);
		
	}

	public void testDeleteOrphanGoals() throws Exception
	{
		int annotationType = ObjectType.GOAL;
		String nodeTagForAnnotationList = Factor.TAG_GOAL_IDS;

		verifyDeleteOrphanAnnotations(annotationType, ObjectType.TARGET, nodeTagForAnnotationList);
		
	}

	public void testDeleteOrphanIndicators() throws Exception
	{
		int annotationType = ObjectType.INDICATOR;
		String nodeTagForAnnotationList = Factor.TAG_INDICATOR_IDS;

		verifyDeleteOrphanAnnotations(annotationType, ObjectType.CAUSE, nodeTagForAnnotationList);
		
	}

	private void verifyDeleteOrphanAnnotations(int annotationType, int nodeType, String nodeTagForAnnotationList) throws Exception
	{
		Project project = new ProjectForTesting(getName());
		try
		{
			//BaseId orphan = project.createObject(annotationType);
			BaseId nonOrphan = project.createObjectAndReturnId(annotationType);
			FactorId nodeId = (FactorId)project.createObject(nodeType, BaseId.INVALID);
			IdList annotationIds = new IdList();
			annotationIds.add(nonOrphan);
			project.setObjectData(ObjectType.FACTOR, nodeId, nodeTagForAnnotationList, annotationIds.toString());
			
			EAM.setLogToString();
			
//			TODO: removed orphan deletion code until a solution can be found to general extentions of having annoations having annoations
			ProjectRepairer.repairAnyProblems(project);
			//assertContains("Deleting orphan", EAM.getLoggedString());
			//assertNull("Didn't delete orphan?", project.findObject(annotationType, orphan));
			assertEquals("Deleted non-orphan?", nonOrphan, project.findObject(annotationType, nonOrphan).getId());
		}
		finally
		{
			EAM.setLogToConsole();
			project.close();
		}
	}
}
