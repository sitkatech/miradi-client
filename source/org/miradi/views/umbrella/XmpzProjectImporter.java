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

import java.util.zip.ZipFile;

import javax.swing.filechooser.FileFilter;

import org.martus.util.inputstreamwithseek.InputStreamWithSeek;
import org.miradi.main.MainWindow;
import org.miradi.project.Project;
import org.miradi.utils.ProgressInterface;
import org.miradi.utils.XmpzFileFilter;
import org.miradi.xml.AbstractXmpzProjectImporter;
import org.miradi.xml.xmpz.XmpzXmlImporter;

public class XmpzProjectImporter extends AbstractXmpzProjectImporter
{
	public XmpzProjectImporter(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
	}
	
	@Override
	protected void importProjectXml(Project projectToFill, ZipFile zipFile, InputStreamWithSeek projectAsInputStream, ProgressInterface progressIndicator) throws Exception
	{
		XmpzXmlImporter xmpzImporter = new XmpzXmlImporter(projectToFill, progressIndicator);
		xmpzImporter.importProject(projectAsInputStream);
	}

	@Override
	public FileFilter[] getFileFilters()
	{
		return new FileFilter[] {new XmpzFileFilter()};
	}
}
