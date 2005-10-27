/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.IOException;

public class ProjectVisionAnnotation extends NodeAnnotations
{
	public Color getColor()
	{
		return LIGHT_BLUE;
	}

	public void readDataFrom(DataInputStream dataIn) throws IOException
	{
	}

	public boolean equals(Object obj)
	{
		return false;
	}

	private static final Color LIGHT_BLUE = new Color(204,238,255);
}
