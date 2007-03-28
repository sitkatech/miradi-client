/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramContentsId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class DiagramContentsObject extends BaseObject
{
	public DiagramContentsObject(BaseId idToUse)
	{
		super(idToUse);
	}
	
	public DiagramContentsObject(int idToUse, EnhancedJsonObject json) throws Exception
	{
		super(new DiagramContentsId(idToUse), json);
	}
	
	public int getType()
	{
		return ObjectType.DIAGRAM_CONTENTS;
	}
	
	public void clear()
	{
		super.clear();
	}
}
