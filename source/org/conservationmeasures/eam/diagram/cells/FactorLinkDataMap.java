/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.cells;

import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.utils.DataMap;

public class FactorLinkDataMap extends DataMap
{
	public FactorLinkDataMap()
	{
		super();
	}

	public void setId(DiagramFactorLinkId id)
	{
		putId(TAG_ID, id);
	}
	
	public DiagramFactorLinkId getId()
	{
		return new DiagramFactorLinkId(getId(TAG_ID).asInt());
	}

	public void setFromId(DiagramFactorId fromId)
	{
		putInt(TAG_FROM_ID, fromId.asInt());
	}

	public DiagramFactorId getFromId()
	{
		return new DiagramFactorId(getId(TAG_FROM_ID).asInt());
	}

	public void setToId(DiagramFactorId toId)
	{
		putInt(TAG_TO_ID, toId.asInt());
	}

	public DiagramFactorId getToId()
	{
		return new DiagramFactorId(getId(TAG_TO_ID).asInt());
	}

	public static final String TAG_ID = "Id";
	public static final String TAG_FROM_ID = "FromId";
	public static final String TAG_TO_ID = "ToId";

}
