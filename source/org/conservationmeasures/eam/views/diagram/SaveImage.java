/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.views.diagram;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.swing.JFileChooser;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.MainWindowDoer;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class SaveImage extends MainWindowDoer
{

	public SaveImage(MainWindow mainWindowToUse, BufferedImage imageToSave) 
	{
		super(mainWindowToUse);
		image = imageToSave;
	}

	public boolean isAvailable() 
	{
		return image != null;
	}

	public void doIt() throws CommandFailedException 
	{
		JFileChooser dlg = new JFileChooser();
		dlg.setDialogTitle(EAM.text("Title|Save JPEG Image"));
		dlg.setFileFilter(new JPEGFileFilter());
		dlg.setDialogType(JFileChooser.CUSTOM_DIALOG);
		dlg.setApproveButtonToolTipText(EAM.text("TT|Save JPeg Image"));
		if(dlg.showDialog(getMainWindow(), EAM.text("Save Image")) != JFileChooser.APPROVE_OPTION)
			return;
		
		File chosen = dlg.getSelectedFile();
		if(!chosen.getName().toLowerCase().endsWith(JPEGFileFilter.JPG_EXTENSION))
			chosen = new File(chosen.getAbsolutePath() + JPEGFileFilter.JPG_EXTENSION);

		if(chosen.exists())
		{
			String title = EAM.text("Title|Overwrite existing image?");
			String[] body = {EAM.text("This will replace the existing image with this one.")};
			if(!EAM.confirmDialog(title, body))
				return;
			chosen.delete();
		}

		try 
		{
			FileOutputStream out = new FileOutputStream(chosen);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out); 
			JPEGEncodeParam jpegParams = encoder.getDefaultJPEGEncodeParam(image);
			float quality = 1;
			jpegParams.setQuality(quality, false);
			encoder.setJPEGEncodeParam(jpegParams);
			encoder.encode(image); 
			out.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
	}
	
	private BufferedImage image;
}
