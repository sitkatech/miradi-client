/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.utils;

import org.miradi.main.EAM;

public class PNGFileFilter extends AbstractMiradiFileFilter
{
	public PNGFileFilter() 
	{
		super();
	}

	public String getDescription()
	{
		return EAM.text("FileFilter|PNG (*.png)");
	}
	
	public String getFileExtension()
	{
		return EXTENSION;
	}
	
	public static final String EXTENSION = ".png";
}
