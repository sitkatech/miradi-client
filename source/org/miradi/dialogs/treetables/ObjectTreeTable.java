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
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.miradi.actions.ActionCollapseAllRows;
import org.miradi.actions.ActionExpandAllRows;
import org.miradi.actions.ActionTreeNodeDown;
import org.miradi.actions.ActionTreeNodeUp;
import org.miradi.actions.Actions;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objectpools.EAMObjectPool;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.views.umbrella.ObjectPicker;

import com.java.sun.jtreetable.TreeTableModel;
import com.java.sun.jtreetable.TreeTableModelAdapter;

abstract public class ObjectTreeTable extends TreeTableWithColumnWidthSaving implements ObjectPicker, RowColumnBaseObjectProvider
{
	public ObjectTreeTable(MainWindow mainWindowToUse, GenericTreeTableModel treeTableModelToUse)
	{
		super(mainWindowToUse, treeTableModelToUse);
		project = mainWindowToUse.getProject();
		selectionListeners = new Vector<ListSelectionListener>();

		setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		getTree().setShowsRootHandles(true);
		getTree().setRootVisible(false);
		getTree().setCellRenderer(new ObjectTreeCellRenderer(this));
		getTree().setEditable(false);
		getColumnModel().getColumn(0).setPreferredWidth(200);
		TableCellEditor ce = new NonEditableTreeTableCellEditor();
		setDefaultEditor(TreeTableModel.class, ce);
		if (getRowCount()>0)
			setRowSelectionInterval(0,0);
		
		final int CUSTOM_HEIGHT_TO_AVOID_ICON_CROPPING = getRowHeight() + 1;
		setRowHeight(CUSTOM_HEIGHT_TO_AVOID_ICON_CROPPING);
	}

	public Project getProject()
	{
		return project;
	}
	
	public int getProportionShares(int row)
	{
		return getNodeForRow(row).getProportionShares();
	}

	public boolean areBudgetValuesAllocated(int row)
	{
		return getNodeForRow(row).areBudgetValuesAllocated();
	}

	@Override
	public String getToolTipText(MouseEvent event)
	{
		Point at = new Point(event.getX(), event.getY());
		int row = rowAtPoint(at);
		TreeTableNode node = getNodeForRow(row);
		if(node == null)
			return null;

		BaseObject object = node.getObject();
		if(object == null)
			return null;
		
		String typeName = EAM.fieldLabel(object.getType(), object.getTypeName());
		String tooltip = "<html><b>" + typeName + "</b><br>";
		
		return tooltip + object.getFullName();
	}
	
	public static Font createFristLevelFont(Font defaultFontToUse)
	{
		Map map = defaultFontToUse.getAttributes();
	    map.put(TextAttribute.SIZE, new Float(defaultFontToUse.getSize2D() + 2));
	    map.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
	    Font customFont = new Font(map);
		return customFont;
	}
	
