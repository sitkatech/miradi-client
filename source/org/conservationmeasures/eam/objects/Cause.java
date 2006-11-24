/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.diagram.factortypes.FactorTypeCause;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class Cause extends Factor
{
	public Cause(ModelNodeId idToUse)
	{
		super(idToUse, new FactorTypeCause());
		clear();
	}
	
	public Cause(ModelNodeId idToUse, EnhancedJsonObject json) throws Exception
	{
		super(idToUse, Factor.TYPE_CAUSE, json);
	}

	public boolean isFactor()
	{
		return true;
	}
	
	public boolean isIndirectFactor()
	{
		return !isDirectThreat();
	}
	
	public boolean isDirectThreat()
	{
		return (targetCount > 0);
	}
	
	public void increaseTargetCount()
	{
		++targetCount;
	}
	
	public void decreaseTargetCount()
	{
		--targetCount;
	}
	
	public boolean canHaveObjectives()
	{
		return true;
	}
	
	void clear()
	{
		super.clear();
		taxonomyCode = new StringData();
		addField(TAG_TAXONOMY_CODE, taxonomyCode);
	}	
	
	public static final String TAG_TAXONOMY_CODE = "TaxonomyCode";
	StringData taxonomyCode; 
	
	private int targetCount;

}
