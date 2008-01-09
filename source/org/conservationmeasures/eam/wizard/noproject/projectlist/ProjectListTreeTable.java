/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard.noproject.projectlist;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;

import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTreeTable;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.noproject.CopyProject;
import org.conservationmeasures.eam.views.noproject.DeleteProject;
import org.conservationmeasures.eam.views.noproject.RenameProject;
import org.conservationmeasures.eam.views.umbrella.ExportZippedProjectFileDoer;
import org.conservationmeasures.eam.wizard.noproject.FileSystemTreeNode;
import org.conservationmeasures.eam.wizard.noproject.NoProjectWizardStep;

public class ProjectListTreeTable extends PanelTreeTable
{
	public ProjectListTreeTable(ProjectListTreeTableModel treeTableModel, NoProjectWizardStep handlerToUse)
	{
		super(treeTableModel);
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		addMouseListener(new MouseHandler());
	}

	public String getUniqueTableIdentifier()
	{
		return EAM.text("Project List");
	}
	
	private void doProjectOpen(Point point)
	{
		int row = rowAtPoint(point);
		if(row < 0 || row > getRowCount())
			return;
		
		FileSystemTreeNode node = (FileSystemTreeNode)getObjectForRow(row);
		File file = node.getFile();
		doProjectOpen(file);
	}
	
	public static boolean isProjectDirectory(File file)
	{
		return ProjectServer.isExistingProject(file);
	}

	public static void doProjectOpen(File file)
	{
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
		menu.add(new ProjectListOpenAction(this, selectedFile));
		menu.addSeparator();
		menu.add(new ProjectListRenameAction(this, selectedFile)); 
		menu.add(new ProjectListCopyToAction(this, selectedFile));
		menu.add(new ProjectListExportAction(this, selectedFile));
		menu.addSeparator();
		menu.add(new ProjectListDeleteAction(this, selectedFile));
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
				doProjectOpen(e.getPoint());
		}
	}
	
	abstract static class ProjectListAction extends AbstractAction
	{
		public ProjectListAction(ProjectListTreeTable tableToUse, String string, File selectedFile)
		{
			super(string);
			table = tableToUse;
			thisFile = selectedFile;
		}

		File getFile()
		{
			return thisFile;
		}
		
		void refresh()
		{
			table.refresh();
		}
		
		private ProjectListTreeTable table;
		private File thisFile;
	}

	static class ProjectListOpenAction extends ProjectListAction
	{
		public ProjectListOpenAction(ProjectListTreeTable tableToUse, File selectedFile)
		{
			super(tableToUse, EAM.text("Open"), selectedFile);
			setEnabled(isProjectDirectory(getFile()));
		}

		public void actionPerformed(ActionEvent event)
		{
			doProjectOpen(getFile());
		}
	}

	static class ProjectListRenameAction extends ProjectListAction
	{
		public ProjectListRenameAction(ProjectListTreeTable tableToUse, File selectedFile)
		{
			super(tableToUse, EAM.text("Rename..."), selectedFile);
			setEnabled(isProjectDirectory(getFile()));
		}

		public void actionPerformed(ActionEvent event)
		{
			try
			{
				RenameProject.doIt(EAM.getMainWindow(), getFile());
			}
			catch(Exception e)
			{
				EAM.logException(e);
				EAM.errorDialog(EAM.text("Error renaming project: " + e.getMessage()));
			}
			refresh();
		}
	}

	static class ProjectListCopyToAction extends ProjectListAction
	{
		public ProjectListCopyToAction(ProjectListTreeTable tableToUse, File selectedFile)
		{
			super(tableToUse, EAM.text("Copy To..."), selectedFile);
			setEnabled(isProjectDirectory(getFile()));
		}

		public void actionPerformed(ActionEvent event)
		{
			try
			{
				CopyProject.doIt(EAM.getMainWindow(), getFile());
			}
			catch(Exception e)
			{
				EAM.logException(e);
				EAM.errorDialog(EAM.text("Error copying project: " + e.getMessage()));
			}
			refresh();
		}
	}

	static class ProjectListExportAction extends ProjectListAction
	{
		public ProjectListExportAction(ProjectListTreeTable tableToUse, File selectedFile)
		{
			super(tableToUse, EAM.text("Export..."), selectedFile);
			setEnabled(isProjectDirectory(getFile()));
		}

		public void actionPerformed(ActionEvent event)
		{
			try
			{
				ExportZippedProjectFileDoer.perform(EAM.getMainWindow(), getFile());
			}
			catch(CommandFailedException e)
			{
				EAM.logException(e);
				EAM.errorDialog(EAM.text("Error exporting project: " + e.getMessage()));
			}
		}
	}

	static class ProjectListDeleteAction extends ProjectListAction
	{
		public ProjectListDeleteAction(ProjectListTreeTable tableToUse, File selectedFile)
		{
			super(tableToUse, EAM.text("Delete"), selectedFile);
			setEnabled(isProjectDirectory(getFile()));
		}

		public void actionPerformed(ActionEvent event)
		{
			try
			{
				DeleteProject.doIt(EAM.getMainWindow(), getFile());
			}
			catch(Exception e)
			{
				EAM.logException(e);
				EAM.errorDialog(EAM.text("Error deleting project: " + e.getMessage()));
			}
			refresh();
		}
	}
}
