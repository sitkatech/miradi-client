/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.io.IOException;

import org.conservationmeasures.eam.diagram.nodes.Linkage;
import org.conservationmeasures.eam.diagram.nodes.Node;

public class ProjectForTesting extends Project
{

	public ProjectForTesting() throws IOException
	{
		super(new Storage());
	}

	public boolean isOpen()
	{
		return true;
	}
	
	public void closeDatabase()
	{
	}

	public void deleteNodeFromDatabase(int id)
	{
	}
	
	public void insertNodeInDatabase(Node node)
	{
	}
	
	public void deleteLinkageFromDatabase(int id)
	{
	}
	
	public void insertLinkageInDatabase(Linkage linkage)
	{
	}

}
