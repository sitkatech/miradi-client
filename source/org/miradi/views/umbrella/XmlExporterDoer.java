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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.martus.util.UnicodeStringWriter;
import org.miradi.exceptions.ValidationException;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramObject;
import org.miradi.utils.BufferedImageFactory;
import org.miradi.utils.ConstantButtonNames;
import org.miradi.utils.EAMFileSaveChooser;
import org.miradi.utils.PNGFileFilter;
import org.miradi.utils.ProgressInterface;
import org.miradi.xml.XmlExporter;

abstract public class XmlExporterDoer extends AbstractFileSaverDoer
{
	@Override
	protected boolean doesConfirmBetaExport() throws Exception
	{
		String title = EAM.text("Export Project (BETA)");
		String[] body = new String[] {
			EAM.text("This feature is not yet fully supported. " +
			"It exports all the project data, but the XML schema is still in flux. " +
			"Future versions of Miradi will export the data in different formats."),
		};

		String[] buttons = new String[] {
			EAM.text("Export"),
			ConstantButtonNames.CANCEL,
		};
		
		return EAM.confirmDialog(title, body, buttons);
	}

	@Override
	protected boolean doWork(File destinationFile, ProgressInterface progressInterface) throws Exception
	{
		return export(destinationFile, progressInterface);
	}

	@Override
	protected String getIOExceptionErrorMessage()
	{
		return EAM.text("Unable to write XML. Perhaps the disk was full, or you " +
				"don't have permission to write to it, or you are using invalid characters in the file name.");
	}
	
	protected void addProjectAsXmlToZip(ZipOutputStream zipOut) throws Exception
	{
		byte[] projectXmlInBytes = exportProjectXmlToBytes();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(projectXmlInBytes);
		try
		{
			if (!isValidXml(inputStream))
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

	private byte[] exportProjectXmlToBytes() throws IOException, Exception,	UnsupportedEncodingException
	{
		UnicodeStringWriter writer = UnicodeStringWriter.create();
		try
		{
			createExporter().exportProject(writer);
		}
		finally
		{
			writer.close();
		}
		
		return writer.toString().getBytes("UTF-8");
	}
	
	protected void addDiagramImagesToZip(ZipOutputStream zipOut) throws Exception
	{		
		ORefList allDiagramObjectRefs = getProject().getAllDiagramObjectRefs();
		for (int refIndex = 0; refIndex < allDiagramObjectRefs.size(); ++refIndex)
		{
			DiagramObject diagramObject = (DiagramObject) getProject().findObject(allDiagramObjectRefs.get(refIndex));
			ORef diagramRef = diagramObject.getRef();
			String imageName = createImageFileName(refIndex, diagramRef);
			writeDiagramImage(zipOut, diagramObject, imageName);
		}
	}

	protected String createImageFileName(int index, ORef diagramRef)
	{
		return getDiagramPrefix(diagramRef) + index + PNGFileFilter.EXTENSION;
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
			new SaveImagePngDoer().saveImage(byteOut, BufferedImageFactory.createImageFromDiagram(getMainWindow(), diagramObject));
			writeContent(zipOut, IMAGES_DIR_NAME_IN_ZIP + imageName, byteOut.toByteArray());
		}
		finally
		{
			byteOut.close();
		}
	}
	
	protected void writeContent(ZipOutputStream out, String projectXmlName, byte[] bytes) throws FileNotFoundException, IOException
	{
		ZipEntry entry = new ZipEntry(projectXmlName);
		entry.setSize(bytes.length);
		out.putNextEntry(entry);	
		out.write(bytes);
	}
	
	@Override
	protected String getProgressTitle()
	{
		return EAM.text("Export...");
	}
	
	abstract protected XmlExporter createExporter() throws Exception;
	
	abstract protected boolean isValidXml(ByteArrayInputStream inputStream) throws Exception;

	@Override
	abstract protected EAMFileSaveChooser createFileChooser();
	
	abstract protected boolean export(File chosen, ProgressInterface progressInterface) throws Exception;
	
	public static final String CM_IMAGE_PREFIX = "CM";
	public static final String RC_IMAGE_PREFIX = "RC";
	public static final String IMAGES_DIR_NAME_IN_ZIP = "images/";
	public static final String PROJECT_XML_FILE_NAME = "project.xml";
}
