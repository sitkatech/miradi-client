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

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.martus.util.UnicodeStringReader;
import org.martus.util.UnicodeStringWriter;
import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.objects.XslTemplate;
import org.miradi.utils.FileSaveChooserWithUserDefinedFileFilter;
import org.miradi.utils.XmlUtilities2;
import org.miradi.views.ObjectsDoer;
import org.miradi.xml.wcs.XmpzXmlExporter;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class RunXlsTemplateDoer extends ObjectsDoer
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
		String xlsTemplate = selectedObject.getData(XslTemplate.TAG_XSL_TEMPLATE);
		xlsTemplate = XmlUtilities2.getXmlDecoded(xlsTemplate);
		
		final String extension = selectedObject.getData(XslTemplate.TAG_FILE_EXTENSION);
		
		transform(xlsTemplate, extension);
	}
	
	private void transform(final String xslTemplate, final String extensionToUse) throws Exception 
	{
		final InputSource projectXmlInputSource = getExportedProjectXmlAsString(); 

		DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
		dfactory.setNamespaceAware(true);
		Document doc = dfactory.newDocumentBuilder().parse(projectXmlInputSource);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		final StreamSource xslStreamSource = new StreamSource(new UnicodeStringReader(xslTemplate));
		Transformer transformer = transformerFactory.newTransformer(xslStreamSource);
		
		FileSaveChooserWithUserDefinedFileFilter fileChooser = new FileSaveChooserWithUserDefinedFileFilter(getMainWindow(), extensionToUse);
		final File outputFile = fileChooser.displayChooser();
		if (outputFile != null)
		{				
			transformer.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(outputFile)));
			EAM.notifyDialog(EAM.text("Transformation Completed!"));
		}
	}
	
	private InputSource getExportedProjectXmlAsString() throws Exception
	{
		UnicodeStringWriter projectWriter = UnicodeStringWriter.create();
		try
		{
			new XmpzXmlExporter(getProject()).exportProject(projectWriter);
			projectWriter.flush();
			final String projectXmlAsString = projectWriter.toString();
			UnicodeStringReader reader = new UnicodeStringReader(projectXmlAsString);
			
			return new InputSource(reader);
		}
		finally
		{
			projectWriter.close();
		}
	}
}
