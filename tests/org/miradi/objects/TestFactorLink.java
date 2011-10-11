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
import org.miradi.utils.EnhancedJsonObject;

public class TestFactorLink extends ObjectTestCase
{
	public TestFactorLink(String name) 
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		FactorLink linkageData = createFactorLink();
		assertEquals("Id not the same?", id, linkageData.getId());
	}

	public void testFields() throws Exception
	{
		verifyFields(ObjectType.FACTOR_LINK);
	}
	
	public void testToJson() throws Exception
	{
		FactorLink original = createFactorLink();
		EnhancedJsonObject json = original.toJson();
		FactorLink gotBack = (FactorLink)BaseObject.createFromJson(getProject().getObjectManager(), original.getType(), json);
		assertEquals("wrong id?", original.getId(), gotBack.getId());
		assertEquals("wrong from?", original.getFromFactorRef(), gotBack.getFromFactorRef());
		assertEquals("wrong to?", original.getToFactorRef(), gotBack.getToFactorRef());
	}

	private FactorLink createFactorLink() throws Exception
	{
		DiagramFactor nodeA = getProject().createDiagramFactorAndAddToDiagram(ObjectType.CAUSE);
		DiagramFactor nodeB = getProject().createDiagramFactorAndAddToDiagram(ObjectType.CAUSE);
		final FactorLink factorLink = new FactorLink(getObjectManager(), id);
		factorLink.setData(FactorLink.TAG_FROM_REF, nodeA.getWrappedORef().toString());
		factorLink.setData(FactorLink.TAG_TO_REF, nodeB.getWrappedORef().toString());
		
		return factorLink;
	}
	
	private static final BaseId id = new BaseId(1);
}
