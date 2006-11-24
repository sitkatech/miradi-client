/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.cells;

import java.text.ParseException;

import org.conservationmeasures.eam.diagram.factortypes.FactorType;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeCause;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeCluster;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeStrategy;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeTarget;
import org.conservationmeasures.eam.utils.DataMap;
import org.json.JSONObject;


public class FactorDataMap extends DataMap 
{
	public static final int INT_TYPE_INVALID = -1;
	public static final int INT_TYPE_TARGET = 1;
	public static final int INT_TYPE_INDIRECT_FACTOR = 2;
	public static final int INT_TYPE_INTERVENTION = 3;
	public static final int INT_TYPE_DIRECT_THREAT = 4;
	public static final int INT_TYPE_CLUSTER = 5;

	public FactorDataMap()
	{
	}
	
	public FactorDataMap(JSONObject copyFrom) throws ParseException
	{
		super(copyFrom);
	}
	
	public static FactorType convertIntToNodeType(int rawType)
	{
		switch(rawType)
		{
			case FactorDataMap.INT_TYPE_TARGET:
				return new FactorTypeTarget();
			case FactorDataMap.INT_TYPE_INDIRECT_FACTOR:
				return new FactorTypeCause();
			case FactorDataMap.INT_TYPE_DIRECT_THREAT:
				return new FactorTypeCause();
			case FactorDataMap.INT_TYPE_INTERVENTION:
				return new FactorTypeStrategy();
			case FactorDataMap.INT_TYPE_CLUSTER:
				return new FactorTypeCluster();
			default:
				throw new RuntimeException("Unknown factor type: " + rawType);
		}
		
	}
	
	public static int convertNodeTypeToInt(FactorType type)
	{
		if(type.isTarget())
			return FactorDataMap.INT_TYPE_TARGET;
		if(type.isContributingFactor())
			return FactorDataMap.INT_TYPE_INDIRECT_FACTOR;
		if(type.isStrategy())
			return FactorDataMap.INT_TYPE_INTERVENTION;
		if(type.isDirectThreat())
			return FactorDataMap.INT_TYPE_INDIRECT_FACTOR;
		if(type.isFactorCluster())
			return FactorDataMap.INT_TYPE_CLUSTER;
		
		if(type.isCause())
			return FactorDataMap.INT_TYPE_INDIRECT_FACTOR;

		throw new RuntimeException("Unknown factor type: " + type);
	}
}
