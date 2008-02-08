/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.conservationmeasures.eam.actions.ActionCloneStress;
import org.conservationmeasures.eam.actions.ActionCreateStress;
import org.conservationmeasures.eam.actions.ActionCreateStressFromKea;
import org.conservationmeasures.eam.actions.ActionDeleteStress;
import org.conservationmeasures.eam.actions.ActionHideCellRatings;
import org.conservationmeasures.eam.actions.ActionManageStresses;
import org.conservationmeasures.eam.actions.ActionShowCellRatings;
import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.commands.CommandSetThreatRating;
import org.conservationmeasures.eam.dialogs.threatstressrating.ThreatStressRatingManagementPanel;
import org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel.ThreatStressRatingMultiTablePanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.objects.RatingCriterion;
import org.conservationmeasures.eam.objects.ValueOption;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.SimpleThreatRatingFramework;
import org.conservationmeasures.eam.project.ThreatRatingBundle;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.ThreatRatingModeChoiceQuestion;
import org.conservationmeasures.eam.utils.BufferedImageFactory;
import org.conservationmeasures.eam.utils.MiradiScrollPane;
import org.conservationmeasures.eam.views.CardedView;
import org.conservationmeasures.eam.views.diagram.doers.CloneStressDoer;
import org.conservationmeasures.eam.views.diagram.doers.CreateStressDoer;
import org.conservationmeasures.eam.views.diagram.doers.CreateStressFromKeaDoer;
import org.conservationmeasures.eam.views.diagram.doers.DeleteStressDoer;
import org.conservationmeasures.eam.views.threatmatrix.doers.ManageStressesDoer;


