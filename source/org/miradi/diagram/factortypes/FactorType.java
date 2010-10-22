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
import org.miradi.icons.HumanWelfareTargetIcon;
import org.miradi.icons.IntermediateResultIcon;
import org.miradi.icons.ScopeBoxIcon;
import org.miradi.icons.StrategyIcon;
import org.miradi.icons.StressIcon;
import org.miradi.icons.TargetIcon;
import org.miradi.icons.TextBoxIcon;
import org.miradi.icons.ThreatReductionResultIcon;
import org.miradi.main.EAM;
import org.miradi.objects.Cause;
import org.miradi.objects.Factor;
import org.miradi.objects.GroupBox;
import org.miradi.objects.HumanWelfareTarget;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.ScopeBox;
import org.miradi.objects.Strategy;
import org.miradi.objects.Stress;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.TextBox;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.project.ObjectManager;



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
	
	public boolean isScopeBox()
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
	
	@Override
	public boolean equals(Object other)
	{
		return getClass().getName().equals(other.getClass().getName());
	}

	@Override
	public int hashCode()
	{
		return getClass().getName().hashCode();
	}
	
	public static String getFactorTypeLabel(Factor factor)
	{
		if(factor.isDirectThreat())
			return getFieldLabel(Cause.getObjectType(), Cause.OBJECT_NAME_THREAT);
		
		if (factor.isContributingFactor())
			return getFieldLabel(Cause.getObjectType(), Cause.OBJECT_NAME_CONTRIBUTING_FACTOR);
		
		if (factor.isStrategy())
			return getFactorLabel(factor.getObjectManager(), Strategy.getObjectType());
		
		if (factor.isTarget())
			return getFactorLabel(factor.getObjectManager(), Target.getObjectType());
		
		if (factor.isHumanWelfareTarget())
			return getFactorLabel(factor.getObjectManager(), HumanWelfareTarget.getObjectType());
		
		if (factor.isIntermediateResult())
			return getFactorLabel(factor.getObjectManager(), IntermediateResult.getObjectType());

		if (factor.isThreatReductionResult())
			return getFactorLabel(factor.getObjectManager(), ThreatReductionResult.getObjectType());
		
		if (factor.isTextBox())
			return getFactorLabel(factor.getObjectManager(), TextBox.getObjectType());
		
		if (factor.isScopeBox())
			return getFactorLabel(factor.getObjectManager(), ScopeBox.getObjectType());
		
		if (factor.isGroupBox())
			return getFactorLabel(factor.getObjectManager(), GroupBox.getObjectType());
		
		if (factor.isStress())
			return getFactorLabel(factor.getObjectManager(), Stress.getObjectType());
		
		if (factor.isActivity())
			return getFieldLabel(Task.getObjectType(), Task.ACTIVITY_NAME);
		
		throw new RuntimeException("Unknown factor type " + factor.getRef());
	}
	
	private static String getFactorLabel(ObjectManager manager, int objectType)
	{
		final String internalObjectTypeName = manager.getInternalObjectTypeName(objectType);
		
		return getFieldLabel(objectType, internalObjectTypeName);
	}

	private static String getFieldLabel(int objectType, String internalObjectTypeName)
	{
		return EAM.fieldLabel(objectType, internalObjectTypeName);
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
		
		if (factor.isHumanWelfareTarget())
			return new HumanWelfareTargetIcon();
		
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
		
		if (factor.isScopeBox())
			return new ScopeBoxIcon();
		
		if (factor.isGroupBox())
			return new GroupBoxIcon();
		
		throw new RuntimeException("Unknown factor type: " + factor.getRef());
	}
	
	@Override
	public abstract String toString();

}
