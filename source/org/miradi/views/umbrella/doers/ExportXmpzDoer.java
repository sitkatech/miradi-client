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

import org.miradi.main.EAM;
import org.miradi.utils.ConstantButtonNames;
import org.miradi.utils.MiradiFileSaveChooser;
import org.miradi.utils.XmpzFileChooser;
import org.miradi.xml.wcs.WcsMiradiXmlValidator;

public class ExportXmpzDoer extends AbstractExportProjectXmlZipDoer
{
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
	
	@Override
	protected boolean doesUserConfirm() throws Exception
	{
		return doesUserConfirmXmpz1Export();
	}

	public static boolean doesUserConfirmXmpz1Export()
	{
		String title = EAM.text("Export Project");
		String[] body = new String[] {
			EAM.text("The XMPZ format is only supported for compatibility with Miradi 3.3. " +
					"It does not support some of the newer features of Miradi 4.0, so exporting " +
					"your project to the XMPZ format may lose data. If you just " +
					"want a backup copy of your project, or if you want to send a copy to " +
					"someone who uses Miradi 4.0, you should either export to the XMPZ2 format, " +
					"or just use the .Miradi project file directly.  " +
					"NOTE: This data format can be used for sending to other systems or creating reports. " +
					"It cannot be used to transfer data from one copy or version of Miradi to another.\n" +
					"Do you want to continue with XMPZ export that may lose data?"),
		};

		String[] buttons = new String[] {
			EAM.text("Export"),
			ConstantButtonNames.CANCEL,
		};
		
		return EAM.confirmDialog(title, body, buttons);
	}
}
