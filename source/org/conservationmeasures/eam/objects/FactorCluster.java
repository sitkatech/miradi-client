/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objectdata.IdListData;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class FactorCluster extends Factor
{
	public FactorCluster(FactorId idToUse)
	{
		super(idToUse, Factor.TYPE_CLUSTER);
		clear();
	}

	public FactorCluster(FactorId idToUse, EnhancedJsonObject json) throws Exception
	{
		super(idToUse, Factor.TYPE_CLUSTER, json);
	}
	
	public boolean isFactorCluster()
	{
		return true;
	}
	
	public IdList getMemberIds()
	{
		return memberIds.getIdList();
	}

    public void clear()
    {
    	super.clear();
    	memberIds = new IdListData();
    	
    	addField(TAG_MEMBER_IDS, memberIds);
    }

	public static final String TAG_MEMBER_IDS = "Members";
	
	IdListData memberIds;
}
