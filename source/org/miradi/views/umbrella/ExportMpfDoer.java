/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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

import java.io.File;

import org.miradi.main.EAM;
import org.miradi.project.ProjectSaver;
import org.miradi.utils.MiradiFileSaveChooser;
import org.miradi.utils.MpfFileChooser;
import org.miradi.utils.ProgressInterface;

public class ExportMpfDoer extends AbstractFileSaverDoer
{
	@Override
	protected MiradiFileSaveChooser createFileChooser()
	{
		return new MpfFileChooser(getMainWindow());
	}

	@Override
	protected boolean doWork(File destinationFile, ProgressInterface progressInterface) throws Exception
	{
		initializeSingleStepSaveProgressInterface(progressInterface);
		ProjectSaver.saveProject(getProject(), destinationFile);
		progressInterface.incrementProgress();
		
		return true;
	}

	@Override
	protected String getProgressTitle()
	{
		return EAM.text("Export Miradi Project");
	}	
}
