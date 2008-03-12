/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
