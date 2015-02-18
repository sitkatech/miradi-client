/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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

package org.miradi.views.umbrella;

import org.miradi.main.MainWindow;
import org.miradi.project.Project;
import org.miradi.utils.GenericMiradiFileFilter;
import org.miradi.utils.ProgressInterface;
import org.miradi.utils.Xmpz2FileFilter;
import org.miradi.xml.AbstractXmlImporter;
import org.miradi.xml.AbstractXmpzProjectImporter;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;

public class Xmpz2ProjectImporter extends AbstractXmpzProjectImporter
{
	public Xmpz2ProjectImporter(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
	}

	@Override
	protected AbstractXmlImporter createXmpzXmlImporter(Project projectToFill,	ProgressInterface progressIndicator) throws Exception
	{
		return new Xmpz2XmlImporter(projectToFill, progressIndicator);
	}

	@Override
	protected GenericMiradiFileFilter createFileFilter()
	{
		return new Xmpz2FileFilter();
	}
}
