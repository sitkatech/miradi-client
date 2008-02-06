/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard.noproject.projectlist;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTreeTable;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.utils.MiradiResourceImageIcon;
import org.conservationmeasures.eam.wizard.noproject.FileSystemTreeNode;
import org.conservationmeasures.eam.wizard.noproject.NoProjectWizardStep;

public class ProjectListTreeTable extends PanelTreeTable
{
	public ProjectListTreeTable(ProjectListTreeTableModel treeTableModel, NoProjectWizardStep handlerToUse)
	{
		super(treeTableModel);
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);
		setRowSelectionAllowed(true);
		setColumnSelectionAllowed(false);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		addMouseListener(new MouseHandler());
		
		 DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer)tree.getCellRenderer();
		 renderer.setLeafIcon(new MiradiResourceImageIcon("icons/miradi16.png"));
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
		
		FileSystemTreeNode node = (FileSystemTreeNode)getObjectForRow(row);
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
		FileSystemTreeNode node = (FileSystemTreeNode)getObjectForRow(row);
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
}
