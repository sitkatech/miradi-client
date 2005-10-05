/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.io.File;

public class ProjectForTesting extends RealProject
{
	public ProjectForTesting(File projectDirectory) throws Exception
	{
		open(projectDirectory);
	}
}
