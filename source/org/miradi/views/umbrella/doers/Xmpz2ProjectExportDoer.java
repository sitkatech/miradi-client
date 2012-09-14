/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.utils.MiradiFileSaveChooser;
import org.miradi.utils.Xmpz2ZipFileChooser;
import org.miradi.xml.XmlExporter;
import org.miradi.xml.wcs.Xmpz2XmlValidator;
import org.miradi.xml.xmpz2.Xmpz2XmlExporter;

public class Xmpz2ProjectExportDoer extends AbstractExportProjectXmlZipDoer
{
	@Override
	protected MiradiFileSaveChooser createFileChooser()
	{
		return new Xmpz2ZipFileChooser(getMainWindow());
	}

	@Override
	protected XmlExporter createExporter() throws Exception
	{
		return new Xmpz2XmlExporter(getProject());
	}
	
	@Override
	protected boolean isValidXml(ByteArrayInputStream inputStream) throws Exception
	{
		return new Xmpz2XmlValidator().isValid(inputStream);
	}
}
