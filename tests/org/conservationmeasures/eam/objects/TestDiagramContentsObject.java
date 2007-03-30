/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;

public class TestDiagramContentsObject extends ObjectTestCase
{
	public TestDiagramContentsObject(String name)
	{
		super(name);
	}
	
	public void testFields() throws Exception
	{
		verifyFields(ObjectType.DIAGRAM_CONTENTS);
		verifyTagBehavior(DiagramContentsObject.TAG_DIAGRAM_FACTOR_IDS);
	}
	
	private void verifyTagBehavior(String tag) throws Exception
	{
		IdList list = new IdList();
		list.add(new DiagramFactorId(4));
		list.add(new DiagramFactorId(6));
		
		DiagramContentsObject diagramContentsObject = new DiagramContentsObject(new BaseId(22));
		assertEquals(tag + " didn't default properly?", "", diagramContentsObject.getData(tag));
		
		diagramContentsObject.setData(tag, list.toJson().toString());
		DiagramContentsObject got = (DiagramContentsObject) DiagramContentsObject.createFromJson(diagramContentsObject.getType(), diagramContentsObject.toJson());
		assertEquals(tag + " didn't survive json?", diagramContentsObject.getData(tag), got.getData(tag));
	}
}
