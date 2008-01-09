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
	
	private void doProjectOpen(File file)
	{
		if(!isProjectDirectory(file))
			return;
		
		Cursor cursor = getMainWindow().getCursor();
		getMainWindow().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		try
		{
			getMainWindow().createOrOpenProject(file);
		}
		finally
		{
			getMainWindow().setCursor(cursor);
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
		menu.add(new ProjectListOpenAction(selectedFile));
		menu.addSeparator();
		menu.add(new ProjectListRenameAction(selectedFile)); 
		menu.add(new ProjectListCopyToAction(selectedFile));
		menu.add(new ProjectListExportAction(selectedFile));
		menu.addSeparator();
		menu.add(new ProjectListDeleteAction(selectedFile));
		return menu;
	}
	
	void refresh()
	{
		ProjectListTreeTableModel model = (ProjectListTreeTableModel)tree.getModel();
		model.rebuildEntireTree();
		repaint();
	}
	
	private boolean isProjectDirectory(File file)
	{
		return ProjectServer.isExistingProject(file);
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
	
	abstract class ProjectListAction extends AbstractAction
	{
		public ProjectListAction(String string, File selectedFile)
		{
			super(string);
			file = selectedFile;
		}
		
		File getFile()
		{
			return file;
		}
		
		private File file;
	}

	class ProjectListOpenAction extends ProjectListAction
	{
		public ProjectListOpenAction(File selectedFile)
		{
			super(EAM.text("Open"), selectedFile);
			setEnabled(isProjectDirectory(getFile()));
		}

		public void actionPerformed(ActionEvent event)
		{
			doProjectOpen(getFile());
		}
	}

	class ProjectListRenameAction extends ProjectListAction
	{
		public ProjectListRenameAction(File selectedFile)
		{
			super(EAM.text("Rename..."), selectedFile);
			setEnabled(isProjectDirectory(getFile()));
		}

		public void actionPerformed(ActionEvent event)
		{
			try
			{
				RenameProject.doIt(getMainWindow(), getFile());
			}
			catch(Exception e)
			{
				EAM.logException(e);
				EAM.errorDialog(EAM.text("Error renaming project: " + e.getMessage()));
			}
			refresh();
		}
	}

	class ProjectListCopyToAction extends ProjectListAction
	{
		public ProjectListCopyToAction(File selectedFile)
		{
			super(EAM.text("Copy To..."), selectedFile);
			setEnabled(isProjectDirectory(getFile()));
		}

		public void actionPerformed(ActionEvent event)
		{
			try
			{
				CopyProject.doIt(getMainWindow(), getFile());
			}
			catch(Exception e)
			{
				EAM.logException(e);
				EAM.errorDialog(EAM.text("Error copying project: " + e.getMessage()));
			}
			refresh();
		}
	}

	class ProjectListExportAction extends ProjectListAction
	{
		public ProjectListExportAction(File selectedFile)
		{
			super(EAM.text("Export..."), selectedFile);
			setEnabled(isProjectDirectory(getFile()));
		}

		public void actionPerformed(ActionEvent event)
		{
			try
			{
				ExportZippedProjectFileDoer.perform(getMainWindow(), getFile());
			}
			catch(CommandFailedException e)
			{
				EAM.logException(e);
				EAM.errorDialog(EAM.text("Error exporting project: " + e.getMessage()));
			}
		}
	}

	class ProjectListDeleteAction extends ProjectListAction
	{
		public ProjectListDeleteAction(File selectedFile)
		{
			super(EAM.text("Delete"), selectedFile);
			setEnabled(isProjectDirectory(getFile()));
		}

		public void actionPerformed(ActionEvent event)
		{
			try
			{
				DeleteProject.doIt(getMainWindow(), getFile());
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
