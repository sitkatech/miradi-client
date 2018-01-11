/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

package org.miradi.questions;

import org.miradi.icons.HumanWelfareTargetIcon;
import org.miradi.icons.TargetIcon;
import org.miradi.main.EAM;

public class TargetModeQuestion extends StaticChoiceQuestion
{
	public TargetModeQuestion()
	{
		super(getChoiceItems());
	}

	public static ChoiceItem[] getChoiceItems()
	{
        // Note per MRD-5892 the decision was made to default to including HWBs for all new projects via project metadata set-up (see createProjectMetadata)
        // The original logical default value is retained here to handle migration of older projects whose default behavior should not change
		return new ChoiceItem[] {
				new ChoiceItem(DEFAULT_CODE, EAM.text("Biodiversity Targets Only"), new TargetIcon()),
				new ChoiceItem(HUMAN_WELFARE_TARGET_CODE, EAM.text("Include Human Wellbeing Targets (default)"), new HumanWelfareTargetIcon()),
		};
	}
	
	@Override
	protected boolean hasReadableAlternativeDefaultCode()
	{
		return true;
	}
	
	@Override
	protected String getReadableAlternativeDefaultCode()
	{
		return READABLE_DEFAULT_CODE;
	}
	
	@Override
	public String convertToReadableCode(String code)
	{
		if (code.equals(DEFAULT_CODE))
			return READABLE_DEFAULT_CODE;
		
		if (code.equals(HUMAN_WELFARE_TARGET_CODE))
			return READABLE_HUMAN_WELFARE_CODE;
		
		return getReadableAlternativeDefaultCode();
	}
	
	@Override
	public String convertToInternalCode(String code)
	{
		if (code.equals(READABLE_DEFAULT_CODE))
			return DEFAULT_CODE;
		
		if (code.equals(READABLE_HUMAN_WELFARE_CODE))
			return HUMAN_WELFARE_TARGET_CODE;
		
		return code;
	}

	
	public static final String DEFAULT_CODE = "";
	public static final String HUMAN_WELFARE_TARGET_CODE = "HumanWelfareTargetMode";
	
	public static final String READABLE_DEFAULT_CODE = "BiologicalTargetsOnly";
	public static final String READABLE_HUMAN_WELFARE_CODE = "BiologicalAndHumanWellbeingTargets";
}