	class NonEditableTreeTableCellEditor extends TreeTableCellEditor
	{
		public NonEditableTreeTableCellEditor() 
		{
		    super();
		}
		
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int r, int c)
		{
			JTextField textField = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, r, c);
			textField.setEditable(false);
			return textField;
		}
	}
	
	public TreeTableNode[] getSelectedTreeNodes()
	{
		return new TreeTableNode[] {(TreeTableNode)getTree().getLastSelectedPathComponent()};
	}

	 //TODO: This method needs review as it seems a bit complex
	public BaseObject[] getSelectedObjects()
	{
		TreeTableNode selectedNode = (TreeTableNode)getTree().getLastSelectedPathComponent();
		
		if (selectedNode == null)
			return new BaseObject[0];
		
		ORef oRef = selectedNode.getObjectReference();
		EAMObjectPool pool = project.getPool(oRef.getObjectType());
		
		if (pool == null)
			return new BaseObject[0];
		
		BaseObject foundObject = pool.findObject(oRef.getObjectId());
		
		if (foundObject == null)
			return new BaseObject[0];
		
		return new BaseObject[] {foundObject};
	}
	
	public ORefList getSelectionHierarchy()
	{
		TreePath selectionPath = getTree().getSelectionModel().getSelectionPath();
		if (selectionPath == null)
			return new ORefList();
		
		ORefList selectionHierarchyNodeRefs = new ORefList();
		for(int i = selectionPath.getPathCount() - 1; i >=0 ; --i)
		{			
			TreeTableNode node = (TreeTableNode) selectionPath.getPathComponent(i);
			selectionHierarchyNodeRefs.add(node.getObjectReference());
		}
		
		return selectionHierarchyNodeRefs;
	}

	public ORefList[] getSelectedHierarchies()
	{
		TreePath[] selectionPaths = getTree().getSelectionModel().getSelectionPaths();
		if (selectionPaths == null)
			return new ORefList[] {new ORefList(getRootNodeRef())};
		
		ORefList[] selectionHierarchies = new ORefList[selectionPaths.length];
		for (int i = 0; i < selectionPaths.length; ++i)
		{
			selectionHierarchies[i] = convertPath(selectionPaths[i]);
		}
		
		return selectionHierarchies;
	}

	public boolean isActive()
	{
		return isActive;
	}
	
	public void becomeActive()
	{
		Actions actions = getMainWindow().getActions();
		for(Class actionClass : getRelevantActions())
		{
			actions.getObjectsAction(actionClass).addPicker(this);
		}
		isActive = true;
	}

	public void becomeInactive()
	{
		isActive = false;
		Actions actions = getMainWindow().getActions();
		for(Class actionClass : getRelevantActions())
		{
			actions.getObjectsAction(actionClass).removePicker(this);
		}
	}

	private ORef getRootNodeRef()
	{
		return getTreeTableModel().getRootNode().getObjectReference();
	}
	
	private ORefList convertPath(TreePath treePath)
	{
		ORefList selectionHierarchyNodeRefs = new ORefList();
		for(int i = treePath.getPathCount() - 1; i >=0 ; --i)
		{			
			TreeTableNode node = (TreeTableNode) treePath.getPathComponent(i);
			selectionHierarchyNodeRefs.add(node.getObjectReference());
		}
		
		return selectionHierarchyNodeRefs;	
	}

	public void ensureObjectVisible(ORef ref)
	{
		// NOTE: This code hasn't been proven to work...we believe it needs to be called
		// from inside invokeLater and that it will work if we do that
		// NOTE: This code might not even be needed any more, because 
		// selecting an object now automatically also scrolls it to be visible
		TreePath path = getTreeTableModel().findObject(getTreeTableModel().getPathToRoot(), ref);
		getTree().scrollPathToVisible(path);
	}

	public void addSelectionChangeListener(ListSelectionListener listener)
	{
		selectionListeners.add(listener);
	}

	public void removeSelectionChangeListener(ListSelectionListener listener)
	{
		selectionListeners.remove(listener);
	}
	
	public void valueChanged(ListSelectionEvent e)
	{
		super.valueChanged(e);
		
		if(selectionListeners == null)
			return;
		
		for(int i = 0; i < selectionListeners.size(); ++i)
		{
			ListSelectionListener listener = selectionListeners.get(i);
			listener.valueChanged(null);
		}
	}

	public TreeTableModelAdapter getTreeTableAdapter()
	{
		return (TreeTableModelAdapter)getModel();
	}
	
	public TreeTableNode getNodeForRow(int row)
	{
		return (TreeTableNode)getTreeTableAdapter().nodeForRow(row);
	}
	
	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return getNodeForRow(row).getObject();
	}
	
	public void selectObject(ORef ref, int fallbackRow)
	{
		TreePath path = getTreeTableModel().getPathOfNode(ref.getObjectType(), ref.getObjectId());
		if(path == null)
		{
			getSelectionModel().setSelectionInterval(fallbackRow, fallbackRow);
			return;
		}
		
		tree.setSelectionPath(path);
	}

	public void selectObjectAfterSwingClearsItDueToTreeStructureChange(ORef selectedRef, int fallbackRow)
	{
		clearSelection();
		tree.clearSelection();
		if(selectedRef == null || selectedRef.isInvalid())
			return;
		
		SwingUtilities.invokeLater(new Reselecter(this, selectedRef, fallbackRow));
	}
	
	static class Reselecter implements Runnable
	{
		public Reselecter(ObjectTreeTable treeTableToUse, ORef refToSelect, int rowToSelect)
		{
			treeTable = treeTableToUse;
			ref = refToSelect;
			row = rowToSelect;
		}
		
		public void run()
		{
			treeTable.selectObject(ref, row);
			treeTable.ensureSelectedRowVisible();
		}
		
		private ObjectTreeTable treeTable;
		private ORef ref;
		private int row;
	}

	protected Set<Class> getRelevantActions()
	{
		HashSet<Class> set = new HashSet<Class>();
		set.add(ActionCollapseAllRows.class);
		set.add(ActionExpandAllRows.class);
		set.add(ActionTreeNodeUp.class);
		set.add(ActionTreeNodeDown.class);
		return set;
	}

	protected Project project;
	private Vector<ListSelectionListener> selectionListeners;
	private boolean isActive;
}
