/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.Box;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.dialogs.base.EAMDialog;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelButton;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTitleLabel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiButton;
import org.martus.swing.UiList;
import org.martus.swing.UiParagraphPanel;
import org.martus.swing.UiScrollPane;
import org.martus.swing.UiTextField;
import org.martus.swing.Utilities;
import org.martus.util.DirectoryUtils;

public class CreateProjectDialog extends EAMDialog implements ActionListener,
		ListSelectionListener
{
	public CreateProjectDialog(MainWindow parent) throws HeadlessException
	{
		super(parent);
		setModal(true);
		setResizable(true);

		projectFilenameField = createTextArea();
		existingProjectList = createExistingProjectList();

		UiParagraphPanel panel = new UiParagraphPanel();
		panel.addOnNewLine(new PanelTitleLabel("<HTML>This command saves a copy of your project under a new filename. <BR>" +
												  "You are still in your original file - to switch to the copy with the new filename, <BR>" +
												  "you have to close this project and open the new one.<HTML>"));
		
		panel.addOnNewLine(new PanelTitleLabel(EAM.getHomeDirectory().getAbsolutePath()));
		UiScrollPane uiScrollPane = new UiScrollPane(existingProjectList);
		uiScrollPane.setPreferredSize(new Dimension(projectFilenameField.getPreferredSize().width, 200));
		panel.addComponents(new PanelTitleLabel(EAM.text("Label|Existing Projects:")), uiScrollPane);
		panel.addComponents(new PanelTitleLabel(EAM.text("New Project Filename: ")), projectFilenameField);
		panel.addOnNewLine(createButtonBar());
		getContentPane().add(panel);

		getRootPane().setDefaultButton(okButton);
	}

	public boolean showCreateDialog(String buttonLabel)
	{
		return showCreateDialog(EAM.text("Title|Create New Project"), buttonLabel);
	}

	public boolean showCreateDialog(String title, String buttonLabel)
	{
		setTitle(title);
		okButton.setText(buttonLabel);
		return showDialog();
	}

	private boolean showDialog()
	{
		pack();
		setVisible(true);
		return getResult();
	}

	public File getSelectedFile()
	{
		return new File(EAM.getHomeDirectory(), projectFilenameField.getText());
	}

	String getSelectedFilename()
	{
		return projectFilenameField.getText();
	}

	private UiList createExistingProjectList()
	{
		File home = EAM.getHomeDirectory();
		UiList list = new UiList(home.list(new DirectoryFilter()));
		list.addListSelectionListener(this);
		list.addMouseListener(new DoubleClickHandler(this));
		return list;
	}

	static class DoubleClickHandler extends MouseAdapter
	{
		public DoubleClickHandler(CreateProjectDialog dialogToControl)
		{
			dialog = dialogToControl;
		}

		public void mouseClicked(MouseEvent e)
		{
			if(e.getClickCount() != 2)
				return;

			dialog.ok();
		}

		CreateProjectDialog dialog;
	}

	public static class DirectoryFilter implements FilenameFilter
	{
		public boolean accept(File eamDataDirectory, String projectDirectoryName)
		{
			if(projectDirectoryName.startsWith("."))
				return false;

			File projectDirectory = new File(eamDataDirectory,
					projectDirectoryName);
			return ProjectServer.isExistingProject(projectDirectory);
		}
	}

	private UiTextField createTextArea()
	{
		UiTextField textField = new UiTextField(40);
		textField.requestFocus(true);
		textField.selectAll();
		textField.getDocument().addDocumentListener(new TextFieldListener());
		return textField;
	}

	class TextFieldListener implements DocumentListener
	{
		public void changedUpdate(DocumentEvent e)
		{
			enableOkButtonIfNotEmpty();
		}

		public void insertUpdate(DocumentEvent e)
		{
			enableOkButtonIfNotEmpty();
		}

		public void removeUpdate(DocumentEvent e)
		{
			enableOkButtonIfNotEmpty();
		}

		public void enableOkButtonIfNotEmpty()
		{
			boolean isNotEmpty = (projectFilenameField.getText().length() > 0);
			okButton.setEnabled(isNotEmpty);
		}
	}

	private Box createButtonBar()
	{
		okButton = new PanelButton("");
		okButton.addActionListener(this);
		okButton.setEnabled(false);
		getRootPane().setDefaultButton(okButton);
		cancelButton = new PanelButton(EAM.text("Button|Cancel"));
		cancelButton.addActionListener(this);

		Box buttonBar = Box.createHorizontalBox();
		Component[] components = new Component[] { Box.createHorizontalGlue(),
				okButton, cancelButton };
		Utilities.addComponentsRespectingOrientation(buttonBar, components);
		return buttonBar;
	}

	public boolean getResult()
	{
		return result;
	}

	public void actionPerformed(ActionEvent event)
	{
		if(event.getSource() == okButton)
			ok();
		else
			cancel();
	}

	public void ok()
	{
		if(!Project.isValidProjectFilename(getSelectedFilename()))
		{
			String body = EAM
					.text("Project filenames cannot contain punctuation other than dots, dashes, and spaces; and they cannot be longer than 32 characters. ");
			EAM.errorDialog(body);
			return;
		}

		if(getSelectedFile().exists())
		{
			Project project = ((MainWindow)getOwner()).getProject();
			if(project.isOpen()
					&& getSelectedFilename().equals(project.getFilename()))
			{
				String body = EAM.text("Cannot overwrite an open project");
				EAM.errorDialog(body);
				return;
			}

			if (!ProjectServer.isExistingProject(getSelectedFile())) 
			{
				String body = EAM.text("File exists: Cannot overwrite a non project directory");
				EAM.errorDialog(body);
				return;
			}
			
			String title = EAM.text("Title|Overwrite existing file?");
			String[] body = { EAM.text("This will replace the existing file.") };
			if(!EAM.confirmDialog(title, body))
				return;
			DirectoryUtils.deleteEntireDirectoryTree(getSelectedFile());
		}

		result = true;
		dispose();

	}

	public void cancel()
	{
		result = false;
		dispose();
	}

	public void valueChanged(ListSelectionEvent arg0)
	{
		projectFilenameField.setText((String) existingProjectList
				.getSelectedValue());
	}

	boolean result;

	UiList existingProjectList;

	UiTextField projectFilenameField;

	UiButton okButton;

	UiButton cancelButton;
}
