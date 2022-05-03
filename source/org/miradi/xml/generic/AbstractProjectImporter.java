/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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

package org.miradi.xml.generic;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipException;

import org.martus.util.inputstreamwithseek.InputStreamWithSeek;
import org.miradi.exceptions.XmlValidationException;
import org.miradi.main.MainWindow;
import org.miradi.project.Project;
import org.miradi.utils.MiradiZipFile;
import org.miradi.utils.ProgressInterface;
import org.miradi.views.umbrella.AbstractZippedXmlImporter;
import org.miradi.xml.AbstractXmlImporter;
import org.miradi.xml.xmpz2.Xmpz2MigrationResult;

abstract public class AbstractProjectImporter extends AbstractZippedXmlImporter
{
	public AbstractProjectImporter(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
	}
	
	@Override
	protected ImportXmlProjectResult importProjectXml(Project projectToFill, MiradiZipFile zipFile, InputStreamWithSeek projectAsInputStream, ProgressInterface progressIndicator) throws Exception
	{
		AbstractXmlImporter xmpzImporter = createXmpzXmlImporter(projectToFill, progressIndicator);
		Xmpz2MigrationResult migrationResult = xmpzImporter.importProjectXml(projectAsInputStream);
		return new ImportXmlProjectResult(projectToFill, migrationResult.getSchemaVersionWasUpdated(), migrationResult.getDocumentSchemaVersion());
	}

	@Override
	protected void createOrOpenProject(Project projectToFill, File projectFile) throws Exception
	{
		projectToFill.rawCreatorOpen();
	}
	
	protected ImportXmlProjectResult importProject(File zipFileToImport, ProgressInterface progressIndicator) throws ZipException, IOException, Exception, XmlValidationException
	{
		MiradiZipFile zipFile = new MiradiZipFile(zipFileToImport);
		try
		{
			return importProjectFromXmlEntry(zipFile, progressIndicator);
		}
		finally
		{
			zipFile.close();
		}
	}

	@Override
	protected void possiblyNotifyUserOfAutomaticMigration(File file) throws Exception
	{
	}
	
	abstract protected AbstractXmlImporter createXmpzXmlImporter(Project projectToFill,	ProgressInterface progressIndicator) throws Exception;
}
