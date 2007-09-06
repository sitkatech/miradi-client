/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import java.util.Vector;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
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
		DefaultListSelectionModel source = (DefaultListSelectionModel) event.getSource();
		int leadSelectionIndex  = source.getLeadSelectionIndex();
		for (int i = 0; i < tables.size(); ++i)
		{
			JTable table = tables.get(i);
			table.getSelectionModel().setSelectionInterval(leadSelectionIndex, leadSelectionIndex);
		}
	}

	private Vector<JTable> tables = new Vector();
}
