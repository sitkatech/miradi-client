/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import java.awt.Adjustable;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.Vector;

import javax.swing.JScrollPane;

public class MultiTableScrollController implements AdjustmentListener
{
	public MultiTableScrollController(int orientationToUse)
	{
		orientation = orientationToUse;
	}

	public void addTable(JScrollPane scrollPaneToAdd)
	{
		scrollPaneToAdd.getVerticalScrollBar().addAdjustmentListener(this);
		scrollPanes.add(scrollPaneToAdd);
	}
	
	public void adjustmentValueChanged(AdjustmentEvent event)
	{
		adjustAllScrollBars(event);
	}
	
	private void adjustAllScrollBars(AdjustmentEvent event)
	{
		Adjustable source = event.getAdjustable();
		int sourceOrientation = source.getOrientation();
        if (sourceOrientation != orientation)
        	return;
        
        int valueToSetTo = event.getValue();
        for (int i = 0; i < scrollPanes.size(); ++i)
        {
        	JScrollPane currentPane = scrollPanes.get(i);
        	currentPane.getVerticalScrollBar().setValue(valueToSetTo);
        }
	}
	
	private Vector<JScrollPane> scrollPanes = new Vector();
	private int orientation;
}
