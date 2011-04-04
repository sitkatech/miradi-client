/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.icons.IconManager;
import org.miradi.main.EAM;

public class OpenStandardsDynamicProgressStatusQuestion extends StaticChoiceQuestion
{
	@Override
	public ChoiceItem[] createChoices()
	{
		return new ChoiceItem[] {
					new ChoiceItem(NOT_SPECIFIED_CODE, EAM.text("Automatic"), IconManager.getOpenStandardsAutomaticIcon()),
					new ChoiceItem(NOT_STARTED_CODE, EAM.text("Not Started"), IconManager.getHyphenIcon()),
					new ChoiceItem(IN_PROGRESS_CODE, EAM.text("In Progress"), IconManager.getOpenStandardsInProgressIcon()),
					new ChoiceItem(COMPLETE_CODE, EAM.text("Complete"), IconManager.getOpenStandardsCompleteIcon()),
					new ChoiceItem(NOT_APPLICABLE_CODE, EAM.text("Not Applicable"), IconManager.getOpenStandardsNotApplicableIcon()),
			};
	}

	public static final String NOT_SPECIFIED_CODE = "";
	public static final String NOT_STARTED_CODE = "1";
	public static final String IN_PROGRESS_CODE = "2";
	public static final String COMPLETE_CODE = "3";
	public static final String NOT_APPLICABLE_CODE = "4";
}
