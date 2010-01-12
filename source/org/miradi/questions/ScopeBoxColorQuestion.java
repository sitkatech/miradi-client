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
public class ScopeBoxColorQuestion extends DynamicChoiceQuestion
{
	public ScopeBoxColorQuestion()
	{
		this(EAM.getMainWindow().getAppPreferences());
	}
	
	public ScopeBoxColorQuestion(AppPreferences appPreferencesToUse)
	{
		super();
		
		appPreferences = appPreferencesToUse;
	}

	@Override
	public ChoiceItem[] getChoices()
	{
		return new ChoiceItem[] {
				new ChoiceItemWithDynamicColor("", EAM.text("Green (Biodiversity Target)"), getAppPreferences(), AppPreferences.TAG_COLOR_SCOPE_BOX),
				new ChoiceItemWithDynamicColor("HumanWelfareTargetBrown", EAM.text("Brown (Human Welfare Target)"), getAppPreferences(), AppPreferences.TAG_COLOR_HUMAN_WELFARE_SCOPE_BOX),
			};
	}

	private AppPreferences getAppPreferences()
	{
		return appPreferences;
	}
	
	private AppPreferences appPreferences;
}
