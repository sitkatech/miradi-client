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

package org.miradi.dialogs.notify;

import org.miradi.main.EAM;

public class NotifyDialogTemplate
{
	public NotifyDialogTemplate(String dialogCodeToUse, String titleToUse, String notificationTextToUse)
	{
		dialogCode = dialogCodeToUse;
		title = titleToUse;
		notificationText = notificationTextToUse;
	}
	
	public String getNotificationText()
	{
		return notificationText;
	}

	public String getTitle()
	{
		return title;
	}

	public String getDialogCode()
	{
		return dialogCode;
	}

	public String[] getButtonLabels()
	{
		return new String[] {CLOSE_TEXT};
	}

	private final String CLOSE_TEXT = EAM.text("Button|Close");
	
	private String dialogCode;
	private String title;
	private String notificationText;
}
