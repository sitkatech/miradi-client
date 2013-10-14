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

package org.miradi.dialogs.confirm;

import org.miradi.main.EAM;

public class ConfirmDialogTemplate
{
	public ConfirmDialogTemplate(String titleToUse, String confirmationTextToUse, String yesButtonTextToUse)
	{
		title = titleToUse;
		confirmationText = confirmationTextToUse;
		yesText = yesButtonTextToUse;
		
		noText = CANCEL_TEXT;
	}
	
	public String getConfirmationText()
	{
		return confirmationText;
	}

	public String getTitle()
	{
		return title;
	}

	public String[] getButtonLabels()
	{
		return new String[] {yesText, noText};
	}

	private final String CANCEL_TEXT = EAM.text("Button|Cancel");
	
	private String title;
	private String confirmationText;
	private String yesText;
	private String noText;
}