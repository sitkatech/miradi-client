/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.main;

import java.util.HashMap;

public class MiradiStrings
{
	public static String getOverwriteLabel()
	{
		return EAM.text("Button|Overwrite");
	}

	public static String getCancelButtonText()
	{
		return EAM.text("Button|Cancel");
	}

	public static String getOpenLabel()
	{
		return EAM.text("Button|Open");
	}

	public static String getOkButtonText()
	{
		return EAM.text("Button|OK");
	}

	public static String getWarningLabel()
	{
		return EAM.text("Warning");
	}

	public static String getUnexpectedErrorMessage()
	{
		return EAM.text("An unexpected error has occurred");
	}

	public static String getErrorMessage()
	{
		return EAM.text("Title|Error");
	}

	public static String getRetainLabel()
	{
		return EAM.text("Retain");
	}

	public static String getDeleteLabel()
	{
		return EAM.text("Delete");
	}

	public static String getInformationDialogTitle()
	{
		return EAM.text("Wintitle|Information");
	}

	public static String getErrorMessage(Exception e)
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%message", e.getMessage());
		tokenReplacementMap.put("%logPath", EAM.getDefaultExceptionsLogFile().getAbsolutePath());
		return EAM.substitute("An unexpected error occurred: %message" +
				"\n\nPlease report this to the Miradi support team, " +
				"ideally including the contents of this file: " +
				"\n\n   %logPath" +  
				"\n\nMiradi has attempted to save your latest changes, and will now exit.", tokenReplacementMap);
	}
}
