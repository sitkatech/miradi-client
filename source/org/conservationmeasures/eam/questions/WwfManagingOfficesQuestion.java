/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import org.conservationmeasures.eam.objecthelpers.ManagingOfficeFileLoader;
import org.conservationmeasures.eam.objecthelpers.TwoLevelFileLoader;


public class WwfManagingOfficesQuestion extends TwoLevelQuestion
{
	public WwfManagingOfficesQuestion(String tag)
	{
		super(tag, new ManagingOfficeFileLoader(TwoLevelFileLoader.WWF_MANAGING_OFFICES_FILE));
	}
}
