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
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import org.miradi.utils.MiradiFileSaveChooser;
import org.miradi.utils.PngFileChooser;

public class SaveImagePngDoer extends AbstractImageSaverDoer
{
	@Override
	protected MiradiFileSaveChooser createFileChooser()
	{
		return new PngFileChooser(getMainWindow());
	}
	
	public static void saveImage(ByteArrayOutputStream rawOut, BufferedImage image) throws IOException
	{
		MemoryCacheImageOutputStream out = new MemoryCacheImageOutputStream(rawOut);
		new SaveImagePngDoer().saveImage(out, image);
	}

	@Override
	public void saveImage(ImageOutputStream out, BufferedImage image) throws IOException
	{
		ImageIO.write(image, "png", out);
	}	
}
