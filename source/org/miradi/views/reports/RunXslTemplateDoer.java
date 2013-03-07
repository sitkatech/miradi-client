/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.views.reports;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Set;

import javax.imageio.stream.FileImageOutputStream;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.martus.util.UnicodeStringReader;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.BaseObject;
import org.miradi.objects.XslTemplate;
import org.miradi.utils.BufferedImageFactory;
import org.miradi.utils.DirectoryChooser;
import org.miradi.utils.FileSaveChooserWithUserDefinedFileFilter;
import org.miradi.utils.FileUtilities;
import org.miradi.utils.HtmlUtilities;
import org.miradi.utils.UnicodeXmlWriter;
import org.miradi.views.ObjectsDoer;
import org.miradi.views.umbrella.SaveImagePngDoer;
import org.miradi.xml.xmpz2.Xmpz2XmlExporter;

public class RunXslTemplateDoer extends ObjectsDoer
{
	@Override
	public boolean isAvailable()
	{
		if(!super.isAvailable())
			return false;
		
		return getSingleSelectedObject() != null;
	}
	
	@Override
	protected void doIt() throws Exception
	{
		if (!isAvailable())
			return;
		
		BaseObject selectedObject = getSingleSelectedObject();
		String xlsTemplate = selectedObject.getData(XslTemplate.TAG_TEMPLATE_CONTENTS);
		xlsTemplate = HtmlUtilities.convertStoredXslToNative(xlsTemplate);
		
		final File outputFile = getOutputFile(selectedObject);
		if (outputFile != null)
			transform(xlsTemplate, outputFile);
	}

	public File getOutputFile(BaseObject selectedObject) throws Exception
	{
		final String extension = selectedObject.getData(XslTemplate.TAG_FILE_EXTENSION);
		final boolean includeImages = selectedObject.getBooleanData(XslTemplate.TAG_INCLUDE_IMAGES);
		if (includeImages)
		{
			File outputDirectory = askUserForOutputDir();
			if (outputDirectory == null)
			{
				EAM.notifyDialog(EAM.text("Please choose an output directory."));
				return null;
			}
			
			createAndFillImagesDir(outputDirectory);
			
			return new File(outputDirectory, FileUtilities.createFileNameWithExtension(outputDirectory.getName(), extension));
		}

		FileSaveChooserWithUserDefinedFileFilter fileChooser = new FileSaveChooserWithUserDefinedFileFilter(getMainWindow(), extension);
		return  fileChooser.displayChooser();
	}

	private void createAndFillImagesDir(File outputDirectory) throws Exception
	{
		File imagesDir = new File(outputDirectory, "images");
		if (!imagesDir.mkdir())
		{
			EAM.errorDialog(EAM.text("Images dir could not be created!"));
			return;
		}
		
		writeImages(getMainWindow(), imagesDir);
	}

	private File askUserForOutputDir() throws Exception
	{
		DirectoryChooser chooser = new DirectoryChooser(getMainWindow());
		File outputDirectory = chooser.displayChooser();
		if (outputDirectory == null)
		{
			return null;
		}
		File[] containingFiles = outputDirectory.listFiles();
		if (containingFiles.length > 0)
		{
			EAM.errorDialog(EAM.text("Please choose an empty directory!"));
			return null;
		}

		return outputDirectory;
	}
	
	private void writeImages(MainWindow mainWindowToUse, File imagesDir) throws Exception
	{
		HashMap<String, BufferedImage> namesToDiagramImagesMap = BufferedImageFactory.createNamesToImagesMap(mainWindowToUse);
		Set<String> imageNames = namesToDiagramImagesMap.keySet();
		for(String imageName : imageNames)
		{
			writeImage(imagesDir, imageName, namesToDiagramImagesMap.get(imageName));
		}
	}

	private void writeImage(File imagesDir, String imageName, BufferedImage bufferedImage) throws Exception
	{
		File imageFile = new File(imagesDir, imageName);
		FileImageOutputStream out = new FileImageOutputStream(imageFile);
		try
		{
			new SaveImagePngDoer().saveImage(out, bufferedImage);
		}
		finally
		{
			out.close();	
		}
	}

	private void transform(final String xslTemplate, final File outputFile) throws Exception 
	{
		final StreamSource projectXmlInputSource = getExportedProjectXmlAsString(); 
		final StreamSource xslStreamSource = new StreamSource(new UnicodeStringReader(xslTemplate));
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer(xslStreamSource);
		final StreamResult output = new StreamResult(new FileOutputStream(outputFile));
		transformer.transform(projectXmlInputSource, output);
		EAM.notifyDialog(EAM.text("Report Completed"));
	}
	
	private StreamSource getExportedProjectXmlAsString() throws Exception
	{
		UnicodeXmlWriter projectWriter = UnicodeXmlWriter.create();
		try
		{
			new Xmpz2XmlExporter(getProject()).exportProject(projectWriter);
			projectWriter.flush();
			final String projectXmlAsString = projectWriter.toString();
			UnicodeStringReader reader = new UnicodeStringReader(projectXmlAsString);
			
			return new StreamSource(reader);
		}
		finally
		{
			projectWriter.close();
		}
	}
}
