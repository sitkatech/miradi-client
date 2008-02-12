/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.utils;

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class MultipleTableSelectionController implements ListSelectionListener
{
	public void addTable(JTable tableToAdd)
	{
		tables.add(tableToAdd);
		tableToAdd.getSelectionModel().addListSelectionListener(this);
	}

	public void valueChanged(ListSelectionEvent event)
	{
		adjustSelectionOfAllTables(event);
	}
	
	private void adjustSelectionOfAllTables(ListSelectionEvent event)
	{
		ListSelectionModel source = (ListSelectionModel) event.getSource();
		int leadSelectionIndex  = source.getLeadSelectionIndex();
		for (int i = 0; i < tables.size(); ++i)
		{
			JTable table = tables.get(i);
			table.getSelectionModel().setSelectionInterval(leadSelectionIndex, leadSelectionIndex);
		}
	}

	private Vector<JTable> tables = new Vector();
}
