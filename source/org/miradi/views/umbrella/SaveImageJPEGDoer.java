/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.umbrella;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import org.miradi.utils.EAMFileSaveChooser;
import org.miradi.utils.EAMJPGFileChooser;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class SaveImageJPEGDoer extends AbstractImageSaverDoer
{
	protected EAMFileSaveChooser getFileChooser()
	{
		return new EAMJPGFileChooser(getMainWindow());
	}
	
	public void saveImage(OutputStream out, BufferedImage image) throws IOException
	{
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out); 
		JPEGEncodeParam jpegParams = encoder.getDefaultJPEGEncodeParam(image);
		float quality = 1;
		jpegParams.setQuality(quality, false);
		encoder.setJPEGEncodeParam(jpegParams);
		encoder.encode(image);
	}
}
