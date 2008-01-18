/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.factortypes;

import javax.swing.Icon;

import org.conservationmeasures.eam.icons.ContributingFactorIcon;
import org.conservationmeasures.eam.icons.DirectThreatIcon;
import org.conservationmeasures.eam.icons.GroupBoxIcon;
import org.conservationmeasures.eam.icons.IntermediateResultIcon;
import org.conservationmeasures.eam.icons.StrategyIcon;
import org.conservationmeasures.eam.icons.StressIcon;
import org.conservationmeasures.eam.icons.TargetIcon;
import org.conservationmeasures.eam.icons.TextBoxIcon;
import org.conservationmeasures.eam.icons.ThreatReductionResultIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.GroupBox;
import org.conservationmeasures.eam.objects.IntermediateResult;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.objects.TextBox;
import org.conservationmeasures.eam.objects.ThreatReductionResult;



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
	
	public static String getFactorTypeLabel(Factor factor)
	{
		if(factor.isDirectThreat())
			return EAM.fieldLabel(Cause.getObjectType(), Cause.OBJECT_NAME_THREAT);
		
		if (factor.isContributingFactor())
			return EAM.fieldLabel(Cause.getObjectType(), Cause.OBJECT_NAME_CONTRIBUTING_FACTOR);
		
		if (factor.isStrategy())
			return EAM.fieldLabel(Strategy.getObjectType(), Strategy.OBJECT_NAME);
		
		if (factor.isTarget())
			return EAM.fieldLabel(Target.getObjectType(), Target.OBJECT_NAME);
		
		if (factor.isIntermediateResult())
			return EAM.fieldLabel(IntermediateResult.getObjectType(), IntermediateResult.OBJECT_NAME);

		if (factor.isThreatReductionResult())
			return EAM.fieldLabel(ThreatReductionResult.getObjectType(), ThreatReductionResult.OBJECT_NAME);
		
		if (factor.isTextBox())
			return EAM.fieldLabel(TextBox.getObjectType(), TextBox.OBJECT_NAME);
		
		if (factor.isGroupBox())
			return EAM.fieldLabel(GroupBox.getObjectType(), GroupBox.OBJECT_NAME);
		
		throw new RuntimeException("Unknown factor type " + factor.getRef());
	}

	
	public static Icon getFactorIcon(Factor factor) throws Exception
	{
		if (factor.isDirectThreat())
			return new DirectThreatIcon();
		
		if (factor.isContributingFactor())
			return new ContributingFactorIcon();
		
		if (factor.isStrategy())
			return new StrategyIcon();
		
		if (factor.isTarget())
			return new TargetIcon();
		
		if (factor.isStress())
			return new StressIcon();
		
		if (factor.isIntermediateResult())
			return new IntermediateResultIcon();
		
		if (factor.isThreatReductionResult())
			return new ThreatReductionResultIcon();
		
		if (factor.isTextBox())
			return new TextBoxIcon();
		
		if (factor.isGroupBox())
			return new GroupBoxIcon();
		
		throw new RuntimeException("Unknown factor type: " + factor.getRef());
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
