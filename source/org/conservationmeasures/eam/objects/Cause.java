/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.diagram.factortypes.FactorTypeCause;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class Cause extends Factor
{
	public Cause(FactorId idToUse)
	{
		super(idToUse, new FactorTypeCause());
		clear();
	}
	
	public Cause(FactorId idToUse, EnhancedJsonObject json) throws Exception
	{
		super(idToUse, Factor.TYPE_CAUSE, json);
	}

	public boolean isCause()
	{
		return true;
	}
	
	public boolean isContributingFactor()
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
	public static final String OBJECT_NAME = "Cause";
	StringData taxonomyCode; 
	
	private int targetCount;

}
