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
	protected boolean doWork(File destinationFile) throws Exception
	{
		FileOutputStream out = new FileOutputStream(destinationFile);
		try
		{
			saveImage(out);
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
		doIt();
	}
	
	@Override
	protected boolean doesUserConfirm() throws Exception
	{
		int defaultScale = 100;
		ImageExportScaleDialog dialog = new ImageExportScaleDialog(getMainWindow(), defaultScale);
		dialog.showDialog();
		boolean userChoseOk = dialog.userChoseOk();
		if(userChoseOk)
			scale = dialog.getScale();
		return userChoseOk;
	}
	
	private void saveImage(FileOutputStream out) throws Exception
	{
		BufferedImage image = getView().getImage(scale);
		saveImage(out, image);
	}

	abstract public void saveImage(OutputStream out, BufferedImage image) throws IOException;

	private int scale;
}
