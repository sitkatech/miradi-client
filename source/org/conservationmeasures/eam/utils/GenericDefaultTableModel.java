package org.conservationmeasures.eam.utils;

import javax.swing.table.DefaultTableModel;

public class GenericDefaultTableModel extends DefaultTableModel implements ColumnTagProvider
{
	public GenericDefaultTableModel(String[][] data, String[] names)
	{
		super(data, names);
	}

	public GenericDefaultTableModel()
	{
		super();
	}

	public String getColumnTag(int column)
	{
		return getColumnName(column);
	}
}
