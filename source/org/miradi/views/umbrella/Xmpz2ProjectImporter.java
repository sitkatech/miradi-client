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

package org.miradi.views.umbrella;

import java.util.zip.ZipFile;

import javax.swing.filechooser.FileFilter;

import org.martus.util.inputstreamwithseek.InputStreamWithSeek;
import org.miradi.main.MainWindow;
import org.miradi.project.Project;
import org.miradi.utils.ProgressInterface;
import org.miradi.utils.Xmpz2FileFilter;
import org.miradi.xml.AbstractXmpzProjectImporter;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;

public class Xmpz2ProjectImporter extends AbstractXmpzProjectImporter
{
	public Xmpz2ProjectImporter(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
	}

	@Override
	protected void importProjectXml(Project projectToFill, ZipFile zipFile, InputStreamWithSeek projectAsInputStream, ProgressInterface progressIndicator) throws Exception
	{
		Xmpz2XmlImporter xmpzImporter = new Xmpz2XmlImporter(projectToFill, progressIndicator);
		xmpzImporter.importProject(projectAsInputStream);
	}

	@Override
	public FileFilter[] getFileFilters()
	{
		return new FileFilter[] {createFileFilter()};
	}

	protected Xmpz2FileFilter createFileFilter()
	{
		return new Xmpz2FileFilter();
	}
}
