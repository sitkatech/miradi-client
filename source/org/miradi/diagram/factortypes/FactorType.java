/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.diagram.factortypes;

import javax.swing.Icon;

import org.miradi.icons.ActivityIcon;
import org.miradi.icons.ContributingFactorIcon;
import org.miradi.icons.DirectThreatIcon;
import org.miradi.icons.GroupBoxIcon;
import org.miradi.icons.IntermediateResultIcon;
import org.miradi.icons.StrategyIcon;
import org.miradi.icons.StressIcon;
import org.miradi.icons.TargetIcon;
import org.miradi.icons.TextBoxIcon;
import org.miradi.icons.ThreatReductionResultIcon;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Cause;
import org.miradi.objects.Factor;
import org.miradi.objects.GroupBox;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.Strategy;
import org.miradi.objects.Stress;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.TextBox;
import org.miradi.objects.ThreatReductionResult;



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
	
	public boolean isStress()
	{
		return false;
	}
	
	public boolean isActivity()
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
		
		if (factor.isStress())
			return EAM.fieldLabel(Stress.getObjectType(), Stress.OBJECT_NAME);
		
		if (factor.isActivity())
			return EAM.fieldLabel(Task.getObjectType(), Task.ACTIVITY_NAME);
		
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
		
		if (factor.isActivity())
			return new ActivityIcon();
		
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
		
		else if (factorType.equals(FactorTypeStress.STRESS_TYPE))
			return ObjectType.STRESS;
		
		else if (factorType.equals(FactorTypeActivity.ACTIVITY_TYPE))
			return ObjectType.TASK;
		
		throw new RuntimeException("Unknown factor type: " + factorType);
	}

	public abstract String toString();

}
