/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.umbrella;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.miradi.utils.EAMFileSaveChooser;
import org.miradi.utils.EAMPNGFileChooser;

public class SaveImagePngDoer extends AbstractImageSaverDoer
{
	protected EAMFileSaveChooser getFileChooser()
	{
		return new EAMPNGFileChooser(getMainWindow());
	}
	
	public void saveImage(OutputStream out, BufferedImage image) throws IOException
	{
		ImageIO.write(image, "png", out);
	}	
}
