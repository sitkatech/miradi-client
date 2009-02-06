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
package org.miradi.utils;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.CreateDiagramFactorLinkParameter;
import org.miradi.objecthelpers.CreateDiagramFactorParameter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;
import org.miradi.objects.GroupBox;
import org.miradi.objects.Target;
import org.miradi.objects.Task;

public class TestDiagramCorruptionDetector extends TestCaseWithProject
{
	public TestDiagramCorruptionDetector(String name)
	{
		super(name);
	}
	
	
	public void testHasCorruptedDiagramFactors() throws Exception
	{
		DiagramObject diagramObject = getProject().getTestingDiagramObject();
		assertFalse("detected corrupted diagram factors?", DiagramCorruptionDetector.getCorruptedDiagramFactorErrorMessages(getProject(), diagramObject).size() > 0);
		
		Task task = getProject().createTask();
		CreateDiagramFactorParameter extraDiagramFactorInfo = new CreateDiagramFactorParameter(task.getRef());
		ORef diagramFactorRef = getProject().createObject(DiagramFactor.getObjectType(), extraDiagramFactorInfo);
		IdList diagramFactorIds = new IdList(DiagramFactor.getObjectType());
		diagramFactorIds.addRef(diagramFactorRef);
		getProject().setObjectData(diagramObject.getRef(), DiagramObject.TAG_DIAGRAM_FACTOR_IDS, diagramFactorIds.toString());
		
		assertTrue("did't detect task diagram factor that is not activity?", DiagramCorruptionDetector.getCorruptedDiagramFactorErrorMessages(getProject(), diagramObject).size() > 0);
		
		getProject().deleteObject(task);
		
		assertTrue("didn't detect diagram factor that has no wrapped ref?", DiagramCorruptionDetector.getCorruptedDiagramFactorErrorMessages(getProject(), diagramObject).size() > 0);
		
		IdList idsWithNonExistingObjectId = new IdList(DiagramFactor.getObjectType());
		idsWithNonExistingObjectId.add(new BaseId(9999));
		getProject().setObjectData(diagramObject.getRef(), DiagramObject.TAG_DIAGRAM_FACTOR_IDS, idsWithNonExistingObjectId.toString());
		assertTrue("didn't detect diagram factor that does not exist?", DiagramCorruptionDetector.getCorruptedDiagramFactorErrorMessages(getProject(), diagramObject).size() > 0);
	}
	
	public void testHasCorruptedDiagramLinks() throws Exception
	{
		DiagramObject diagramObject = getProject().getTestingDiagramObject();
		assertFalse("detected corrupted diagram links?", DiagramCorruptionDetector.getCorruptedDiagramLinksErrorMessages(getProject(), diagramObject).size() > 0);

		BaseId bogusDiagramLinkId = new BaseId(9999);
		IdList diagramLinkIdsWithBogusId = new IdList(DiagramLink.getObjectType());
		diagramLinkIdsWithBogusId.add(bogusDiagramLinkId);
		getProject().setObjectData(diagramObject.getRef(), DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, diagramLinkIdsWithBogusId.toString());
		assertTrue("did not detect corrupted diagram with missing link?", DiagramCorruptionDetector.getCorruptedDiagramLinksErrorMessages(getProject(), diagramObject).size() > 0);
		
		ORef diagramLinkRef = getProject().createDiagramLink();
		IdList diagramLinkIds = new IdList(DiagramLink.getObjectType());
		diagramLinkIds.addRef(diagramLinkRef);
		getProject().setObjectData(diagramObject.getRef(), DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, diagramLinkIds.toString());
		
		DiagramLink diagramLink = DiagramLink.find(getProject(), diagramLinkRef);
		DiagramFactor fromDiagramFactor = diagramLink.getFromDiagramFactor();
		Factor fromFactor = fromDiagramFactor.getWrappedFactor();
		
		FactorLink factorLink = FactorLink.find(getProject(), diagramLink.getWrappedRef());
		getProject().deleteObject(factorLink);
		assertTrue("did not detect corrupted diagram with corrupted link with missing from wrapped factor link?", DiagramCorruptionDetector.getCorruptedGroupBoxDiagramLinks(getProject(), diagramObject).size() > 0);
		
		getProject().deleteObject(fromFactor);
		assertTrue("did not detect corrupted diagram with  corrupted link with missing from wrapped factor?", DiagramCorruptionDetector.getCorruptedDiagramLinksErrorMessages(getProject(), diagramObject).size() > 0);
		
		getProject().deleteObject(fromDiagramFactor);
		assertTrue("did not detect corrupted diagram with  corrupted link with missing diagram factor end?", DiagramCorruptionDetector.getCorruptedDiagramLinksErrorMessages(getProject(), diagramObject).size() > 0);
	}
	
