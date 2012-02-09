/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

import java.io.File;

import org.martus.util.UnicodeWriter;
import org.miradi.objects.XslTemplate;
import org.miradi.utils.GenericFileSaveChooser;
import org.miradi.utils.XmlUtilities2;
import org.miradi.views.ObjectsDoer;

public class ExportXslTemplateDoer extends ObjectsDoer
{
	@Override
	public boolean isAvailable()
	{
		if(!super.isAvailable())
			return false;
		
		return getPicker().getSelectedHierarchies().length > 0;
	}
	
	@Override
	protected void doIt() throws Exception
	{
		if (!isAvailable())
			return;
		
		final GenericFileSaveChooser fileChooser = new GenericFileSaveChooser(getMainWindow());
		final File userChosenFile = fileChooser.displayChooser();
		if (userChosenFile == null)
			return;
		
		String xsl = getProject().getObjectData(getSelectedRef(), XslTemplate.TAG_XSL_TEMPLATE);
		UnicodeWriter writer = new UnicodeWriter(userChosenFile);
		try
		{
			xsl = XmlUtilities2.getXmlDecoded(xsl);
			writer.write(xsl);
		}
		finally
		{
			writer.flush();
			writer.close();
		}
	}
}
