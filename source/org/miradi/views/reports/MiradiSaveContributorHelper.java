/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.views.reports;

import net.sf.jasperreports.engine.JRException;

import org.miradi.main.EAM;
import org.miradi.resources.ResourcesHandler;

public class MiradiSaveContributorHelper
{
	public static boolean askUserForConfirmation(String title, String resourceFilename) throws JRException
	{
		try
		{
			String bodyText = EAM.loadResourceFile(ResourcesHandler.class, resourceFilename);
			String SAVE_LABEL = EAM.text("Save");
			String[] buttonLabels = new String[] {
				SAVE_LABEL, 
				EAM.text("Cancel")
			};
			int result = EAM.confirmDialog(title, bodyText, buttonLabels);
			if(result < 0)
				return false;
			return (buttonLabels[result].equals(SAVE_LABEL));
		}
		catch(Exception e)
		{
			EAM.logException(e);
			throw new JRException(e);
		}
	}

}
