/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
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
	
	public boolean isCluster()
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
