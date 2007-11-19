package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.TestCaseWithProject;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Task;

public class TestProjectRepairer extends TestCaseWithProject
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
		//BaseId orphan = project.createObject(annotationType);
		BaseId nonOrphan = getProject().createObjectAndReturnId(annotationType);
		FactorId nodeId = (FactorId)getProject().createObject(nodeType, BaseId.INVALID);
		IdList annotationIds = new IdList();
		annotationIds.add(nonOrphan);
		getProject().setObjectData(ObjectType.FACTOR, nodeId, nodeTagForAnnotationList, annotationIds.toString());

		EAM.setLogToString();

//		TODO: removed orphan deletion code until a solution can be found to general extentions of having annoations having annoations
		ProjectRepairer.repairAnyProblems(getProject());
		//assertContains("Deleting orphan", EAM.getLoggedString());
		//assertNull("Didn't delete orphan?", project.findObject(annotationType, orphan));
		assertEquals("Deleted non-orphan?", nonOrphan, getProject().findObject(annotationType, nonOrphan).getId());
	}
	
	public void testScanForCorruptedObjects() throws Exception
	{
		ORef indicatorRef = getProject().createObject(Indicator.getObjectType());
		Indicator indicator = (Indicator) getProject().findObject(indicatorRef);
		BaseId nonExistantId = new BaseId(500);
		CommandSetObjectData appendTask = CommandSetObjectData.createAppendIdCommand(indicator, Indicator.TAG_TASK_IDS, nonExistantId);
		getProject().executeCommand(appendTask);
		assertEquals("task not appended?", 1, indicator.getTaskCount());		
		
		ORef taskRef = getProject().createObject(Task.getObjectType());
		Task task = (Task) getProject().findObject(taskRef);
		CommandSetObjectData appendSubTask = CommandSetObjectData.createAppendIdCommand(task, Task.TAG_SUBTASK_IDS, nonExistantId);
		getProject().executeCommand(appendSubTask);
		assertEquals("subtask not appended?", 1, task.getSubtaskCount());
		
		ProjectRepairer repairer = new ProjectRepairer(getProject());
		ORefList corruptedObjectRefs = repairer.findAllCorruptedObjects();
		assertEquals("found no corrupted objects?", 2, corruptedObjectRefs.size());
	}
}
