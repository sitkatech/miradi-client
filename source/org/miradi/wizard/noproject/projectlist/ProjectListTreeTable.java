/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
import javax.swing.table.TableCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.miradi.database.ProjectServer;
import org.miradi.dialogs.fieldComponents.PanelTreeTable;
import org.miradi.icons.FolderIcon;
import org.miradi.icons.MiradiApplicationIcon;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.wizard.noproject.FileSystemTreeNode;
import org.miradi.wizard.noproject.NoProjectWizardStep;

import com.java.sun.jtreetable.TreeTableModel;

public class ProjectListTreeTable extends PanelTreeTable
{
	public ProjectListTreeTable(ProjectListTreeTableModel treeTableModel, NoProjectWizardStep handlerToUse)
	{
		super(treeTableModel);
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);
		Renderer renderer = new Renderer();
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
		menu.add(new ProjectListCopyToAction(this));
		menu.add(new ProjectListExportAction(this));
		menu.addSeparator();
		menu.add(new ProjectListDeleteAction(this));
		return menu;
	}
	
	void refresh()
	{
		ProjectListTreeTableModel model = (ProjectListTreeTableModel)tree.getModel();
		model.rebuildEntireTree();
		repaint();
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
	
	static class ProjectListItemRenderer extends DefaultTreeCellRenderer
	{
		public ProjectListItemRenderer()
		{
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
	
	public static class Renderer extends DefaultTreeCellRenderer
	{		
		public Renderer()
		{	
			folderRenderer = new ProjectListItemRenderer();
			folderRenderer.setClosedIcon(new FolderIcon());
			folderRenderer.setOpenIcon(new FolderIcon());
			folderRenderer.setLeafIcon(new FolderIcon());

			projectRenderer = new ProjectListItemRenderer();
			projectRenderer.setClosedIcon(new MiradiApplicationIcon());
			projectRenderer.setOpenIcon(new MiradiApplicationIcon());
			projectRenderer.setLeafIcon(new MiradiApplicationIcon());
		}

		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocusToUse)
		{
			DefaultTreeCellRenderer renderer = null;
			
			FileSystemTreeNode node = (FileSystemTreeNode) value;
			if(node.isProjectDirectory())
				renderer = projectRenderer;
			else
				renderer = folderRenderer;
			
			JComponent configuredRenderer = (JComponent)renderer.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			return configuredRenderer;
		}
		

		private	DefaultTreeCellRenderer folderRenderer;
		private DefaultTreeCellRenderer projectRenderer;
	}

}
