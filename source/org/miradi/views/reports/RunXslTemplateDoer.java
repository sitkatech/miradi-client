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

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.martus.util.UnicodeStringReader;
import org.martus.util.UnicodeStringWriter;
import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.objects.XslTemplate;
import org.miradi.utils.FileSaveChooserWithUserDefinedFileFilter;
import org.miradi.utils.HtmlUtilities;
import org.miradi.views.ObjectsDoer;
import org.miradi.xml.wcs.XmpzXmlExporter;

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
		xlsTemplate = HtmlUtilities.convertHtmlToPlainText(xlsTemplate);
		
		final String extension = selectedObject.getData(XslTemplate.TAG_FILE_EXTENSION);
		
		transform(xlsTemplate, extension);
	}
	
	private void transform(final String xslTemplate, final String extensionToUse) throws Exception 
	{
		final StreamSource projectXmlInputSource = getExportedProjectXmlAsString(); 
		final StreamSource xslStreamSource = new StreamSource(new UnicodeStringReader(xslTemplate));
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer(xslStreamSource);
		FileSaveChooserWithUserDefinedFileFilter fileChooser = new FileSaveChooserWithUserDefinedFileFilter(getMainWindow(), extensionToUse);
		final File outputFile = fileChooser.displayChooser();
		if (outputFile != null)
		{
			final StreamResult output = new StreamResult(new FileOutputStream(outputFile));
			transformer.transform(projectXmlInputSource, output);
			EAM.notifyDialog(EAM.text("Report Completed"));
		}
	}
	
	private StreamSource getExportedProjectXmlAsString() throws Exception
	{
		UnicodeStringWriter projectWriter = UnicodeStringWriter.create();
		try
		{
			new XmpzXmlExporter(getProject()).exportProject(projectWriter);
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
