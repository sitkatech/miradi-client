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

import org.miradi.ids.IdList;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.CreateDiagramFactorParameter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Task;
import org.miradi.views.diagram.DiagramView;

public class TestDiagramView extends TestCaseWithProject
{

	public TestDiagramView(String name) 
	{
		super(name);
	}

	public void testBasics()
	{
		// no tests here yet
	}
	
	public void testHasCorruptedDiagramFactors() throws Exception
	{
		DiagramObject diagramObject = getProject().getDiagramObject();
		assertFalse("detected corrupted diagram factors?", DiagramView.hasCorruptedDiagramFactors(getProject(), diagramObject));
		
		Task task = getProject().createTask();
		CreateDiagramFactorParameter extraDiagramFactorInfo = new CreateDiagramFactorParameter(task.getRef());
		ORef diagramFactorRef = getProject().createObject(DiagramFactor.getObjectType(), extraDiagramFactorInfo);
		IdList diagramFactorIds = new IdList(DiagramFactor.getObjectType());
		diagramFactorIds.addRef(diagramFactorRef);
		getProject().setObjectData(diagramObject.getRef(), DiagramObject.TAG_DIAGRAM_FACTOR_IDS, diagramFactorIds.toString());
		
		assertTrue("did't detect task diagram factor that is not activity?", DiagramView.hasCorruptedDiagramFactors(getProject(), diagramObject));
		
		getProject().deleteObject(task);
		
		assertTrue("didn't detect diagram factor that has no wrapped ref?", DiagramView.hasCorruptedDiagramFactors(getProject(), diagramObject));	
	}
	
	public void testhasCorruptedDiagramLinks() throws Exception
	{
		DiagramObject diagramObject = getProject().getDiagramObject();
		assertFalse("detected corrupted diagram links?", DiagramView.hasCorruptedDiagramLinks(getProject(), diagramObject));
		
		ORef diagramLinkRef = getProject().createDiagramLink();
		IdList diagramLinkIds = new IdList(DiagramLink.getObjectType());
		diagramLinkIds.addRef(diagramLinkRef);
		getProject().setObjectData(diagramObject.getRef(), DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, diagramLinkIds.toString());
		
		DiagramLink diagramLink = DiagramLink.find(getProject(), diagramLinkRef);
		DiagramFactor fromDiagramFactor = diagramLink.getFromDiagramFactor();
		Factor fromFactor = fromDiagramFactor.getWrappedFactor();
		
		FactorLink factorLink = FactorLink.find(getProject(), diagramLink.getWrappedRef());
		getProject().deleteObject(factorLink);
		assertTrue("did not find corrupted link with missing from wrapped factor link?", DiagramView.hasCorruptedDiagramLinks(getProject(), diagramObject));
		
		getProject().deleteObject(fromFactor);
		assertTrue("did not find corrupted link with missing from wrapped factor?", DiagramView.hasCorruptedDiagramLinks(getProject(), diagramObject));
		
		getProject().deleteObject(fromDiagramFactor);
		assertTrue("did not find corrupted link with missing diagram factor end?", DiagramView.hasCorruptedDiagramLinks(getProject(), diagramObject));
	}
}