public class ThreatMatrixView extends CardedView
{
	public ThreatMatrixView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		
		addThreatMatrixiewDoersToMap();
	}

	public JToolBar createToolBar()
	{
		return new ThreatMatrixToolBar(getMainWindow(), isCellRatingsVisible());
	}

	private boolean isCellRatingsVisible()
	{
		return getMainWindow().getBooleanPreference(AppPreferences.TAG_CELL_RATINGS_VISIBLE);
	}

	private void addThreatMatrixiewDoersToMap()
	{
		addDoerToMap(ActionShowCellRatings.class, new ShowCellRatingsDoer());
		addDoerToMap(ActionHideCellRatings.class, new HideCellRatingsDoer());
		addDoerToMap(ActionManageStresses.class, new ManageStressesDoer());
		
		addDoerToMap(ActionCreateStress.class, new CreateStressDoer());
		addDoerToMap(ActionDeleteStress.class, new DeleteStressDoer());
		addDoerToMap(ActionCloneStress.class, new CloneStressDoer());
		addDoerToMap(ActionCreateStressFromKea.class, new CreateStressFromKeaDoer());
	}
	
	public String cardName()
	{
		return getViewName();
	}

	static public String getViewName()
	{
		return Project.THREAT_MATRIX_VIEW_NAME;
	}
	
	public boolean isImageAvailable()
	{
		return true;
	}

	public BufferedImage getImage() throws Exception
	{
		if(isStressBasedMode())
			return createStressBasedImage();

		return MatrixTableImageCreator.createImage(getProject(), grid.getThreatMatrixTable(),grid.getRowHeaderTable());
	}

	private BufferedImage createStressBasedImage() throws Exception
	{
		ThreatStressRatingMultiTablePanel multiTablePanel = new ThreatStressRatingMultiTablePanel(getProject());

		BufferedImage image = BufferedImageFactory.createImageFromComponent(multiTablePanel);
		
		multiTablePanel.dispose();
		return image;
	}

	public void createCards() throws Exception
	{
		model = new ThreatMatrixTableModel(getProject());
		createThreatMatrixPanel();
		addCard(threatMatrixPanel, getThreatMatrixCardName());
		
		threatStressRatingManagementPanel = ThreatStressRatingManagementPanel.create(getMainWindow()); 
		addCard(threatStressRatingManagementPanel, getThreatStressRatingCardName());
	}
	
	protected void showCurrentCard(String code)
	{
		if (isStressBasedMode(code))
			showCard(getThreatStressRatingCardName());
		else
			showCard(getThreatMatrixCardName());
	}
	
	private boolean isStressBasedMode()
	{
		return isStressBasedMode(getCurrentCardChoiceName());
	}

	private boolean isStressBasedMode(String code)
	{
		return code.equals(ThreatRatingModeChoiceQuestion.STRESS_BASED_CODE);
	}
	
	public void deleteCards() throws Exception
	{
		threatStressRatingManagementPanel.dispose();
	}

	public void becomeActive() throws Exception
	{
		super.becomeActive();			
		selectBundle(null);
		grid.establishPriorSortState();
		threatStressRatingManagementPanel.updateSplitterLocation();
	}

	private void createThreatMatrixPanel() throws Exception
	{
		grid = new ThreatGridPanel(this, model);
		details = new ThreatRatingBundlePanel(this);
		
		threatMatrixPanel = new JPanel(new BorderLayout());
		threatMatrixPanel.add(new MiradiScrollPane(details), BorderLayout.AFTER_LINE_ENDS);
		threatMatrixPanel.add(grid, BorderLayout.CENTER); 
	}

	protected String getCurrentCardChoiceName()
	{
		return getProject().getMetadata().getData(ProjectMetadata.TAG_THREAT_RATING_MODE);
	}
	
	public void becomeInactive() throws Exception
	{
		// TODO: Should clear ALL view data
		grid = null;
		details.dispose();
		
		super.becomeInactive();
	}

	public ThreatMatrixTableModel getModel()
	{
		return model;
	}
	
	public SimpleThreatRatingFramework getThreatRatingFramework()
	{
		return getProject().getSimpleThreatRatingFramework();
	}
	
	public void selectBundle(ThreatRatingBundle bundle) throws Exception
	{
		details.selectBundle(bundle);
		grid.selectBundle(bundle);
		getMainWindow().getWizard().refresh();
		invalidate();
		validate();
	}
	
	public ThreatRatingBundle getBundle()
	{
		return details.getBundle();
	}
	
	public void setBundleValue(RatingCriterion criterion, ValueOption value) throws Exception
	{
		ThreatRatingBundle bundle = grid.getSelectedBundle();
		if(bundle == null)
			return;
		
		FactorId threatId = bundle.getThreatId();
		FactorId targetId = bundle.getTargetId();
		BaseId criterionId = criterion.getId();
		BaseId valueId = value.getId();
		setBundleValue(threatId, targetId, criterionId, valueId);
	}

	private void setBundleValue(FactorId threatId, FactorId targetId, BaseId criterionId, BaseId valueId) throws CommandFailedException
	{
		try
		{
			ThreatRatingBundle bundle = getThreatRatingFramework().getBundle(threatId, targetId);
			if(bundle.getValueId(criterionId).equals(valueId))
				return;
			CommandSetThreatRating cmd = new CommandSetThreatRating(threatId, targetId, criterionId, valueId);
			getProject().executeCommand(cmd);

		}
		catch(Exception e)
		{
			EAM.logException(e);
		}		
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		updateCardToShow(event);
		updateAfterCommand(event.getCommand());
	}

	private void updateCardToShow(CommandExecutedEvent event)
	{
		if (!event.isSetDataCommandWithThisTypeAndTag(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_THREAT_RATING_MODE))
			return;
		
		CommandSetObjectData command = (CommandSetObjectData) event.getCommand();
		String value = command.getDataValue();
		ChoiceItem choice = new ThreatRatingModeChoiceQuestion().findChoiceByCode(value);
		showCurrentCard(choice.getCode());
		getMainWindow().updateToolBar();
	}

	private String getThreatMatrixCardName()
	{
		return THREAT_MATRIX_CARD_NAME;
	}

	private String getThreatStressRatingCardName()
	{
		return threatStressRatingManagementPanel.getPanelDescription();
	}

	private void updateAfterCommand(Command rawCommand)
	{
		try
		{
			String commandName = rawCommand.getCommandName();
			if(commandName.equals(CommandCreateObject.COMMAND_NAME))
				updateAfterCreateOrUndo((CommandCreateObject)rawCommand);
			if(rawCommand.getCommandName().equals(CommandDeleteObject.COMMAND_NAME))
				updateAfterDeleteOrUndo((CommandDeleteObject)rawCommand);
			if(rawCommand.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
				updateSortIfNeeded((CommandSetObjectData)rawCommand);
		
			snapUiToExecutedCommand(rawCommand);
			if(grid != null)
			{
				selectBundle(grid.getSelectedBundle());
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	private void updateSortIfNeeded(CommandSetObjectData data) throws Exception
	{
		if(data.getObjectType() != ViewData.getObjectType())
			return;
		
		String tag = data.getFieldTag();
		if(tag.equals(ViewData.TAG_CURRENT_SORT_BY) || 
				data.getFieldTag().equals(ViewData.TAG_CURRENT_SORT_DIRECTION))
		{
			grid.establishPriorSortState();
		}
	}

	private void updateAfterCreateOrUndo(CommandCreateObject commandDoneOrUndone)
	{
		model.fireTableDataChanged();
	}
	
	private void updateAfterDeleteOrUndo(CommandDeleteObject commandDoneOrUndone)
	{
		model.fireTableDataChanged();
	}
	
	private void snapUiToExecutedCommand(Command executedCommand) throws Exception
	{
		if(executedCommand.getCommandName().equals(CommandSetThreatRating.COMMAND_NAME))
		{
			CommandSetThreatRating cmd = (CommandSetThreatRating)executedCommand;
			ThreatRatingBundle bundle = model.getBundle(cmd.getThreatId(), cmd.getTargetId());
			selectBundle(bundle);
		}
	}
	
	private static final String THREAT_MATRIX_CARD_NAME = "ThreatMatrix";

	private ThreatMatrixTableModel model;
	private ThreatGridPanel grid;
	private ThreatRatingBundlePanel details;
	private ThreatStressRatingManagementPanel threatStressRatingManagementPanel;
	private JPanel threatMatrixPanel;
}

