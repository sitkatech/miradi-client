/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main;


public class TransferableMiradiList extends TransferableEamList
{
	public TransferableMiradiList(String projectFileName, String[] objectsAsJsonStringToUse)
	{
		super(projectFileName, objectsAsJsonStringToUse);
		deepCopyJsonsAsString =	objectsAsJsonStringToUse;
	}
	
	public String[] getDeepCopiesAsJsonString()
	{
		return deepCopyJsonsAsString;
	}
	
	protected void storeData(Object[] cells)
	{
		//TODO in transition process
	}
	
	//FIXME this is to switch between falvors while in transition 
	public boolean isEAMFlavorSupported()
	{
		return false;
	}
	
	String[] deepCopyJsonsAsString;
}
