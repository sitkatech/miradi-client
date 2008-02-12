/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.questions;

import org.miradi.objecthelpers.ManagingOfficeFileLoader;
import org.miradi.objecthelpers.TwoLevelFileLoader;


public class WwfManagingOfficesQuestion extends TwoLevelQuestion
{
	public WwfManagingOfficesQuestion()
	{
		super(new ManagingOfficeFileLoader(TwoLevelFileLoader.WWF_MANAGING_OFFICES_FILE));
	}
}
