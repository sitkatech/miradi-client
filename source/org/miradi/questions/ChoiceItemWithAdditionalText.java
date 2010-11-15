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

import java.awt.Color;

public class ChoiceItemWithAdditionalText extends ChoiceItem
{
	public ChoiceItemWithAdditionalText(String codeToUse)
	{
		this(codeToUse, "", "");
	}
	
	public ChoiceItemWithAdditionalText(String codeToUse, String additionalTextToUse)
	{
		this(codeToUse, additionalTextToUse, "");
	}
	
	public ChoiceItemWithAdditionalText(String codeToUse, String leftLabelToUse, String additionalTextToUse)
	{
		this(codeToUse, leftLabelToUse, additionalTextToUse, (Color)null);
	}
	
	public ChoiceItemWithAdditionalText(String codeToUse, String leftLabelToUse, String additionalTextToUse, Color colorToUse)
	{
		super(codeToUse, leftLabelToUse, colorToUse);
		
		additionalText = additionalTextToUse;
	}

	public void setRightLabel(String additionalTextToUse)
	{
		additionalText = additionalTextToUse;
	}
	
	public String getAdditionalText()
	{
		return additionalText;
	}

	private String additionalText;
}
