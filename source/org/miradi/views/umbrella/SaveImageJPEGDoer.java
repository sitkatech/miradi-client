/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import org.miradi.utils.JPEGFileFilter;
import org.miradi.utils.JpgFileChooser;
import org.miradi.utils.MiradiFileSaveChooser;

public class SaveImageJPEGDoer extends AbstractImageSaverDoer
{
	@Override
	protected MiradiFileSaveChooser createFileChooser()
	{
		return new JpgFileChooser(getMainWindow());
	}
	
	@Override
	public void saveImage(ImageOutputStream out, BufferedImage image) throws IOException
	{
		saveJpeg(out, image);
	}
	
	public static void saveImage(ByteArrayOutputStream rawOut, BufferedImage image) throws IOException
	{
		MemoryCacheImageOutputStream out = new MemoryCacheImageOutputStream(rawOut);
		saveJpeg(out, image);
	}

	public static void saveJpeg(ImageOutputStream out, BufferedImage image) throws IOException
	{
		String extensionWithoutDot = JPEGFileFilter.EXTENSION.replaceAll("\\.", "");
		Iterator<ImageWriter> writers = ImageIO.getImageWritersBySuffix(extensionWithoutDot);
		ImageWriter writer = writers.next();
		writer.setOutput(out);
		writer.write(image);
	}
}
