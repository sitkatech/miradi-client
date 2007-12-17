/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.factortypes;

import org.conservationmeasures.eam.objecthelpers.ObjectType;



public abstract class FactorType
{
	public boolean isThreatReductionResult()
	{
		return false;
	}
	
	public boolean isIntermediateResult()
	{
		return false;
	}
	
	public boolean isStrategy()
	{
		return false;
	}
	
	public boolean isCause()
	{
		return isContributingFactor() || isDirectThreat();
	}
	
	public boolean isTarget()
	{
		return false;
	}
	
	public boolean isContributingFactor()
	{
		return false;
	}
	
	public boolean isDirectThreat()
	{
		return false;
	}
	
	public boolean isTextBox()
	{
		return false;
	}
	
	public boolean isFactorCluster()
	{
		return false;
	}

	public boolean equals(Object other)
	{
		return getClass().getName().equals(other.getClass().getName());
	}

	public int hashCode()
	{
		return getClass().getName().hashCode();
	}
	
	public static int getFactorTypeFromString(String factorType) throws Exception
	{
		//Note : if you change this method you could effect the migration from 19 -> 20
		if (factorType.equals(FactorTypeTarget.TARGET_TYPE))
			return ObjectType.TARGET;
		
		else if (factorType.equals(FactorTypeCause.CAUSE_TYPE))
			return ObjectType.CAUSE;
		
		else if (factorType.equals(FactorTypeStrategy.STRATEGY_TYPE))
			return ObjectType.STRATEGY;
		
		else if (factorType.equals(FactorTypeIntermediateResult.INTERMEDIATE_RESULT))
			return ObjectType.INTERMEDIATE_RESULT;
		
		else if (factorType.equals(FactorTypeThreatReductionResult.THREAT_REDUCTION_RESULT))
			return ObjectType.THREAT_REDUCTION_RESULT;
		
		else if (factorType.equals(FactorTypeTextBox.TEXT_BOX_TYPE))
			return ObjectType.TEXT_BOX;
		
		else if (factorType.equals(FactorTypeGroupBox.GROUP_BOX_TYPE))
			return ObjectType.GROUP_BOX;
		
		throw new RuntimeException("Unknown factor type: " + factorType);
	}

	public abstract String toString();

}
