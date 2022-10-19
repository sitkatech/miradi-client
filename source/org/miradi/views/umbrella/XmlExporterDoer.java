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
package org.miradi.views.umbrella;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.martus.util.inputstreamwithseek.ByteArrayInputStreamWithSeek;
import org.martus.util.inputstreamwithseek.InputStreamWithSeek;
import org.miradi.exceptions.XmlValidationException;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramObject;
import org.miradi.project.Project;
import org.miradi.utils.BufferedImageFactory;
import org.miradi.utils.MiradiFileSaveChooser;
import org.miradi.utils.ProgressInterface;
import org.miradi.utils.UnicodeXmlWriter;
import org.miradi.xml.XmlExporter;

abstract public class XmlExporterDoer extends AbstractFileSaverDoer
{
	@Override
	protected boolean doWork(Project project, File destinationFile, ProgressInterface progressInterface) throws Exception
	{
		return export(project, destinationFile, progressInterface);
	}

	@Override
	protected String getIOExceptionErrorMessage()
	{
		return EAM.text("Unable to write XML. Perhaps the disk was full, or you " +
				"don't have permission to write to it, or you are using invalid characters in the file name.");
	}
	
	protected void addProjectAsXmlToZip(Project project, ZipOutputStream zipOut) throws Exception
	{
		byte[] projectXmlInBytes = exportProjectXmlToBytes(project);
		ByteArrayInputStreamWithSeek inputStream = new ByteArrayInputStreamWithSeek(projectXmlInBytes);
		try
		{
			validateXml(inputStream);
			createZipEntry(zipOut, PROJECT_XML_FILE_NAME, projectXmlInBytes);
		}
		catch (XmlValidationException e)
		{
			EAM.logDebug(new String(projectXmlInBytes, "UTF-8"));
			throw e;
		}
		finally
		{
			inputStream.close();
		}
	}

	private byte[] exportProjectXmlToBytes(Project project) throws IOException, Exception,	UnsupportedEncodingException
	{
		UnicodeXmlWriter writer = UnicodeXmlWriter.create();
		try
		{
			createExporter(project).exportProject(writer);
		}
		finally
		{
			writer.close();
		}
		
		return writer.toString().getBytes("UTF-8");
	}
	
	protected void addDiagramImagesToZip(Project project, ZipOutputStream zipOut) throws Exception
	{		
		ORefList allDiagramObjectRefs = project.getAllDiagramObjectRefs();
		for (int refIndex = 0; refIndex < allDiagramObjectRefs.size(); ++refIndex)
		{
			DiagramObject diagramObject = (DiagramObject) project.findObject(allDiagramObjectRefs.get(refIndex));
			ORef diagramRef = diagramObject.getRef();
			String imageName = createImageFileName(refIndex, diagramRef);
			writeDiagramImage(zipOut, diagramObject, imageName);
		}
	}

	protected String createImageFileName(int index, ORef diagramRef)
	{
		return BufferedImageFactory.createImageFileName(index, diagramRef);
	}

	protected String getDiagramPrefix(ORef diagramObjectRef)
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
			// TODO: MS-2446 - need variant that takes current project...
			final BufferedImage diagramImage = BufferedImageFactory.createImageFromDiagram(getMainWindow(), diagramObject);
			new SaveImagePngDoer().saveImage(byteOut, diagramImage);
			createZipEntry(zipOut, IMAGES_DIR_NAME_IN_ZIP + imageName, byteOut.toByteArray());
		}
		finally
		{
			byteOut.close();
		}
	}
	
	protected void createZipEntry(ZipOutputStream out, String entryName, String contents) throws Exception
	{
		createZipEntry(out, entryName, contents.getBytes("UTF-8"));

	}
	
	protected void createZipEntry(ZipOutputStream zipOut, String entryName, byte[] bytes) throws FileNotFoundException, IOException
	{
		ZipEntry entry = new ZipEntry(entryName);
		entry.setSize(bytes.length);
		zipOut.putNextEntry(entry);	
		zipOut.write(bytes);
		zipOut.closeEntry();
	}
	
	@Override
	protected String getProgressTitle()
	{
		return EAM.text("Export...");
	}
	
	abstract protected XmlExporter createExporter(Project project) throws Exception;

	abstract protected void validateXml(InputStreamWithSeek inputStream) throws Exception;

	@Override
	abstract protected MiradiFileSaveChooser createFileChooser();
	
	abstract public boolean export(Project project, File chosen, ProgressInterface progressInterface) throws Exception;
	
	public static final String CM_IMAGE_PREFIX = "CM";
	public static final String RC_IMAGE_PREFIX = "RC";
	public static final String IMAGES_DIR_NAME_IN_ZIP = "images/";
	public static final String PROJECT_XML_FILE_NAME = "project.xml";
}
