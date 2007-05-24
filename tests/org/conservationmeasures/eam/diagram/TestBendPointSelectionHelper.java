/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

import org.conservationmeasures.eam.main.EAMTestCase;

public class TestBendPointSelectionHelper extends EAMTestCase
{
	public TestBendPointSelectionHelper(String name)
	{
		super(name);
	}

	public void testMousePressed()
	{
		
	}
	
	public void testMouseEvent()
	{
		MouseEvent mouseEvent = getMouseEvent(new Point(1, 1), InputEvent.SHIFT_DOWN_MASK);
		assertEquals("shift not down?", true, mouseEvent.isShiftDown());
		assertEquals("wrong click location?", new Point(1, 1), mouseEvent.getPoint());
		assertEquals("control down?", false, mouseEvent.isControlDown());
	}
	
	private MouseEvent getMouseEvent(Point point, int modifiersToUse)
	{
		Component source = new JButton();
		int id = 0;
		long when = 0;
		int modifiers = modifiersToUse;
        int x = point.x;
        int y = point.y;
        int clickCount = 1;
        boolean popupTrigger = false;
        int button = 0;

		return new MouseEvent(source, id, when, modifiers, x, y, clickCount, popupTrigger, button);
	}
}