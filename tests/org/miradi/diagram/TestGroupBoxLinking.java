/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.diagram;

import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.CreateDiagramFactorLinkParameter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.GroupBox;
import org.miradi.objects.Target;
import org.miradi.views.diagram.LinkCreator;

public class TestGroupBoxLinking extends TestCaseWithProject
{
	public TestGroupBoxLinking(String name)
	{
		super(name);
	}

	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		
		linkCreator = new LinkCreator(getProject());
		cause1 = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		cause2 = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		target = getProject().createDiagramFactorAndAddToDiagram(Target.getObjectType());
		
		causeGroupBox = getProject().createDiagramFactorAndAddToDiagram(GroupBox.getObjectType());
		ORefList causeGroupBoxChildrenRefs = new ORefList();
		causeGroupBoxChildrenRefs.add(cause1.getRef());
		causeGroupBoxChildrenRefs.add(cause2.getRef());
		getProject().setObjectData(causeGroupBox.getRef(), DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS, causeGroupBoxChildrenRefs.toString());
		
		targetGroupBox = getProject().createDiagramFactorAndAddToDiagram(GroupBox.getObjectType());
		ORefList targetGroupBoxChildrenRefs = new ORefList(target.getRef());
		getProject().setObjectData(targetGroupBox.getRef(), DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS, targetGroupBoxChildrenRefs.toString());		
	}

	//NOTE:
	//All possible asserts need to be done for each startin point
	//c1  -> t
	//c2  -> t
	//c1  -> tgb
	//c2  -> tgb
	//cgb -> t
	//cgb -> tgb

	//c1  <- t
	//c2  <- t
	//c1  <- tgb
	//c2  <- tgb
	//cgb <- t
	//cgb <- tgb

	//STARTING POINT no links

	//STARTING POINT c1 -> t

	//STARTING POINT cgb -> t

	//STARTING POINT c1 -> t + c2 -> t

	//STARTING POINT c1 -> t + c2 -> tgb

	//STARTING POINT c1 -> tgb + c2 -> tbg

	//STARTING POINT cgb -> tgb		
	
	public void testInitialValues()
	{
		assertTrue("causeGroupBox is not groupBox?", causeGroupBox.isGroupBoxFactor());
		assertTrue("targetGroupBox is not groupBox?", targetGroupBox.isGroupBoxFactor());
	}
	
	public void testNoLinks() throws Exception
	{
		verifyIfLinkCanBeCreated("cannot create link (c1 -> t)?", cause1, target);
		verifyIfLinkCanBeCreated("cannot create link (c2 -> t)?", cause2, target);
		verifyIfLinkCanBeCreated("cannot create link (c1 -> tgb)?", cause1, targetGroupBox);
		verifyIfLinkCanBeCreated("cannot create link (c2 -> tgb)?", cause2, targetGroupBox);
		verifyIfLinkCanBeCreated("cannot create link (cgb -> t)?", causeGroupBox, target);
		verifyIfLinkCanBeCreated("cannot create link (cgb -> tgb)?", causeGroupBox, targetGroupBox);	
		
		verifyIfLinkCanBeCreated("cannot create link (c1 <- t)?", target, cause1);
		verifyIfLinkCanBeCreated("cannot create link (c2 <- t)?", target, cause2);
		verifyIfLinkCanBeCreated("cannot create link (c1 <- tgb)?", targetGroupBox, cause1);
		verifyIfLinkCanBeCreated("cannot create link (c2 <- tgb)?", targetGroupBox, cause2);
		verifyIfLinkCanBeCreated("cannot create link (cgb <- t)?", target, causeGroupBox);
		verifyIfLinkCanBeCreated("cannot create link (cgb <- tgb)?", targetGroupBox, causeGroupBox);
	}
	
	public void testCause1ToTarget() throws Exception
	{
		getProject().createDiagramLinkAndAddToDiagram(cause1, target);
		verifyIfLinkCannotBeCreated("can create link (c1 -> t)", cause1, target);
		verifyIfLinkCanBeCreated("cannot create link (c2 -> t)", cause2, target);
		verifyIfLinkCanBeCreated("cannot create link (c1 -> tgb)", cause1, targetGroupBox);
		verifyIfLinkCanBeCreated("cannot create link (c2 -> tgb)", cause2, targetGroupBox);
		verifyIfLinkCanBeCreated("cannot create link (cgb -> t)", causeGroupBox, target);
		verifyIfLinkCanBeCreated("cannot create link (cgb -> tgb)", causeGroupBox, targetGroupBox);
		
		verifyIfLinkCannotBeCreated("can create link (c1 <- t)", target, cause1);
		verifyIfLinkCanBeCreated("cannot create link (c2 <- t)", target, cause2);
		verifyIfLinkCanBeCreated("cannot create link (c1 <- tgb)", targetGroupBox, cause1);
		verifyIfLinkCanBeCreated("cannot create link (c2 <- tgb)", targetGroupBox, cause2);
		verifyIfLinkCanBeCreated("cannot create link (cgb <- t)", target, causeGroupBox);
		verifyIfLinkCanBeCreated("cannot create link (cgb <- tgb)", targetGroupBox, causeGroupBox);
	}
	
	public void testCause1GroupBoxToTarget() throws Exception
	{
		ORef cause1ToTargetRef = getProject().createDiagramLinkAndAddToDiagram(cause1, target);
		ORef cause2ToTargetRef = getProject().createDiagramLinkAndAddToDiagram(cause2, target);
			
		ORefList coveredLinkRefs = new ORefList();
		coveredLinkRefs.add(cause1ToTargetRef);
		coveredLinkRefs.add(cause2ToTargetRef);
		createGroupBoxLink(causeGroupBox.getRef(), target.getRef(), coveredLinkRefs);
		
		verifyIfLinkCannotBeCreated("can create link (c1 -> t)?", cause1, target);
		verifyIfLinkCannotBeCreated("can create link (c2 -> t)?", cause2, target);
		verifyIfLinkCannotBeCreated("can create link (c1 -> tgb)?", cause1, targetGroupBox);
		verifyIfLinkCannotBeCreated("can create link (c2 -> tgb)?", cause2, targetGroupBox);
		verifyIfLinkCanBeCreated("cannot create link (cgb -> tgb)?", causeGroupBox, targetGroupBox);
		
		verifyIfLinkCannotBeCreated("can create link (c1 <- t)?", target, cause1);
		verifyIfLinkCannotBeCreated("can create link (c2 <- t)?", target, cause2);
		verifyIfLinkCannotBeCreated("can create link (c1 <- tgb)?", targetGroupBox, cause1);
		verifyIfLinkCannotBeCreated("can create link (c2 <- tgb)?", targetGroupBox, cause2);
		verifyIfLinkCanBeCreated("cannot create link (cgb <- tgb)?", targetGroupBox, causeGroupBox);
	}
	
	public void testCause1ToTargetPlusCause2ToTarget() throws Exception
	{
		getProject().createDiagramLinkAndAddToDiagram(cause1, target);
		getProject().createDiagramLinkAndAddToDiagram(cause2, target);
		
		verifyIfLinkCanBeCreated("cannot create link (cgb -> t)?", causeGroupBox, target);
		verifyIfLinkCanBeCreated("cannot create link (cgb -> tgb)?", causeGroupBox, targetGroupBox);
		verifyIfLinkCannotBeCreated("can create link (c1 -> t)?", cause1, target);
		verifyIfLinkCannotBeCreated("can create link (c2 -> t)?", cause2, target);
		verifyIfLinkCanBeCreated("cannot create link (c1 -> tgb)?", cause1, targetGroupBox);
		verifyIfLinkCanBeCreated("cannot create link (c2 -> tgb)?", cause2, targetGroupBox);
		
		verifyIfLinkCanBeCreated("cannot create link (cgb <- t)?", target, causeGroupBox);
		verifyIfLinkCanBeCreated("cannot create link (cgb <- tgb)?", targetGroupBox, causeGroupBox);
		verifyIfLinkCannotBeCreated("can create link (c1 <- t)?", target, cause1);
		verifyIfLinkCannotBeCreated("can create link (c2 <- t)?", target, cause2);
		verifyIfLinkCanBeCreated("cannot create link (c1 <- tgb)?", targetGroupBox, cause1);
		verifyIfLinkCanBeCreated("cannot create link (c2 <- tgb)?", targetGroupBox, cause2);		
	}
	
	public void testCause1ToTargetPlusCause2ToTargetGroupBox() throws Exception
	{
		getProject().createDiagramLinkAndAddToDiagram(cause1, target);
		
		ORef cause2ToTargetRef = getProject().createDiagramLinkAndAddToDiagram(cause2, target);
		ORefList cause2ToTargetCoveredLinkRefs = new ORefList(cause2ToTargetRef);
		createGroupBoxLink(cause2.getRef(), targetGroupBox.getRef(), cause2ToTargetCoveredLinkRefs);
		
		verifyIfLinkCannotBeCreated("can create link (c1 -> t)?", cause1, target);
		verifyIfLinkCannotBeCreated("can create link (c2 -> tgb)?", cause2, targetGroupBox);
		verifyIfLinkCannotBeCreated("can create link (c2 -> t)?", cause2, target);
		verifyIfLinkCanBeCreated("cannot create link (c1 -> tgb)?", cause1, targetGroupBox);
		verifyIfLinkCannotBeCreated("can create link (cgb -> t)?", causeGroupBox, target);
		verifyIfLinkCanBeCreated("cannot create link (cgb -> tgb)?", causeGroupBox, targetGroupBox);
		
		verifyIfLinkCannotBeCreated("can create link (c1 <- t)?", target, cause1);
		verifyIfLinkCannotBeCreated("can create link (c2 <- tgb)?", targetGroupBox, cause2);
		verifyIfLinkCannotBeCreated("can create link (c2 <- t)?", target, cause2);
		verifyIfLinkCanBeCreated("cannot create link (c1 <- tgb)?", targetGroupBox, cause1);
		verifyIfLinkCannotBeCreated("can create link (cgb <- t)?", target, causeGroupBox);
		verifyIfLinkCanBeCreated("cannot create link (cgb <- tgb)?", targetGroupBox, causeGroupBox);	
	}
	
	public void testCause1ToTargetGroupBoxPlusCause2ToTargetGroupBox() throws Exception
	{
		ORef cause1ToTargetRef = getProject().createDiagramLinkAndAddToDiagram(cause1, target);
		ORefList cause1ToTargetCoveredLinkRefs = new ORefList(cause1ToTargetRef);
		createGroupBoxLink(cause1.getRef(), targetGroupBox.getRef(), cause1ToTargetCoveredLinkRefs);
		
		ORef cause2ToTargetRef = getProject().createDiagramLinkAndAddToDiagram(cause2, target);
		ORefList cause2ToTargetCoveredLinkRefs = new ORefList(cause2ToTargetRef);
		createGroupBoxLink(cause2.getRef(), targetGroupBox.getRef(), cause2ToTargetCoveredLinkRefs);
	
		verifyIfLinkCannotBeCreated("can create link (c1 -> tgb)?", cause1, targetGroupBox);
		verifyIfLinkCannotBeCreated("can create link (c2 -> tgb)?", cause2, targetGroupBox);
		verifyIfLinkCannotBeCreated("can create link (c1 -> t)?", cause1, target);
		verifyIfLinkCannotBeCreated("can create link (c2 -> t)?", cause2, target);
		verifyIfLinkCannotBeCreated("can create link (cgb -> t)?", causeGroupBox, target);
		verifyIfLinkCanBeCreated("cannot create link (cgb -> tgb)?", causeGroupBox, targetGroupBox);
		
		verifyIfLinkCannotBeCreated("can create link (c1 <- tgb)?", targetGroupBox, cause1);
		verifyIfLinkCannotBeCreated("can create link (c2 <- tgb)?", targetGroupBox, cause2);
		verifyIfLinkCannotBeCreated("can create link (c1 <- t)?", target, cause1);
		verifyIfLinkCannotBeCreated("can create link (c2 <- t)?", target, cause2);
		verifyIfLinkCannotBeCreated("can create link (cgb <- t)?", target, causeGroupBox);
		verifyIfLinkCanBeCreated("cannot create link (cgb <- tgb)?", targetGroupBox, causeGroupBox);
	}
	
	public void testCauseGroupBoxToTargetGroupBox() throws Exception
	{
		ORef cause1ToTargetRef = getProject().createDiagramLinkAndAddToDiagram(cause1, target);
		ORef cause2ToTargetRef = getProject().createDiagramLinkAndAddToDiagram(cause2, target);
		ORefList coveredLinkRefs = new ORefList();
		coveredLinkRefs.add(cause1ToTargetRef);
		coveredLinkRefs.add(cause2ToTargetRef);
		createGroupBoxLink(causeGroupBox.getRef(), targetGroupBox.getRef(), coveredLinkRefs);
		
		verifyIfLinkCannotBeCreated("can create link (c1 -> t)?", cause1, target);
		verifyIfLinkCannotBeCreated("can create link (c2 -> t)?", cause2, target);
		verifyIfLinkCannotBeCreated("can create link (c1 -> tgb)?", cause1, targetGroupBox);
		verifyIfLinkCannotBeCreated("can create link (c2 -> tgb)?", cause2, targetGroupBox);
		verifyIfLinkCannotBeCreated("can create link (cgb -> t)?", causeGroupBox, target);
		verifyIfLinkCannotBeCreated("can create link (cgb -> tgb)?", causeGroupBox, targetGroupBox);
		
		verifyIfLinkCannotBeCreated("can create link (c1 <- t)?", target, cause1);
		verifyIfLinkCannotBeCreated("can create link (c2 <- t)?", target, cause2);
		verifyIfLinkCannotBeCreated("can create link (c1 <- tgb)?", targetGroupBox, cause1);
		verifyIfLinkCannotBeCreated("can create link (c2 <- tgb)?", targetGroupBox, cause2);
		verifyIfLinkCannotBeCreated("can create link (cgb <- t)?", target, causeGroupBox);
		verifyIfLinkCannotBeCreated("can create link (cgb <- tgb)?", targetGroupBox, causeGroupBox);
	}
	
	public void verifyIfLinkCanBeCreated(String message, DiagramFactor from, DiagramFactor to) throws Exception
	{
		assertTrue(message, linkCreator.canBeLinked(from, to));
	}
	
	public void verifyIfLinkCannotBeCreated(String message, DiagramFactor from, DiagramFactor to) throws Exception
	{
		assertFalse(message, linkCreator.canBeLinked(from, to));
	}
	
	private void createGroupBoxLink(ORef fromRef, ORef toRef, ORefList coveredLinkRefs) throws Exception
	{
		CreateDiagramFactorLinkParameter extraInfoWithNoFactorLink = new CreateDiagramFactorLinkParameter(fromRef, toRef);
		ORef newGroupBoxDiagramLinkRef = linkCreator.createDiagramLink(getProject().getDiagramObject(), extraInfoWithNoFactorLink);
	
		linkCreator.updateGroupBoxChildrenRefs(coveredLinkRefs, newGroupBoxDiagramLinkRef);
	}
	
	private DiagramFactor cause1;
	private DiagramFactor cause2;
	private DiagramFactor target;
	private DiagramFactor causeGroupBox;
	private DiagramFactor targetGroupBox;
	private LinkCreator linkCreator;
}
