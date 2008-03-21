/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.utils.EAMFileSaveChooser;
import org.miradi.views.ViewDoer;

abstract public class AbstractImageSaverDoer extends ViewDoer
{
	public boolean isAvailable()
	{
		Project project = getMainWindow().getProject();
		if(!project.isOpen())
			return false;
		
		return getView().isImageAvailable();
	}

	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		EAMFileSaveChooser eamFileChooser = getFileChooser();
		File chosen = eamFileChooser.displayChooser();
		if (chosen==null) 
			return;
		
		try 
		{
			FileOutputStream out = new FileOutputStream(chosen);
			saveImage(out); 
			out.close();
		} 
		catch (Exception e) 
		{
			EAM.logException(e);
		} 
	}
	
	private void saveImage(FileOutputStream out) throws Exception
	{
		BufferedImage image = getView().getImage();
		saveImage(out, image);
	}

	abstract public void saveImage(OutputStream out, BufferedImage image) throws IOException;

	abstract protected EAMFileSaveChooser getFileChooser();
}
