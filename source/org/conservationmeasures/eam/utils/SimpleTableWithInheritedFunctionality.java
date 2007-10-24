package org.conservationmeasures.eam.utils;

import javax.swing.table.TableModel;

public class SimpleTableWithInheritedFunctionality extends TableWithColumnWidthSaver
{
	public SimpleTableWithInheritedFunctionality(TableModel model)
	{	
		super(model);
	}
	
	public String getUniqueTableIdentifier()
	{
		return UNIQUE_IDENTIFIER;
	}
	
	public static final String UNIQUE_IDENTIFIER = "SimpleTableWithInheritedFunctionality";
}
