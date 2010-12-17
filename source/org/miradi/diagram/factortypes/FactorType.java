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
import org.miradi.objects.Factor;



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
		return EAM.fieldLabel(factor.getType(), factor.getTypeName());
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
