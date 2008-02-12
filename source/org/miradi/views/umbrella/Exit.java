/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.umbrella;

import org.miradi.views.MainWindowDoer;

public class Exit extends MainWindowDoer
{
	public void doIt()
	{
		getMainWindow().exitNormally();
	}
	
	public boolean isAvailable()
	{
		return true;
	}
}
