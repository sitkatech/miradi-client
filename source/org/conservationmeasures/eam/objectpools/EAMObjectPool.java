/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectpools;

import java.io.IOException;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.martus.util.UnicodeWriter;

public class EAMObjectPool extends ObjectPool
{
	public EAMObjectPool(int objectTypeToStore)
	{
		super(objectTypeToStore);
	}
	
	public BaseObject findObject(BaseId id)
	{
		return (BaseObject)getRawObject(id);
	}
	
	public ORefList getORefList()
	{
		return new ORefList(getObjectType(), new IdList(getObjectType(), getIds()));
	}

	
	public void toXml(UnicodeWriter out) throws IOException
	{
		out.writeln("<Pool type='" + getObjectType() + "'>");
		IdList ids = getIdList();
		for(int i = 0; i < ids.size(); ++i)
		{
			findObject(ids.get(i)).toXml(out);
		}
		out.writeln("</Pool>");
	}
}
