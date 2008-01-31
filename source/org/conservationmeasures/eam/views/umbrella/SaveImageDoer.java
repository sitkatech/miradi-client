/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.EAMJPGFileChooser;
import org.conservationmeasures.eam.views.ViewDoer;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class SaveImageDoer extends ViewDoer
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
		
		EAMJPGFileChooser eamFileChooser = new EAMJPGFileChooser(getMainWindow());
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

	private void saveImage(FileOutputStream out) throws IOException 
	{
		BufferedImage image = getView().getImage();
		saveImage(out, image);
	}

	public static void saveImage(OutputStream out, BufferedImage image) throws IOException
	{
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out); 
		JPEGEncodeParam jpegParams = encoder.getDefaultJPEGEncodeParam(image);
		float quality = 1;
		jpegParams.setQuality(quality, false);
		encoder.setJPEGEncodeParam(jpegParams);
		encoder.encode(image);
	}
}
