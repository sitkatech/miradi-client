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
package org.miradi.views.umbrella;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

import org.miradi.dialogs.ImageExportScaleDialog;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.utils.ProgressInterface;

abstract public class AbstractImageSaverDoer extends AbstractFileSaverDoer
{
	@Override
	public boolean isAvailable()
	{
		boolean superIsAvailable = super.isAvailable();
		if (!superIsAvailable)
			return false;
		
		return getView().isImageAvailable();
	}

	@Override
	protected boolean doWork(Project project, File destinationFile, ProgressInterface progressInterface) throws Exception
	{
		ImageOutputStream out = createOutputStream(destinationFile);
		try
		{
			saveImage(out, progressInterface);
			return true;
		}
		finally
		{
			out.close();	
		}
	}

	protected FileImageOutputStream createOutputStream(File destinationFile) throws IOException
	{
		return new FileImageOutputStream(destinationFile);
	}

	@Override
	protected void tryAgain() throws Exception
	{
		safeDoIt();
	}
	
	@Override
	protected boolean doesUserConfirm() throws Exception
	{
		if(!isInDiagram())
			return true;
		
		int defaultScalePercent = 100;
		AppPreferences appPreferences = getMainWindow().getAppPreferences();
		scalePercent = appPreferences.getDiagramExportScalePercent();
		if(scalePercent == 0)
			scalePercent = defaultScalePercent;
		ImageExportScaleDialog dialog = new ImageExportScaleDialog(getMainWindow(), scalePercent);
		dialog.showDialog();
		boolean userChoseOk = dialog.userChoseOk();
		if(userChoseOk)
		{
			scalePercent = dialog.getScalePercent();
			appPreferences.setDiagramImageExportScalePercent(scalePercent);
		}
		return userChoseOk;
	}
	
	private void saveImage(ImageOutputStream out, ProgressInterface progressInterface) throws Exception
	{
		progressInterface.setStatusMessage(EAM.text("save..."), 2);
		BufferedImage image = getView().getImage(scalePercent);
		progressInterface.incrementProgress();
		
		saveImage(out, image);
		progressInterface.incrementProgress();
	}
	
	@Override
	protected String getProgressTitle()
	{
		return EAM.text("Save...");
	}

	abstract public void saveImage(ImageOutputStream out, BufferedImage image) throws IOException;

	private int scalePercent;
}
