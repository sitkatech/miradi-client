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
package org.miradi.views.umbrella;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.miradi.dialogs.ImageExportScaleDialog;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
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
	protected boolean doWork(File destinationFile, ProgressInterface progressInterface) throws Exception
	{
		FileOutputStream out = new FileOutputStream(destinationFile);
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
	
	private void saveImage(FileOutputStream out, ProgressInterface progressInterface) throws Exception
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

	abstract public void saveImage(OutputStream out, BufferedImage image) throws IOException;

	private int scalePercent;
}
