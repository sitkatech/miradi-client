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

package org.miradi.views.umbrella;

import org.miradi.main.MainWindow;
import org.miradi.project.Project;
import org.miradi.utils.GenericMiradiFileFilter;
import org.miradi.utils.ProgressInterface;
import org.miradi.utils.XmpzFileFilter;
import org.miradi.xml.AbstractXmlImporter;
import org.miradi.xml.AbstractXmpzProjectImporter;
import org.miradi.xml.xmpz1.Xmpz1XmlImporter;

public class XmpzProjectImporter extends AbstractXmpzProjectImporter
{
	public XmpzProjectImporter(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
	}
	
	@Override
	protected AbstractXmlImporter createXmpzXmlImporter(Project projectToFill,	ProgressInterface progressIndicator) throws Exception
	{
		return new Xmpz1XmlImporter(projectToFill, progressIndicator);
	}

	@Override
	protected GenericMiradiFileFilter createFileFilter()
	{
		return new XmpzFileFilter();
	}
}
