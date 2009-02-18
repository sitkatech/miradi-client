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
package org.miradi.project;

import java.awt.Dimension;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.ids.AssignmentId;
import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Assignment;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.objects.Indicator;
import org.miradi.objects.TaggedObjectSet;
import org.miradi.objects.Task;
import org.miradi.objects.TextBox;
import org.miradi.utils.EnhancedJsonObject;

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
		ORef nodeRef = getProject().createObject(nodeType);
		IdList annotationIds = new IdList(annotationType);
		annotationIds.add(nonOrphan);
		getProject().setObjectData(nodeRef, nodeTagForAnnotationList, annotationIds.toString());

		EAM.setLogToString();

//		TODO: removed orphan deletion code until a solution can be found to general extentions of having annoations having annoations
		ProjectRepairer.repairAnyProblems(getProject());
		//assertContains("Deleting orphan", EAM.getLoggedString());
		//assertNull("Didn't delete orphan?", project.findObject(annotationType, orphan));
		assertEquals("Deleted non-orphan?", nonOrphan, getProject().findObject(annotationType, nonOrphan).getId());
	}
	
	public void testScanForCorruptedObjects() throws Exception
	{
		ProjectRepairer repairer = new ProjectRepairer(getProject());

		ORef indicatorRef = getProject().createObject(Indicator.getObjectType());
		Indicator indicator = (Indicator) getProject().findObject(indicatorRef);
		BaseId nonExistantId = new BaseId(500);
		CommandSetObjectData appendTask = CommandSetObjectData.createAppendIdCommand(indicator, Indicator.TAG_TASK_IDS, nonExistantId);
		getProject().executeCommand(appendTask);
		assertEquals("task not appended?", 1, indicator.getMethodRefs().size());		
		assertEquals("Didn't detect non-existant method?", 1, repairer.findAllMissingObjects().size());
		
		ORef taskRef = getProject().createObject(Task.getObjectType());
		Task task = (Task) getProject().findObject(taskRef);
		CommandSetObjectData appendSubTask = CommandSetObjectData.createAppendIdCommand(task, Task.TAG_SUBTASK_IDS, nonExistantId);
		getProject().executeCommand(appendSubTask);
		assertEquals("subtask not appended?", 1, task.getSubtaskCount());
		assertEquals("Didn't detect second instance?", 2, repairer.findAllMissingObjects().size());
	}
	
	public void testRepairUnsnappedFactorSizes() throws Exception
	{
		DiagramFactor cause1 = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		cause1.setData(DiagramFactor.TAG_SIZE, EnhancedJsonObject.convertFromDimension(new Dimension(15, 15)));
		
		DiagramFactor cause2 = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		cause2.setData(DiagramFactor.TAG_SIZE, EnhancedJsonObject.convertFromDimension(new Dimension(30, 30)));
		
		DiagramFactor cause3 = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		cause3.setData(DiagramFactor.TAG_SIZE, EnhancedJsonObject.convertFromDimension(new Dimension(31, 31)));
		
		DiagramFactor cause4 = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		cause4.setData(DiagramFactor.TAG_SIZE, EnhancedJsonObject.convertFromDimension(new Dimension(45, 45)));
		
		ProjectRepairer repairer = new ProjectRepairer(getProject());
		repairer.repair();
		
		DiagramFactor repairedCause1 = DiagramFactor.find(getProject(), cause1.getRef());
		Dimension expectedSnappedSize = new Dimension(30, 30);
		assertEquals("wrong cause1 size?", expectedSnappedSize, repairedCause1.getSize());
		
		DiagramFactor repairedCause2 = DiagramFactor.find(getProject(), cause2.getRef());
		assertEquals("wrong cause2 size?", expectedSnappedSize, repairedCause2.getSize());
		
		DiagramFactor repairedCause3 = DiagramFactor.find(getProject(), cause3.getRef());
		assertEquals("wrong cause3 size?", expectedSnappedSize, repairedCause3.getSize());
		
		DiagramFactor repairedCause4 = DiagramFactor.find(getProject(), cause4.getRef());
		assertEquals("wrong cause4 size?", new Dimension(60, 60), repairedCause4.getSize());
	}
	
	public void testRepairDiagramObjectsReferringToNonExistantTags() throws Exception
	{
		DiagramObject diagramObject = getDiagramModel().getDiagramObject();
		TaggedObjectSet taggedObjectSet = getProject().createTaggedObjectSet();
		
		ORef nonExistantTaggedObjectSetRef = new ORef(TaggedObjectSet.getObjectType(), new BaseId(9000));
		TaggedObjectSet nonExistantTaggedObjectSet = TaggedObjectSet.find(getProject(), nonExistantTaggedObjectSetRef);
		assertNull("tagged object set exists?", nonExistantTaggedObjectSet);
		
		ORef nonExistantTaggedObjectSetRef2 = new ORef(TaggedObjectSet.getObjectType(), new BaseId(9001));
		TaggedObjectSet nonExistantTaggedObjectSet2 = TaggedObjectSet.find(getProject(), nonExistantTaggedObjectSetRef2);
		assertNull("tagged object set exists?", nonExistantTaggedObjectSet2);
		
		ORefList taggedObjectSetRefs = new ORefList();
		taggedObjectSetRefs.add(nonExistantTaggedObjectSetRef);
		taggedObjectSetRefs.add(nonExistantTaggedObjectSetRef2);
		taggedObjectSetRefs.add(taggedObjectSet.getRef());
		
		CommandSetObjectData setSelectedTags = new CommandSetObjectData(diagramObject, DiagramObject.TAG_SELECTED_TAGGED_OBJECT_SET_REFS, taggedObjectSetRefs.toString());
		getProject().executeCommand(setSelectedTags);
		
		assertEquals("wrong selected tag count?", 3, diagramObject.getSelectedTaggedObjectSetRefs().size());
		
		ProjectRepairer repairer = new ProjectRepairer(getProject());
		repairer.repairDiagramObjectsReferringToNonExistantTags();
		
		ORefList selectedTagRefs = diagramObject.getSelectedTaggedObjectSetRefs();
		assertEquals("wrong selected tag count after project repair?", 1, selectedTagRefs.size());
		assertTrue("did not contain tag?", selectedTagRefs.contains(taggedObjectSet.getRef()));
	}
	
	public void testRepairAssignmentsReferringToNonExistantData() throws Exception
	{
		AssignmentId assignmentId = getProject().createAssignment();
		Assignment assignment = Assignment.find(getProject(), new ORef(Assignment.getObjectType(), assignmentId));
		assignment.setData(Assignment.TAG_ACCOUNTING_CODE, new BaseId(40000).toString());
		assignment.setData(Assignment.TAG_FUNDING_SOURCE, new BaseId(50000).toString());
		
		ProjectRepairer repairer = new ProjectRepairer(getProject());
		repairer.repairAssignmentsReferringToNonExistantData();
		
		assertTrue("assignment refers to valid accounting code?", assignment.getAccountingCodeRef().isInvalid());
		assertTrue("assignment refers to valid funding source?", assignment.getFundingSourceRef().isInvalid());
	}
	
	public void testRepairLinkedTextBoxes() throws Exception
	{
		DiagramFactor cause1 = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		DiagramFactor textBox = getProject().createDiagramFactorAndAddToDiagram(TextBox.getObjectType());
		DiagramFactor cause2 = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		getProject().createDiagramLinkAndAddToDiagram(cause1, textBox);
		getProject().createDiagramLinkAndAddToDiagram(textBox, cause2);
		
		assertEquals("more than 2 factor links exist?", 2, getProject().getFactorLinkPool().getRefList().size());
		assertEquals("more than 2 diagram links exist?", 2, getProject().getDiagramFactorLinkPool().getRefList().size());
		
		ProjectRepairer repairer = new ProjectRepairer(getProject());
		repairer.repairLinkedTextBoxes();
		
		assertEquals("FactorLinks were not deleted", 0, getProject().getFactorLinkPool().getRefList().size());
		assertEquals("DiagramLinks were not deleted?", 0, getProject().getDiagramFactorLinkPool().getRefList().size());
	}	
}
