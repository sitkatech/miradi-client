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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.martus.util.UnicodeStringWriter;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.exceptions.ValidationException;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramObject;
import org.miradi.project.ProjectZipper;
import org.miradi.utils.BufferedImageFactory;
import org.miradi.utils.CpmzFileChooser;
import org.miradi.utils.MpzFileFilterForChooserDialog;
import org.miradi.utils.PNGFileFilter;
import org.miradi.xml.conpro.exporter.ConProMiradiXmlValidator;
import org.miradi.xml.conpro.exporter.ConProMiradiXmlValidatorVersion2;
import org.miradi.xml.conpro.exporter.ConproXmlExporter;
import org.miradi.xml.conpro.exporter.ConproXmlExporterVersion2;

public class ExportCpmzDoer extends AbstractFileSaverDoer
{
	@Override
	public boolean isAvailable()
	{
		return (getProject().isOpen());
	}

	@Override
	public void doIt() throws CommandFailedException
	{
		if (!MainWindow.ALLOW_CONPRO_IMPORT_EXPORT)
		{
			EAM.notifyDialog(MainWindow.DISABLED_CONPRO_IMPORT_EXPORT_MESSAGE);
			return;
		}
		
		super.doIt();
	}
	
	protected void doWork(File chosen) throws Exception
	{
		createCpmzFile(chosen);
	}

	protected CpmzFileChooser getFileChooser()
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

	private void addDiagramImagesToZip(ZipOutputStream zipOut) throws Exception
	{		
		ORefList allDiagramObjectRefs = getProject().getAllDiagramObjectRefs();
		for (int refIndex = 0; refIndex < allDiagramObjectRefs.size(); ++refIndex)
		{
			DiagramObject diagramObject = (DiagramObject) getProject().findObject(allDiagramObjectRefs.get(refIndex));
			String imageName = getDiagramPrefix(diagramObject.getRef()) + refIndex + PNGFileFilter.EXTENSION;
			writeDiagramImage(zipOut, diagramObject, imageName);
		}
	}

	private String getDiagramPrefix(ORef diagramObjectRef)
	{
		if (ConceptualModelDiagram.is(diagramObjectRef))
			return CM_IMAGE_PREFIX;
		
		return RC_IMAGE_PREFIX;
	}

	private void writeDiagramImage(ZipOutputStream zipOut, DiagramObject diagramObject, String imageName) throws Exception
	{
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		try
		{
			new SaveImagePngDoer().saveImage(byteOut, BufferedImageFactory.createImageFromDiagram(getMainWindow(), diagramObject));
			writeContent(zipOut, IMAGES_DIR_NAME_IN_ZIP + imageName, byteOut.toByteArray());
		}
		finally
		{
			byteOut.close();
		}
	}

	private void addProjectAsMpzToZip(ZipOutputStream zipOut) throws Exception
	{
		File projectDir = getProject().getDatabase().getCurrentLocalProjectDirectory();
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ZipOutputStream out = new ZipOutputStream(byteOut);
		try
		{
			ProjectZipper.addTreeToZip(out, projectDir.getName(), projectDir);
		}
		finally
		{
			out.close();
		}
		
		writeContent(zipOut, PROJECT_ZIP_FILE_NAME, byteOut.toByteArray());
	}

	private void addProjectAsXmlToZip(ZipOutputStream zipOut) throws Exception
	{
		exportUsingOriginalExporter(zipOut);
		exportUsingVersion2Exporter(zipOut);
	}
	
	private void exportUsingVersion2Exporter(ZipOutputStream zipOut) throws Exception
	{
		byte[] projectXmlInBytes = exportVersion2ProjectXmlToBytes();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(projectXmlInBytes);
		try
		{
			if (!new ConProMiradiXmlValidatorVersion2().isValid(inputStream))
			{
				EAM.logDebug(new String(projectXmlInBytes, "UTF-8"));
				throw new ValidationException(EAM.text("Exported file does not validate."));
			}

			writeContent(zipOut, "projectVersion2.xml", projectXmlInBytes);
		}
		finally
		{
			inputStream.close();
		}
	}

	private void exportUsingOriginalExporter(ZipOutputStream zipOut) throws Exception
	{
		byte[] projectXmlInBytes = exportProjectXmlToBytes();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(projectXmlInBytes);
		try
		{
			if (!new ConProMiradiXmlValidator().isValid(inputStream))
			{
				EAM.logDebug(new String(projectXmlInBytes, "UTF-8"));
				throw new ValidationException(EAM.text("Exported file does not validate."));
			}

			writeContent(zipOut, "project.xml", projectXmlInBytes);
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
			new ConproXmlExporter(getProject()).exportProject(writer);
			writer.close();
		}
		finally
		{
			writer.close();
		}
		
		return writer.toString().getBytes("UTF-8");
	}
	
	private byte[] exportVersion2ProjectXmlToBytes() throws IOException, Exception, UnsupportedEncodingException
	{
		UnicodeStringWriter writer = UnicodeStringWriter.create();
		try
		{
			new ConproXmlExporterVersion2(getProject()).exportProject(writer);
			writer.close();
		}
		finally
		{
			writer.close();
		}
		
		return writer.toString().getBytes("UTF-8");
	}

	private void writeContent(ZipOutputStream out, String projectXmlName, byte[] bytes) throws FileNotFoundException, IOException
	{
		ZipEntry entry = new ZipEntry(projectXmlName);
		entry.setSize(bytes.length);
		out.putNextEntry(entry);	
		out.write(bytes);
	}

	public static final String PROJECT_XML_FILE_NAME = "project.xml";
	public static final String IMAGES_DIR_NAME_IN_ZIP = "images/";
	public static final String PROJECT_ZIP_FILE_NAME = "project" + MpzFileFilterForChooserDialog.EXTENSION;
	public static final String CM_IMAGE_PREFIX = "CM";
	public static final String RC_IMAGE_PREFIX = "RC";
}
