/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

public interface SplitterPositionSaverAndGetter
{
	public int getSplitterLocation(String name);
	public void saveSplitterLocation(String name, int location);
}
