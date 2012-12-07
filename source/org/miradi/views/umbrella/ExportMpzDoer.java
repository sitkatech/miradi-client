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

package org.miradi.views.umbrella;

import java.io.File;

import org.miradi.main.EAM;
import org.miradi.project.ProjectSaver;
import org.miradi.utils.MiradiFileSaveChooser;
import org.miradi.utils.MpfToMpzConverter;
import org.miradi.utils.MpzFileChooser;
import org.miradi.utils.ProgressInterface;
import org.miradi.utils.Translation;

public class ExportMpzDoer extends AbstractFileSaverDoer
{
	@Override
	protected MiradiFileSaveChooser createFileChooser()
	{
		return new MpzFileChooser(getMainWindow());
	}

	@Override
	protected boolean doWork(File destinationFile, ProgressInterface progressInterface) throws Exception
	{
		MpfToMpzConverter converter = new MpfToMpzConverter(getProject());
		String mpfSnapShot = ProjectSaver.createSnapShot(getProject());
		converter.convert(mpfSnapShot, destinationFile);
		
		return true;
	}

	@Override
	protected String getProgressTitle()
	{
		return EAM.text("Export MPZ");
	}
	
	@Override
	protected boolean doesUserConfirm() throws Exception
	{
		String title = EAM.text("Title|MPZ Export Warning");
		String html = Translation.getHtmlContent("ExportMpzWarning.html");
		String export = EAM.text("Button|Export");
		String cancel = EAM.text("Button|Cancel");
		String[] buttonLabels = new String[] {export, cancel};
		boolean result = EAM.confirmDialog(title, new String[] {html}, buttonLabels);
		return result;
	}
}
