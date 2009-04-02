/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.dialogs.treetables;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.BoundedRangeModel;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import org.miradi.main.MainWindow;
import org.miradi.utils.FastScrollBar;
import org.miradi.utils.HideableScrollBar;
import org.miradi.utils.MiradiScrollPane;
import org.miradi.utils.MultiTableRowHeightController;
import org.miradi.utils.MultiTableVerticalScrollController;
import org.miradi.utils.MultipleTableSelectionController;
import org.miradi.utils.TableWithRowHeightSaver;

abstract public class MultiTreeTablePanel extends TreeTablePanel
{
	public MultiTreeTablePanel(MainWindow mainWindowToUse, TreeTableWithStateSaving treeToUse) throws Exception
	{
		this(mainWindowToUse, treeToUse, new Class[0]);
	}
	
	public MultiTreeTablePanel(MainWindow mainWindowToUse, TreeTableWithStateSaving treeToUse, Class[] buttonActionClasses) throws Exception
	{
		super(mainWindowToUse, treeToUse, buttonActionClasses);
		
		// NOTE: Replace tree scroll pane created by super constructor
		ScrollPaneWithHideableScrollBar newTreeScrollPane = new ScrollPaneWithHideableScrollBar(getTree());
		newTreeScrollPane.hideVerticalScrollBar();

		treeTableScrollPane = newTreeScrollPane;
		treeTableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	}
	
	protected ScrollPaneWithHideableScrollBar integrateTable(JScrollBar masterScrollBar, MultiTableVerticalScrollController scrollController, MultiTableRowHeightController rowHeightController, MultipleTableSelectionController selectionController, TreeTableWithStateSaving treeToUse, TableWithRowHeightSaver table)
	{
		ModelUpdater modelUpdater = new ModelUpdater((AbstractTableModel)table.getModel());
		treeToUse.getTreeTableAdapter().addTableModelListener(modelUpdater);
		
		selectionController.addTable(table);
		rowHeightController.addTable(table);
		listenForColumnWidthChanges(table);

		ScrollPaneWithHideableScrollBar scrollPane = new ScrollPaneNoExtraWidth(table);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.hideVerticalScrollBar();
		scrollPane.addMouseWheelListener(new MouseWheelHandler(masterScrollBar));

		scrollController.addScrollPane(scrollPane);
		
		return scrollPane;
	}
	
	public static class MouseWheelHandler implements MouseWheelListener
	{
		public MouseWheelHandler(JScrollBar masterScrollBarToUse)
		{
			scrollBar = masterScrollBarToUse;
		}
		
		public void mouseWheelMoved(MouseWheelEvent e)
		{
			if(e.getScrollType() != e.WHEEL_UNIT_SCROLL)
				return;
			
			scrollBar.setValue(scrollBar.getValue() + e.getUnitsToScroll());
		}
		
		private JScrollBar scrollBar;
	}
		
	public static class ModelUpdater implements TableModelListener
	{
		public ModelUpdater(AbstractTableModel modelToUpdateToUse)
		{
			modelToUpdate = modelToUpdateToUse;
		}
		
		public void tableChanged(TableModelEvent e)
		{
			modelToUpdate.fireTableDataChanged();
		}
		
		private AbstractTableModel modelToUpdate;
	}

	public static class MasterVerticalScrollBar extends FastScrollBar implements ChangeListener
	{
		public MasterVerticalScrollBar(JScrollPane baseRangeOn)
		{
			super(VERTICAL);
			baseRangeOn.getVerticalScrollBar().getModel().addChangeListener(this);
			otherScrollBar = baseRangeOn.getVerticalScrollBar();
		}

		public void stateChanged(ChangeEvent e)
		{
			updateRange();
		}

		private void updateRange()
		{
			BoundedRangeModel ourModel = getModel();
			BoundedRangeModel otherModel = otherScrollBar.getModel();
			ourModel.setMinimum(otherModel.getMinimum());
			ourModel.setMaximum(otherModel.getMaximum());
			ourModel.setExtent(otherModel.getExtent());
		}

		private JScrollBar otherScrollBar;
	}

	public static class ShrinkToFitVerticallyHorizontalBox extends JPanel
	{
		public ShrinkToFitVerticallyHorizontalBox()
		{
			BoxLayout layout = new BoxLayout(this, BoxLayout.LINE_AXIS);
			setLayout(layout);
			
			enableShrinking();
		}
		
		public void disableShrinking()
		{
			shouldShrink = false;
		}
		
		public void enableShrinking()
		{
			shouldShrink = true;
		}
		
		private boolean shouldShrink()
		{
			return shouldShrink;
		}
		
		@Override
		public void setPreferredSize(Dimension preferredSize)
		{
			overriddenPreferredSize = preferredSize;
		}
		
		@Override
		public Dimension getPreferredSize()
		{
			if (!shouldShrink())
				return super.getPreferredSize();
			
			if(overriddenPreferredSize != null)
				return overriddenPreferredSize;
			
			Dimension size = new Dimension(super.getPreferredSize());
			Container parent = getParent();
			if(parent == null)
				return size;
			
			setPreferredSize(new Dimension(0,0));
			Dimension max = parent.getPreferredSize();
			setPreferredSize(null);
			size.height = Math.min(size.height, max.height);
			return size;
		}
		
		private Dimension overriddenPreferredSize;
		private boolean shouldShrink;
	}

	public static class ScrollPaneWithHideableScrollBar extends MiradiScrollPane
	{
		public ScrollPaneWithHideableScrollBar(Component component)
		{
			super(component);
			hideableScrollBar = new HideableScrollBar();
			setVerticalScrollBar(hideableScrollBar);
		}
		
		public void showVerticalScrollBar()
		{
			hideableScrollBar.visible = true;
		}
		
		public void hideVerticalScrollBar()
		{
			hideableScrollBar.visible = false;
		}

		private HideableScrollBar hideableScrollBar;
	}
}
