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
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ProjectForTesting;
import org.miradi.utils.EnhancedJsonObject;

public class TestFactorLink extends ObjectTestCase
{
	public TestFactorLink(String name) 
	{
		super(name);
	}

	public void testBasics() throws Exception
	{
		FactorLink linkageData = new FactorLink(getObjectManager(), id, nodeA.getWrappedORef(), nodeB.getWrappedORef());
		assertEquals("Id not the same?", id, linkageData.getId());
	}

	public void testFields() throws Exception
	{
		verifyFields(ObjectType.FACTOR_LINK);
	}
	
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		project = new ProjectForTesting(getName());
		nodeA = project.createDiagramFactorAndAddToDiagram(ObjectType.CAUSE);
		nodeB = project.createDiagramFactorAndAddToDiagram(ObjectType.CAUSE);
	}

	@Override
	public void tearDown() throws Exception
	{
		project.close();
		project = null;
		super.tearDown();
	}

	public void testToJson() throws Exception
	{
		FactorLink original = new FactorLink(getObjectManager(), id, nodeA.getWrappedORef(), nodeB.getWrappedORef());
		EnhancedJsonObject json = original.toJson();
		FactorLink gotBack = (FactorLink)BaseObject.createFromJson(project.getObjectManager(), original.getType(), json);
		assertEquals("wrong id?", original.getId(), gotBack.getId());
		assertEquals("wrong from?", original.getFromFactorRef(), gotBack.getFromFactorRef());
		assertEquals("wrong to?", original.getToFactorRef(), gotBack.getToFactorRef());
	}
	
	static final BaseId id = new BaseId(1);
	DiagramFactor nodeA;
	DiagramFactor nodeB;
	ProjectForTesting project;
}
