/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.utils;

import java.awt.Adjustable;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.Vector;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

abstract public class MultiTableScrollController implements AdjustmentListener
{
	public MultiTableScrollController(int orientationToUse)
	{
		orientation = orientationToUse;
	}

	public void addTable(JScrollPane scrollPaneToAdd)
	{
		getScrollBar(scrollPaneToAdd).addAdjustmentListener(this);
		scrollPanes.add(scrollPaneToAdd);
	}

	public void adjustmentValueChanged(AdjustmentEvent event)
	{
		if(disableListening)
			return;
		adjustAllScrollBars(event);
	}
	
	private void adjustAllScrollBars(AdjustmentEvent event)
	{
		Adjustable source = event.getAdjustable();
		int sourceOrientation = source.getOrientation();
        if (sourceOrientation != orientation)
        	return;
        
        disableListening = true;
        try
        {
	        double percentToSetTo = ((double)event.getValue()) / (source.getMaximum() - source.getMinimum());
	        for (int i = 0; i < scrollPanes.size(); ++i)
	        {
	        	JScrollPane currentPane = scrollPanes.get(i);
	        	JScrollBar scrollBar = getScrollBar(currentPane);
	            int valueToSetTo = (int) (percentToSetTo * (scrollBar.getMaximum() - scrollBar.getMinimum()));
	            scrollBar.setValue(valueToSetTo);
	        }
        }
        finally
        {
        	disableListening = false;
        }
	}
	
	abstract protected JScrollBar getScrollBar(JScrollPane scrollPaneToAdd);	

	private Vector<JScrollPane> scrollPanes = new Vector();
	private int orientation;
	
	private boolean disableListening;
}
