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
import java.awt.geom.Point2D;

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
		BendPointSelectionHelper selectionHelper = new BendPointSelectionHelper();
		Point2D[] selectionList = selectionHelper.getSelectionList();
		assertEquals("selection list not empty?", 0, selectionList.length);
		
		clickWithNoModifiers(selectionHelper);
	}
	
	private void clickWithNoModifiers(BendPointSelectionHelper selectionHelper)
	{
		MouseEvent mouseEvent = getMouseEvent(InputEvent.SHIFT_DOWN_MASK);
		Point currentBendPoint1 = new Point(1, 1);
		
		pressMouseButton(selectionHelper, mouseEvent, currentBendPoint1);
		assertEquals("not added selection?", 1, selectionHelper.getSelectionList().length);
		assertEquals("wrong selection added?", currentBendPoint1, selectionHelper.getSelectionList()[0]);
		
		pressMouseButton(selectionHelper, mouseEvent, currentBendPoint1);
		assertEquals("not removed selection?", 0, selectionHelper.getSelectionList().length);
	}

	private void pressMouseButton(BendPointSelectionHelper selectionHelper, MouseEvent mouseEvent, Point currentBendPoint1)
	{
		selectionHelper.mousePressed(mouseEvent, currentBendPoint1);
	}
	
	public void testShouldRemove()
	{
		MouseEvent mouseEvent = getMouseEvent(InputEvent.SHIFT_DOWN_MASK);
		BendPointSelectionHelper selectionHelper = new BendPointSelectionHelper();
		Point selectedPoint1 = new Point(1, 1);
		
		assertEquals("can remove?", false, selectionHelper.canRemove(mouseEvent, selectedPoint1));
		
		selectionHelper.addToSelection(selectedPoint1);		
		assertEquals("can not remove?", true, selectionHelper.canRemove(mouseEvent, selectedPoint1));
		
	}
	
	public void testShouldAdd()
	{
		MouseEvent mouseEvent = getMouseEvent(InputEvent.SHIFT_DOWN_MASK);
		BendPointSelectionHelper selectionHelper = new BendPointSelectionHelper();
		Point selectedPoint1 = new Point(1, 1);
		
		assertEquals("selection not remove?", true, selectionHelper.canAdd(mouseEvent, selectedPoint1));
		
		selectionHelper.addToSelection(selectedPoint1);
		assertEquals("selection not remove?", false, selectionHelper.canAdd(mouseEvent, selectedPoint1));
	}

	public void testMouseEvent()
	{
		MouseEvent mouseEvent = getMouseEvent(InputEvent.SHIFT_DOWN_MASK);
		assertEquals("shift not down?", true, mouseEvent.isShiftDown());
		assertEquals("wrong click location?", new Point(0, 0), mouseEvent.getPoint());
		assertEquals("control down?", false, mouseEvent.isControlDown());
	}
	
	private MouseEvent getMouseEvent(int modifiersToUse)
	{
		Component source = new JButton();
		int id = 0;
		long when = 0;
		int modifiers = modifiersToUse;
        int x = 0;
        int y = 0;
        int clickCount = 1;
        boolean popupTrigger = false;
        int button = 0;

		return new MouseEvent(source, id, when, modifiers, x, y, clickCount, popupTrigger, button);
	}
}