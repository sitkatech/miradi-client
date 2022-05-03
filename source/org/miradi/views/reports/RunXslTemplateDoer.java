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

package org.miradi.views.reports;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Set;

import javax.imageio.stream.FileImageOutputStream;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.martus.util.UnicodeStringReader;
import org.martus.util.inputstreamwithseek.StringInputStreamWithSeek;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.main.MiradiStrings;
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
import org.miradi.xml.xmpz2.Xmpz2XmlConstants;
import org.miradi.xml.xmpz2.Xmpz2XmlExporter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

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
		
		Document document = createDomDocument(xlsTemplate);
		String xmpz2NameSpaceUrl = findXmpz2SNameSpaceUrl(document);
		String xmpz2SchemaVersion = xmpz2NameSpaceUrl.replaceAll(Xmpz2XmlConstants.PARTIAL_NAME_SPACE, "");

		boolean doesMatchXmpz2SchemaVersion = xmpz2SchemaVersion.equals(Xmpz2XmlConstants.NAME_SPACE_VERSION);
		if (!doesMatchXmpz2SchemaVersion)
		{
			HashMap<String, String> tokenToTextMap = new HashMap<String, String>();
			tokenToTextMap.put("%xmpz2SchemaVersion", Xmpz2XmlConstants.NAME_SPACE_VERSION);
			tokenToTextMap.put("%versionInXsl", xmpz2SchemaVersion);
			
			String message = EAM.substitute(EAM.text("The output from this report might not be correct, because this XSL template \n" +
													 "appears to have been created for use with an earlier version of Miradi. The \n" +
													 "current Miradi schema version is %xmpz2SchemaVersion, but this template \n" +
													 "expects %versionInXsl. Continuing will produce output which may or may \n" +
													 "not be correct. \n" +
													 "Are you sure you want to do this?"), tokenToTextMap);
			
			int result = EAM.confirmDialog(EAM.text("Run XSL Report"), message, new String[]{MiradiStrings.getExportReportLabel(), MiradiStrings.getCancelButtonText()});
			if (result == JOptionPane.NO_OPTION)
				return;
		}
		
		final File outputFile = getOutputFile(selectedObject);
		if (outputFile != null)
			transform(xlsTemplate, outputFile);
	}

	private String findXmpz2SNameSpaceUrl(Document document)
	{
		Element rootElement = document.getDocumentElement();
		NamedNodeMap attributes = rootElement.getAttributes();
	    if (attributes == null)
	    	return "";
	    
	    for (int index = 0; index < attributes.getLength(); index++)
	    {
	    	Node node = attributes.item(index);
	    	if (node.getNodeType() != Node.ATTRIBUTE_NODE)
	    		continue;

	    	final String textContent = node.getTextContent();
	    	if (textContent == null)
	    		continue;

	    	if (textContent.contains(Xmpz2XmlConstants.PARTIAL_NAME_SPACE))
	    		return textContent;
	    }
	    
	    return "";
	}
	
	private Document createDomDocument(String text) throws Exception
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);

		InputSource inputSource = new InputSource(new StringInputStreamWithSeek(text));
		DocumentBuilder documentBuilder = factory.newDocumentBuilder();
		return documentBuilder.parse(inputSource);
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
