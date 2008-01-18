/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import org.conservationmeasures.eam.utils.CodeList;

public interface RowColumnProvider
{
	public CodeList getRowListToShow() throws Exception;
	public CodeList getColumnListToShow() throws Exception;
	public String getPropertyName();
}
