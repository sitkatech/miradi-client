/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

public interface SplitterPositionSaver
{
	public int getSplitterLocation(int height, String name);
	public void setSplitterLocation(int height, String name, int location);
	public void setSplitterLocationToMiddle(String name);
}
