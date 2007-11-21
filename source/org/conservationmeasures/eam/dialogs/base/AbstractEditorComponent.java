/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.base;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.HideableScrollBar;
import org.conservationmeasures.eam.utils.MultiTableHorizontalScrollController;
import org.conservationmeasures.eam.utils.MultiTableVerticalScrollController;
import org.conservationmeasures.eam.utils.MultipleTableSelectionController;
import org.martus.swing.UiScrollPane;

abstract public class AbstractEditorComponent extends DisposablePanel
{
	public AbstractEditorComponent(Project projectToUse)
	{
		super(new BorderLayout());
		
		project = projectToUse;
		selectionController = new MultipleTableSelectionController();
		verticalController = new MultiTableVerticalScrollController();
		horizontalController = new MultiTableHorizontalScrollController();
	}
	
	public Project getProject()
	{
		return project;
	}
	
	protected void addVerticalAndHorizontalScrollableControlledTable(Box horizontalBox, JScrollPane scroller)
	{
		horizontalController.addTable(scroller);
		addVerticalScrollableControlledTable(horizontalBox, scroller);	
	}
	
	protected void addVerticalScrollableControlledTable(Box horizontalBox, JScrollPane scroller)
	{
		verticalController.addTable(scroller);
		horizontalBox.add(scroller);
	}
	
	protected class ScrollPaneWithInvisibleVerticalScrollBar extends AssignmentTableScrollPane
	{
		public ScrollPaneWithInvisibleVerticalScrollBar(JComponent contents)
		{
			super(contents);
			HideableScrollBar hideableScrollBar = new HideableScrollBar();
			hideableScrollBar.visible = false;
			setVerticalScrollBar(hideableScrollBar);
		}
	}
	
	protected class AssignmentTableScrollPane extends UiScrollPane
	{
		public AssignmentTableScrollPane(JComponent contents)
		{
			super(contents);
			setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			getViewport().setBackground(contents.getBackground());
		}
	}

	private Project project;
	protected MultipleTableSelectionController selectionController;
	protected MultiTableVerticalScrollController verticalController;
	protected MultiTableHorizontalScrollController horizontalController;
}
