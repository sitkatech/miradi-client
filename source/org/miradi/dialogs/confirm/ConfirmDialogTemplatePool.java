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

public class ConfirmDialogTemplatePool
{
	public static ConfirmDialogTemplate shouldPasteSharedFactors()
	{
		String title = EAM.text("Confirm Paste Shared Factors");
		String yesText = EAM.text("Button|Paste Shared");
		String confirmationTextToUse = "<html><div class='WizardText'>" + EAM.text(
				"This will paste shared copies of the factors, " +
				"meaning that any changes made in this diagram will also be reflected " +
				"in other diagrams that these factors appear. " +
				"In other words, each factor will only exist once in this project, " +
				"but will be visible in multiple diagram pages. " +
				"Normally, this is what you want.<br><br>" +
				"If you really wanted to create duplicate copies of the factors, " +
				"separate from the originals, " +
				"you must cancel this operation, " +
				"return to the diagram where these factors originated, " +
				"and paste them there. " +
				"Then use the &lt;Cut&gt; command to move them to the clipboard, " +
				"return to this diagram, and paste the new copies here.");
		return new ConfirmDialogTemplate(title, confirmationTextToUse, yesText);
	}
}
