/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import java.util.Iterator;

public class MiradiProjectData implements Iterator
{
	public MiradiProjectData(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public boolean hasNext() 
	{
		--count;
		return (count>=0);
		
	}

	public Object next() 
	{
		return project.getMetadata();
	}

	public void remove() 
	{
	}
	
	int count = 2;
	Project project;
}
