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

package org.miradi.views.umbrella.doers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.zip.ZipOutputStream;

import org.martus.util.UnicodeReader;
import org.martus.util.UnicodeStringWriter;
import org.miradi.exceptions.InvalidICUNSelectionException;
import org.miradi.exceptions.ValidationException;
import org.miradi.main.EAM;
import org.miradi.main.ResourcesHandler;
import org.miradi.utils.EAMFileSaveChooser;
import org.miradi.utils.WcsZipFileChooser;
import org.miradi.views.umbrella.XmlExporterDoer;
import org.miradi.xml.wcs.WcsMiradiXmlValidator;
import org.miradi.xml.wcs.WcsXmlExporter;
//FIXME this is still under contruction and also has duplicated code from cpmz exporter
public class ExportWcsProjectZipDoer extends XmlExporterDoer
{
	@Override
	protected void export(File chosen) throws Exception
	{
		ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(chosen));
		try
		{
			addProjectAsXmlToZip(zipOut);
			addSchemaToZip(zipOut);
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
	
	private void addSchemaToZip(ZipOutputStream zipOut) throws Exception
	{
		URL resourceURL = ResourcesHandler.getEnglishResourceURL(WcsMiradiXmlValidator.WCS_MIRADI_SCHEMA_FILE_RELATIVE_PATH);
		if(resourceURL == null)
			throw new Exception("Schema not found: " + WcsMiradiXmlValidator.WCS_MIRADI_SCHEMA_FILE_RELATIVE_PATH);

		writeContent(zipOut, "schema.rnc", readAll(resourceURL));
	}
	
	private byte[] readAll(URL resourceURL) throws IOException
	{
		UnicodeReader reader = new UnicodeReader(resourceURL.openStream());
		try
		{
			String wholeFile = reader.readAll();
			return wholeFile.getBytes("UTF-8");
		}
		finally
		{
			reader.close();
		}
	}

	private void addProjectAsXmlToZip(ZipOutputStream zipOut) throws Exception
	{
		byte[] projectXmlInBytes = exportProjectXmlToBytes();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(projectXmlInBytes);
		try
		{
			if (!new WcsMiradiXmlValidator().isValid(inputStream))
			{
				EAM.logDebug(new String(projectXmlInBytes, "UTF-8"));
				throw new ValidationException(EAM.text("Exported file does not validate."));
			}

			writeContent(zipOut, PROJECT_XML_FILE_NAME, projectXmlInBytes);
		}
		finally
		{
			inputStream.close();
		}
	}

	private byte[] exportProjectXmlToBytes() throws IOException, Exception, UnsupportedEncodingException
	{
		UnicodeStringWriter writer = UnicodeStringWriter.create();
		try
		{
			new WcsXmlExporter(getProject()).exportProject(writer);
		}
		finally
		{
			writer.close();
		}
		
		return writer.toString().getBytes("UTF-8");
	}

	@Override
	protected EAMFileSaveChooser createFileChooser()
	{
		return new WcsZipFileChooser(getMainWindow());
	}
	
	public static final String PROJECT_XML_FILE_NAME = "project.xml";
	public static final String IMAGES_DIR_NAME_IN_ZIP = "images/";
	public static final String CM_IMAGE_PREFIX = "CM";
	public static final String RC_IMAGE_PREFIX = "RC";
}
