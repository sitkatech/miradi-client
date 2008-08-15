/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.BoundedRangeModel;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.TreePath;

import org.martus.swing.UiButton;
import org.miradi.actions.Actions;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandDeleteObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.base.ObjectCollectionPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.utils.FastScrollBar;
import org.miradi.utils.FastScrollPane;
import org.miradi.utils.HideableScrollBar;
import org.miradi.utils.MiradiScrollPane;

import com.jhlabs.awt.GridLayoutPlus;

abstract public class TreeTablePanel extends ObjectCollectionPanel  implements TreeSelectionListener
{
	public TreeTablePanel(MainWindow mainWindowToUse, TreeTableWithStateSaving treeToUse)
	{
		this(mainWindowToUse, treeToUse, new Class[0]);
	}
	
	public TreeTablePanel(MainWindow mainWindowToUse, TreeTableWithStateSaving treeToUse, Class[] buttonActionClasses)
	{
		super(mainWindowToUse, treeToUse);
		mainWindow = mainWindowToUse;
		tree = treeToUse;
		
		restoreTreeExpansionState();
		treeTableScrollPane = new ScrollPaneWithHideableScrollBar(tree);
		add(treeTableScrollPane, BorderLayout.CENTER);
		
		buttonBox = createButtonBox(buttonActionClasses);
		add(buttonBox,BorderLayout.AFTER_LAST_LINE);

		tree.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tree.getTree().addSelectionRow(0);
		tree.getTree().addTreeSelectionListener(this);
	}

	private JPanel createButtonBox(Class[] buttonActionClasses)
	{
		GridLayoutPlus layout = createButtonLayout();
		JPanel box = new JPanel(layout);
		box.setBackground(AppPreferences.getDataPanelBackgroundColor());
		addButtonsToBox(buttonActionClasses, box, mainWindow.getActions());
		return box;
	}

	abstract protected GridLayoutPlus createButtonLayout();
	
	public TreeTableWithStateSaving getTree()
	{
		return tree;
	}

	protected void restoreTreeExpansionState() 
	{
		try
		{
			tree.restoreTreeState();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Error restoring tree state"));
		}
	}

	public void dispose()
	{
		tree.dispose();
		super.dispose();
	}
	
	public BaseObject getSelectedObject()
	{
		TreeTableNode selectedTreeNode = getSelectedTreeNode();
		if(selectedTreeNode == null)
			return null;
		return selectedTreeNode.getObject();
	}

	public TreeTableNode getSelectedTreeNode()
	{
		return (TreeTableNode)tree.getTree().getLastSelectedPathComponent();
	}

	public GenericTreeTableModel getModel()
	{
		return model;
	}
	
	public void addButtonsToBox(Class[] classes, JPanel box, Actions actions)
	{
		for (int i=0; i<classes.length; ++i)
		addCreateButtonAndAddToBox(classes[i], box, actions);
	}
	
	private void addCreateButtonAndAddToBox(Class actionClass, JPanel box, Actions actions)
	{
		UiButton button = createObjectsActionButton(actions.getObjectsAction(actionClass), tree);
		box.add(button);
	}
	

	public void valueChanged(TreeSelectionEvent e)
	{	
		if (getPropertiesPanel() == null)
			return;
		
		TreePath[] treePaths = tree.getTree().getSelectionPaths();
		if (treePaths==null)
			return;
		
		Object[] selectedObjects = treePaths[0].getPath();
		
		Vector objects = new Vector();
		for (int i=0; i< selectedObjects.length; ++i)
		{
			TreeTableNode node = (TreeTableNode)selectedObjects[i];
			objects.insertElementAt(node.getObjectReference(), 0);
		}
		
		getPropertiesPanel().setObjectRefs((ORef[])objects.toArray(new ORef[0]));
		mainWindow.updateActionStates();
	}
	
	
	//TODO:Is this needed? Is it the right place/mechanism? 
	public void setSelectedObject(ORef ref)
	{
	}
	
	protected boolean isDeleteCommand(CommandExecutedEvent event, int type)
	{
		if (! event.isDeleteObjectCommand())
			return false;
		
		CommandDeleteObject deleteCommand = (CommandDeleteObject) event.getCommand();
		if (deleteCommand.getObjectType() != type)
			return false;
		
		return true;
	}

	protected boolean isCreateCommand(CommandExecutedEvent event, int type)
	{
		if (! event.isCreateObjectCommand())
			return false;
	
		CommandCreateObject createCommand = (CommandCreateObject) event.getCommand();
		if (createCommand.getObjectType() != type)
			return false;
		
		return true;
	}

	protected boolean isSelectedObjectModification(CommandExecutedEvent event, int typeToCheck)
	{
		if (! event.isSetDataCommand())
			return false;
		
		TreeTableNode node = getSelectedTreeNode();
		if (node == null)
			return false;
		
		BaseObject selectedObject = node.getObject(); 
		if (selectedObject == null)
			return false;
		
		CommandSetObjectData setCommand = (CommandSetObjectData) event.getCommand();
		int setType = setCommand.getObjectType();
		if(setType == typeToCheck)
			return true;
		
		String setField = setCommand.getFieldTag();
		
		String[] fieldTags = selectedObject.getFieldTags();
		Vector fields = new Vector(Arrays.asList(fieldTags));
	
		boolean sameType = (selectedObject.getType() == setType);
		boolean containsField = (fields.contains(setField));
		return (sameType && containsField);
	}

	protected void repaintToGrowIfTreeIsTaller()
	{
		if (getTopLevelAncestor() != null)
			getTopLevelAncestor().repaint();
	}

	protected boolean doesCommandAffectRowHeight(CommandExecutedEvent event)
	{
		if(!event.isSetDataCommand())
			return false;
		
		if(getMainWindow().isRowHeightModeManual())
			return false;
		
		CommandSetObjectData command = (CommandSetObjectData)event.getCommand();
		String tag = command.getFieldTag();
		if(tag.equals(BaseObject.TAG_ID) || tag.equals(BaseObject.TAG_LABEL))
			return true;
		return false;
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
		}
		
		@Override
		public void setPreferredSize(Dimension preferredSize)
		{
			overriddenPreferredSize = preferredSize;
		}
		
		@Override
		public Dimension getPreferredSize()
		{
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
	}

	public static class ScrollPaneNoExtraWidth extends ScrollPaneWithHideableScrollBar
	{
		public ScrollPaneNoExtraWidth(Component component)
		{
			super(component);
		}

		@Override
		public Dimension getMaximumSize()
		{
			Dimension max = super.getMaximumSize();
			max.width = getPreferredSize().width;
			return max;
		}
		
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

	private MainWindow mainWindow;
	protected TreeTableWithStateSaving tree;
	protected JPanel buttonBox;
	protected GenericTreeTableModel model;
	protected FastScrollPane treeTableScrollPane;
}