	public void testGroupBoxLinksShouldBeIgnored() throws Exception
	{
		DiagramObject diagramObject = getProject().getTestingDiagramObject();
		assertFalse("detected corrupted diagram links?", DiagramCorruptionDetector.getCorruptedDiagramLinksErrorMessages(getProject(), diagramObject).size() > 0);
		
		ORef groupBoxDiagramFactorRef = createDiagramFactorAndAddToDiagram(diagramObject, GroupBox.getObjectType());
		ORef targetDiagramFactorRef = createDiagramFactorAndAddToDiagram(diagramObject, Target.getObjectType());
		
		ORef causeDiagramFactorRef = createDiagramFactorAndAddToDiagram(diagramObject, Target.getObjectType());
		ORefList groupBoxChildRefs = new ORefList(causeDiagramFactorRef);
		getProject().setObjectData(groupBoxDiagramFactorRef, DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS, groupBoxChildRefs.toString());
		
		DiagramLink diagramLink = createDiagramLink(diagramObject, groupBoxDiagramFactorRef, targetDiagramFactorRef);
		assertTrue("is not group box link?", diagramLink.isGroupBoxLink());
		
		assertFalse("group box link is included in corrupted project test?", DiagramCorruptionDetector.getCorruptedDiagramLinksErrorMessages(getProject(), diagramObject).size() > 0);
	}


	private DiagramLink createDiagramLink(DiagramObject diagramObject,
			ORef groupBoxDiagramFactorRef, ORef targetDiagramFactorRef)
			throws Exception
	{
		CreateDiagramFactorLinkParameter diagramLinkExtraInfo = new CreateDiagramFactorLinkParameter(targetDiagramFactorRef, groupBoxDiagramFactorRef);
		ORef diagramLinkRef = getProject().createObject(DiagramLink.getObjectType(), diagramLinkExtraInfo);
		
		IdList diagramLinkIds = new IdList(DiagramLink.getObjectType());
		diagramLinkIds.addRef(diagramLinkRef);
		getProject().setObjectData(diagramObject.getRef(), DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, diagramLinkIds.toString());
		DiagramLink diagramLink = DiagramLink.find(getProject(), diagramLinkRef);
		return diagramLink;
	}


	private ORef createDiagramFactorAndAddToDiagram(DiagramObject diagramObject, int type) throws Exception
	{
		ORef ref = getProject().createObject(type);
		CreateDiagramFactorParameter extraInfo = new CreateDiagramFactorParameter(ref);
		ORef diagramFactorRef = getProject().createObject(DiagramFactor.getObjectType(), extraInfo);
		
		IdList diagramFactorIds = diagramObject.getAllDiagramFactorRefs().convertToIdList(DiagramFactor.getObjectType());
		diagramFactorIds.addRef(diagramFactorRef);
		getProject().setObjectData(diagramObject.getRef(), DiagramObject.TAG_DIAGRAM_FACTOR_IDS, diagramFactorIds.toString());
		
		return diagramFactorRef;
	}
}
