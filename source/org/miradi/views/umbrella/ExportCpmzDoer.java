/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.zip.ZipOutputStream;

import org.miradi.exceptions.InvalidICUNSelectionException;
import org.miradi.exceptions.ValidationException;
import org.miradi.main.EAM;
import org.miradi.project.ProjectMpzWriter;
import org.miradi.utils.CpmzFileChooser;
import org.miradi.utils.MpzFileFilterForChooserDialog;
import org.miradi.xml.XmlExporter;
import org.miradi.xml.conpro.exporter.ConProMiradiXmlValidator;
import org.miradi.xml.conpro.exporter.ConproXmlExporter;

public class ExportCpmzDoer extends XmlExporterDoer
{
	@Override
	public boolean isAvailable()
	{
		if(!super.isAvailable())
			return false;
		
		return (getProject().getDatabase().isLocalProject());
	}
	
	@Override
	protected void export(File chosen) throws Exception
	{
		createCpmzFile(chosen);
	}

	@Override
	protected CpmzFileChooser createFileChooser()
	{
		return new CpmzFileChooser(getMainWindow());
	}

	private void createCpmzFile(File chosen) throws Exception
	{
		ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(chosen));
		try
		{
			addProjectAsXmlToZip(zipOut);			
			addProjectAsMpzToZip(zipOut);
			addDiagramImagesToZip(zipOut);
		}
		catch(ValidationException e)
		{
			EAM.logException(e);
			EAM.errorDialog(e.getMessage());
		}
		catch (InvalidICUNSelectionException e)
		{
			String errorMessage = EAM.substitute(EAM.text("Please choose a specific IUCN classification (Not a category). Fix needed for Strategy named:\n'%s'"), e.getStrategy().toString());
			EAM.logException(e);
			EAM.errorDialog(errorMessage);
		}
		catch(Exception e)
		{
			// NOTE: If we created no zip entries, the finally will throw
			// a generic exception instead of this one, so log this one
			// while we have a chance
			EAM.logException(e);
			throw(e);
		}
		finally
		{
			zipOut.close();
		}
	}

	private void addProjectAsMpzToZip(ZipOutputStream zipOut) throws Exception
	{
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ZipOutputStream out = new ZipOutputStream(byteOut);
		try
		{
			ProjectMpzWriter.writeProjectZip(out, getProject());
		}
		finally
		{
			out.close();
		}
		
		writeContent(zipOut, PROJECT_ZIP_FILE_NAME, byteOut.toByteArray());
	}

	@Override
	protected XmlExporter createExporter() throws Exception
	{
		return new ConproXmlExporter(getProject());
	}
	
	@Override
	protected boolean isValidXml(ByteArrayInputStream inputStream) throws Exception
	{
		return new ConProMiradiXmlValidator().isValid(inputStream);
	}
	
	@Override
	protected boolean doesConfirmBetaExport() throws Exception
	{
		return true;
	}
	
	public static final String PROJECT_ZIP_FILE_NAME = "project" + MpzFileFilterForChooserDialog.EXTENSION;
}
