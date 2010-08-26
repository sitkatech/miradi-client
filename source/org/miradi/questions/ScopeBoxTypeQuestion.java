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

package org.miradi.questions;

import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;

//TODO: Superclass really should not have color data members
public class ScopeBoxTypeQuestion extends DynamicChoiceQuestion
{
	public static ScopeBoxTypeQuestion createScopeBoxTypeQuestion()
	{
		if (isMainWindowNullDueToTest())
			return new ScopeBoxTypeQuestion(null);
		
		return new ScopeBoxTypeQuestion();
	}

	private static boolean isMainWindowNullDueToTest()
	{
		return EAM.getMainWindow() == null;
	}
	
	private ScopeBoxTypeQuestion()
	{
		this(EAM.getMainWindow().getAppPreferences());
	}
	
	public ScopeBoxTypeQuestion(AppPreferences appPreferencesToUse)
	{
		super();
		
		appPreferences = appPreferencesToUse;
	}

	@Override
	public ChoiceItem[] getChoices()
	{
		return new ChoiceItem[] {
				new ChoiceItemWithDynamicColor(BIODIVERSITY_TARGET_CODE, EAM.text("Biodiversity Target"), getAppPreferences(), AppPreferences.TAG_COLOR_SCOPE_BOX),
				new ChoiceItemWithDynamicColor(HUMAN_WELFARE_TARGET_CODE, EAM.text("Human Welfare Target"), getAppPreferences(), AppPreferences.TAG_COLOR_HUMAN_WELFARE_SCOPE_BOX),
			};
	}

	private AppPreferences getAppPreferences()
	{
		return appPreferences;
	}
	
	@Override
	public boolean hasReadableAlternativeDefaultCode()
	{
		return true;
	}
	
	@Override
	public String getReadableAlternativeDefaultCode()
	{
		return READABLE_BIODIVERSITY_CODE;
	}
	
	@Override
	public String convertToReadableCode(String code)
	{
		if (code.equals(HUMAN_WELFARE_TARGET_CODE))
			return READABLE_HUMAN_WELFARE_CODE;

		return getReadableAlternativeDefaultCode();
	}
	
	@Override
	public String convertToInternalCode(String code)
	{
		if (code.equals(READABLE_HUMAN_WELFARE_CODE))
			return HUMAN_WELFARE_TARGET_CODE;
		
		if (code.equals(READABLE_BIODIVERSITY_CODE))
			return BIODIVERSITY_TARGET_CODE;
		
		return code;
	}
		
	private AppPreferences appPreferences;
	public static final String BIODIVERSITY_TARGET_CODE = "";
	public static final String HUMAN_WELFARE_TARGET_CODE = "HumanWelfareTargetBrown";
	
	public static final String READABLE_BIODIVERSITY_CODE = "Biodiversity";
	public static final String READABLE_HUMAN_WELFARE_CODE = "HumanWelfare";
	
}
