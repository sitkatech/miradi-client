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

import org.miradi.main.EAM;


public class DiagramFactorFontStyleQuestion extends StaticChoiceQuestion
{
	public DiagramFactorFontStyleQuestion()
	{
		super(getStyleChoices());
	}
	
	static ChoiceItem[] getStyleChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem(PLAIN_CODE, EAM.text("Plain (Default)")),
			new ChoiceItem(BOLD_CODE, EAM.text("Bold")),
			new ChoiceItem(UNDERLINE_CODE, EAM.text("Underline")),
			new ChoiceItem(STRIKE_THROUGH_CODE, EAM.text("Strike through")),
		};
	}
	
	@Override
	protected boolean hasReadableAlternativeDefaultCode()
	{
		return true;
	}
	
	@Override
	public String convertToReadableCode(String code)
	{
		if (code.equals(PLAIN_CODE))
			return PLAIN_CODE;
		
		if (code.equals(BOLD_CODE))
			return HUMAN_READABLE_BOLD_CODE;
		
		if (code.equals(UNDERLINE_CODE))
			return HUMAN_READABLE_UNDERLINE_CODE;
		
		if (code.equals(STRIKE_THROUGH_CODE))
			return HUMAN_READABLE_STRIKE_THROUGH_CODE;
		
		return super.convertToReadableCode(code);
	}
	
	@Override
	public String convertToInternalCode(String code)
	{
		if (code.equals(PLAIN_CODE))
			return PLAIN_CODE;
		
		if (code.equals(HUMAN_READABLE_BOLD_CODE))
			return BOLD_CODE;
		
		if (code.equals(HUMAN_READABLE_UNDERLINE_CODE))
			return UNDERLINE_CODE;
		
		if (code.equals(HUMAN_READABLE_STRIKE_THROUGH_CODE))
			return STRIKE_THROUGH_CODE;

		return super.convertToInternalCode(code);
	}

	public static String convertToCssStyle(String code)
	{
		if (code.equals(BOLD_CODE))
			return "font-weight: bold;";

		if (code.equals(UNDERLINE_CODE))
			return "text-decoration: underline;";

		if (code.equals(STRIKE_THROUGH_CODE))
			return "text-decoration: line-through;";

		return "";
	}

	public static final String PLAIN_CODE = "";
	public static final String BOLD_CODE = "<B>";
	public static final String UNDERLINE_CODE = "<U>";
	public static final String STRIKE_THROUGH_CODE = "<S>";
	
	public static final String HUMAN_READABLE_BOLD_CODE = "Bold";
	public static final String HUMAN_READABLE_UNDERLINE_CODE = "Underline";
	public static final String HUMAN_READABLE_STRIKE_THROUGH_CODE = "StrikeThrough";

}
