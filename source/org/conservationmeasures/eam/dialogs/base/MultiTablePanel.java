/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.base;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.dialogs.treetables.TreeTableNode;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.TableWithRowHeightSaver;
import org.conservationmeasures.eam.utils.MiradiScrollPane;
import org.conservationmeasures.eam.utils.HideableScrollBar;
import org.conservationmeasures.eam.utils.MultiTableHorizontalScrollController;
import org.conservationmeasures.eam.utils.MultiTableRowHeightController;
import org.conservationmeasures.eam.utils.MultiTableVerticalScrollController;
import org.conservationmeasures.eam.utils.MultipleTableSelectionController;
import org.conservationmeasures.eam.views.threatmatrix.ThreatGridPanel;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;
import org.martus.swing.UiScrollPane;

abstract public class MultiTablePanel extends DisposablePanel implements ObjectPicker
{
	public MultiTablePanel(Project projectToUse)
	{
		super(new BorderLayout());
		
		project = projectToUse;
		selectionController = new MultipleTableSelectionController();
		verticalController = new MultiTableVerticalScrollController();
		horizontalController = new MultiTableHorizontalScrollController();
		rowHeightController = new MultiTableRowHeightController();
	}
	
	public Project getProject()
	{
		return project;
	}
	
	protected void addRowHeightControlledTable(TableWithRowHeightSaver tableToAdd)
	{
		rowHeightController.addTable(tableToAdd);
	}
	
	protected void addToVerticalController(JScrollPane scroller)
	{
		verticalController.addTable(scroller);
	}
	
	protected void addToHorizontalController(JScrollPane scroller)
	{
		horizontalController.addTable(scroller);
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
	
	public class FixedWidthScrollPaneWithInvisibleVerticalScrollBar extends FixedWidthScrollPane
	{
		public FixedWidthScrollPaneWithInvisibleVerticalScrollBar(JComponent contentToUse)
		{
			super(contentToUse);
			HideableScrollBar hideableScrollBar = new HideableScrollBar();
			hideableScrollBar.visible = false;
			setVerticalScrollBar(hideableScrollBar);
		}
	}
	
	public class FixedWidthScrollPane extends MiradiScrollPane
	{
		public FixedWidthScrollPane(JComponent contentToUse)
		{
			super(contentToUse);
			
			content = contentToUse;
		}

		public Dimension getMinimumSize()
		{
			Dimension minimumSize = super.getMinimumSize();
			return new Dimension(getPreferredSize().width, minimumSize.height);
		}
		
		public Dimension getMaximumSize()
		{
			return getPreferredSize();
		} 
		
		public Dimension getPreferredSize()
		{
			Dimension preferredSize = content.getPreferredSize();
			return new Dimension(120, preferredSize.height);
		}
			
		private Component content;
	}

	public class FixedHeightScrollPane extends MiradiScrollPane
	{
		public FixedHeightScrollPane(JComponent contentToUse)
		{
			super(contentToUse);
			
			content = contentToUse;
		}

		public Dimension getMinimumSize()
		{
			return getPreferredSize();
		}
		
		public Dimension getMaximumSize()
		{
			return getPreferredSize();
		} 
		
		public Dimension getPreferredSize()
		{
			Dimension preferredSize = content.getPreferredSize();
			return new Dimension(preferredSize.width, ThreatGridPanel.ROW_HEIGHT);
		}
		
		private Component content;
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
	
	public BaseObject[] getSelectedObjects()
	{
		return null;
	}

	public TreeTableNode[] getSelectedTreeNodes()
	{
		return null;
	}

	public ORefList getSelectionHierarchy()
	{
		return null;
	}

	public void removeSelectionChangeListener(ListSelectionListener listener)
	{
	}
	
	public void addSelectionChangeListener(ListSelectionListener listener)
	{
	}

	public void clearSelection()
	{
	}

	public void ensureObjectVisible(ORef ref)
	{
	}
	
	public void valueChanged(ListSelectionEvent arg0)
	{
	}

	private Project project;
	protected MultipleTableSelectionController selectionController;
	protected MultiTableVerticalScrollController verticalController;
	protected MultiTableHorizontalScrollController horizontalController;
	private MultiTableRowHeightController rowHeightController;
}
