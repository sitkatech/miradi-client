/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.miradi.views.umbrella;

import org.miradi.wizard.WizardManager;


public class WizardNextDoer extends WizardNavigationDoer
{
	String getControlName()
	{
		return WizardManager.CONTROL_NEXT;
	}
}
