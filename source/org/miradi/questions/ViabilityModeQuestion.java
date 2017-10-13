/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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

import org.miradi.icons.IconManager;
import org.miradi.icons.KeyEcologicalAttributeIcon;
import org.miradi.main.EAM;

public class ViabilityModeQuestion extends StaticChoiceQuestion
{
	public ViabilityModeQuestion()
	{
		super(getViabilityModeChoices());
	}

	static ChoiceItem[] getViabilityModeChoices()
	{
		return new ChoiceItem[] {
				new ChoiceItem("", EAM.text("Simple"), IconManager.getIndicatorIcon()),
				new ChoiceItem(TNC_STYLE_CODE, EAM.text("Key Attribute"), new KeyEcologicalAttributeIcon()),
		};
	}
	
	@Override
	public String convertToReadableCode(String code)
	{
		if (code.equals(SIMPLE_MODE_CODE))
			return READABLE_SIMPLE_CODE; 

		if (code.equals(TNC_STYLE_CODE))
			return READABLE_KEA_CODE;

		return super.convertToReadableCode(code);
	}
	
	@Override
	public String convertToInternalCode(String code)
	{
		if (code.equals(READABLE_SIMPLE_CODE))
			return SIMPLE_MODE_CODE;
		
		if (code.equals(READABLE_KEA_CODE))
			return TNC_STYLE_CODE;
		
		return code;
	}
	
	public static final String READABLE_SIMPLE_CODE = "Simple";
	public static final String READABLE_KEA_CODE = "KEA";
	
	public static String TNC_STYLE_CODE = "TNC";
	public static String SIMPLE_MODE_CODE = "";
}
