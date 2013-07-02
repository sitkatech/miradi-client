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

import org.miradi.utils.MiradiFileSaveChooser;
import org.miradi.utils.XmpzFileChooser;
import org.miradi.xml.wcs.WcsMiradiXmlValidator;

public class ExportXmpzDoer extends AbstractExportProjectXmlZipDoer
{
	@Override
	public boolean isAvailable()
	{
		//FIXME urgent, due to migration bugs,  MPZ/xmpz1/cpmz import and export have been disabled. 
		//Remove this isAvailable when migrations have been fixed.  This doer replies on parent's isA
		return false;
	}
	
	@Override
	protected MiradiFileSaveChooser createFileChooser()
	{
		return new XmpzFileChooser(getMainWindow());
	}

	@Override
	protected String getSchemaRelativeFilePath()
	{
		return WcsMiradiXmlValidator.WCS_MIRADI_SCHEMA_FILE_RELATIVE_PATH;
	}
}
