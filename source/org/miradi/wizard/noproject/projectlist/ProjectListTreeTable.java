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
package org.miradi.wizard.noproject.projectlist;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.miradi.database.ProjectServer;
import org.miradi.dialogs.tablerenderers.BorderlessTableCellRendererFactory;
import org.miradi.dialogs.tablerenderers.DefaultTableCellRendererWithPreferredHeightFactory;
import org.miradi.dialogs.treetables.TreeTableWithColumnWidthSaving;
import org.miradi.dialogs.treetables.TreeTableWithRowHeightSaver;
import org.miradi.dialogs.treetables.VariableHeightTreeCellRenderer;
import org.miradi.icons.FolderIcon;
import org.miradi.icons.MiradiApplicationIcon;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.utils.ColumnSortListener;
import org.miradi.utils.SortableTable;
import org.miradi.wizard.noproject.FileSystemTreeNode;
import org.miradi.wizard.noproject.NoProjectWizardStep;

import com.java.sun.jtreetable.TreeTableModel;

public class ProjectListTreeTable extends TreeTableWithColumnWidthSaving implements SortableTable
{
	public ProjectListTreeTable(MainWindow mainWindowToUse, ProjectListTreeTableModel treeTableModel, NoProjectWizardStep handlerToUse)
	{
		super(mainWindowToUse, treeTableModel);
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);
		Renderer renderer = new Renderer(this);
		tree.setCellRenderer(renderer);
		getTree().setEditable(false);
		getColumnModel().getColumn(0).setPreferredWidth(200);
		TableCellEditor ce = new NonEditableTreeTableCellEditor();
		setDefaultEditor(TreeTableModel.class, ce);
		setBackground(AppPreferences.getDataPanelBackgroundColor());
		setRowSelectionAllowed(true);
		setColumnSelectionAllowed(false);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		addMouseListener(new MouseHandler());
		addColumnSorter();
		dateRenderer = new BorderlessTableCellRendererFactory();
	}

	private void addColumnSorter()
	{
		JTableHeader columnHeader = getTableHeader();
		ColumnSortListener sortListener = new ColumnSortListener(this);
		columnHeader.addMouseListener(sortListener);
	}

	@Override
	public boolean shouldSaveRowHeight()
	{
		return false;
	}
	
	@Override
	public boolean shouldSaveColumnSequence()
	{
		return false;
	}
	
	@Override
	public boolean shouldSaveColumnWidth()
	{
		return false;
	}
	
	public String getUniqueTableIdentifier()
	{
		return EAM.text("Project List");
	}
	
	public File getSelectedFile()
	{
		int row = getSelectedRow();
		if(row < 0)
			return null;
		
		FileSystemTreeNode node = (FileSystemTreeNode)getRawObjectForRow(row);
		File file = node.getFile();
		return file;
	}
	
	public static boolean isProjectDirectory(File file)
	{
		return ProjectServer.isExistingProject(file);
	}

	public static void doProjectOpen(File file)
	{
		if(file == null)
			return;
		
		if(!isProjectDirectory(file))
			return;
		
		MainWindow mainWindow = EAM.getMainWindow();
		Cursor cursor = mainWindow.getCursor();
		mainWindow.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		try
		{
			mainWindow.createOrOpenProject(file);
		}
		finally
		{
			mainWindow.setCursor(cursor);
		}
	}
	
	@Override
	public TableCellRenderer getCellRenderer(int row, int column)
	{
		if(column == 0)
			return super.getCellRenderer(row, column);
		
		dateRenderer.setCellBackgroundColor(getBackground());
		return dateRenderer;
	}
	
	private void doContextMenu(Point point)
	{
		int row = rowAtPoint(point);
		if(row < 0 || row > getRowCount())
			return;
		
		getSelectionModel().setSelectionInterval(row, row);
		FileSystemTreeNode node = (FileSystemTreeNode)getRawObjectForRow(row);
		JPopupMenu menu = getRightClickMenu(node.getFile());
		menu.show(this, point.x, point.y);

	}

	public JPopupMenu getRightClickMenu(File selectedFile)
	{
		JPopupMenu menu = new JPopupMenu();
		menu.add(new ProjectListOpenAction(this));
		menu.addSeparator();
		menu.add(new ProjectListRenameAction(this)); 
		menu.add(new ProjectListSaveAsAction(this));
		menu.add(new ProjectListExportAction(this));
		menu.addSeparator();
		menu.add(new ProjectListDeleteAction(this));
		menu.add(new ProjectListCreateDirectory(this));
		return menu;
	}
	
	void refresh()
	{
		ProjectListTreeTableModel model = getProjectListTreeTableModel();
		model.rebuildEntireTree();
		repaint();
	}

	private ProjectListTreeTableModel getProjectListTreeTableModel()
	{
		return (ProjectListTreeTableModel) getTreeTableModel();
	}
	
	public void sort(int sortByTableColumn)
	{	
		int modelColumn = convertColumnIndexToModel(sortByTableColumn);
		getProjectListTreeTableModel().sort(modelColumn);
	}
	
	class MouseHandler extends MouseAdapter
	{
		public void mousePressed(MouseEvent e)
		{
			super.mousePressed(e);
			if(e.isPopupTrigger())
				doContextMenu(e.getPoint());
		}
		
		public void mouseReleased(MouseEvent e)
		{
			super.mouseReleased(e);
			if(e.isPopupTrigger())
				doContextMenu(e.getPoint());
		}
		
		public void mouseClicked(MouseEvent e)
		{
			super.mouseClicked(e);
			if(e.getClickCount() == 2)
				doProjectOpen(getSelectedFile());
		}
	}
	
	static class ProjectListItemRenderer extends VariableHeightTreeCellRenderer
	{
		public ProjectListItemRenderer(TreeTableWithRowHeightSaver treeTableToUse)
		{
			super(treeTableToUse);
			setBackgroundNonSelectionColor(AppPreferences.getDataPanelBackgroundColor());
		}
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
	
	public static class Renderer extends VariableHeightTreeCellRenderer
	{		
		public Renderer(TreeTableWithRowHeightSaver treeTable)
		{	
			super(treeTable);
			folderRenderer = new ProjectListItemRenderer(treeTable);
			folderRenderer.setClosedIcon(new FolderIcon());
			folderRenderer.setOpenIcon(new FolderIcon());
			folderRenderer.setLeafIcon(new FolderIcon());

			projectRenderer = new ProjectListItemRenderer(treeTable);
			projectRenderer.setClosedIcon(new MiradiApplicationIcon());
			projectRenderer.setOpenIcon(new MiradiApplicationIcon());
			projectRenderer.setLeafIcon(new MiradiApplicationIcon());
		}

		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocusToUse)
		{
			VariableHeightTreeCellRenderer renderer = null;
			
			FileSystemTreeNode node = (FileSystemTreeNode) value;
			if(node.isProjectDirectory())
				renderer = projectRenderer;
			else
				renderer = folderRenderer;
			
			JComponent configuredRenderer = (JComponent)renderer.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocusToUse);
			return configuredRenderer;
		}
		

		private	VariableHeightTreeCellRenderer folderRenderer;
		private VariableHeightTreeCellRenderer projectRenderer;
	}

	DefaultTableCellRendererWithPreferredHeightFactory dateRenderer;
}
