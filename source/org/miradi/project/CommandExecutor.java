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

package org.miradi.project;

import java.io.PrintStream;
import java.util.Collections;
import java.util.Vector;

import org.miradi.commands.Command;
import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandDeleteObject;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.commands.CommandSetThreatRating;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.exceptions.NothingToRedoException;
import org.miradi.exceptions.NothingToUndoException;
import org.miradi.exceptions.UnableToBeginTransactionException;
import org.miradi.exceptions.UnexpectedNonSideEffectException;
import org.miradi.exceptions.UnexpectedSideEffectException;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.objects.HumanWelfareTarget;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.Target;
import org.miradi.objects.ViewData;
import org.miradi.utils.CommandVector;

public class CommandExecutor
{
	public CommandExecutor(Project projectToUse)
	{
		project = projectToUse;
		commandExecutedListeners = new Vector<CommandExecutedListener>();
		sideEffectExecutor = new SideEffectExecutor();
		normalExecutor = new NormalExecutor();
		
		clear();
	}
	
	public void clear()
	{
		undoRedoState = new UndoRedoState();
	}
	
	public void executeCommand(Command command) throws UnexpectedNonSideEffectException, CommandFailedException
	{
		if(!isTransactionStateValidFor(command))
			throw new CommandFailedException("executeCommand bad transaction state: " + command.toString());
		try
		{
			getNormalExecutor().executeCommand(command);
			if(!isInTransaction())
				transactionOrSingleCommandHasEnded();
		}
		catch (UnableToBeginTransactionException e)
		{
			EAM.panic(e);
		}
	}

	private void transactionOrSingleCommandHasEnded()
	{
		// NOTE: Eventually data will not be written during transactions,
		// and will be written here instead
	}

	private boolean isTransactionStateValidFor(Command command)
	{
		if(command.isBeginTransaction())
			return !isInTransaction();
		if(command.isEndTransaction())
			return isInTransaction();
		
		return true;
	}

	private boolean shouldUpdateLastModfiedTime(Command command)
	{
		if (command.getCommandName().equals(CommandDeleteObject.COMMAND_NAME))
			return true;
		
		if (command.getCommandName().equals(CommandCreateObject.COMMAND_NAME))
			return true;
		
		if (command.getCommandName().equals(CommandSetThreatRating.COMMAND_NAME))
			return true;
		
		if (!command.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
			return false;

		CommandSetObjectData setCommand = ((CommandSetObjectData) command);
		BaseObject baseObject = BaseObject.find(getProject(), setCommand.getObjectORef());
		
		return !baseObject.isPresentationDataField(setCommand.getFieldTag());
	}
	
	public void executeEndTransaction() throws CommandFailedException
	{
		executeCommand(new CommandEndTransaction());
	}
	
	public void executeBeginTransaction() throws CommandFailedException
	{
		executeCommand(new CommandBeginTransaction());
	}
	
	public void executeCommandsWithoutTransaction(Command[] commands) throws CommandFailedException
	{
		for(int i = 0; i < commands.length; ++i)
		{
			executeCommand(commands[i]);
		}
	}

	public void executeCommandsWithoutTransaction(CommandVector commands) throws CommandFailedException
	{
		executeCommandsWithoutTransaction(commands.toArray(new Command[0]));
	}
	
	public void executeCommandAsTransaction(Command command) throws CommandFailedException
	{
		CommandVector singleItemList = new CommandVector();
		singleItemList.add(command);
		executeCommandsAsTransaction(singleItemList);
	}
	
	public void executeCommandsAsTransaction(CommandVector commands) throws CommandFailedException
	{
		executeCommandsAsTransaction(commands.toArray(new Command[0]));
	}
	
	public void executeCommandsAsTransaction(Command[] commands) throws CommandFailedException
	{
		executeCommand(new CommandBeginTransaction());
		try
		{
			executeCommandsWithoutTransaction(commands);
		}
		finally
		{
			executeCommand(new CommandEndTransaction());
		}
	}
	
	public void undo() throws CommandFailedException, RuntimeException
	{
		EAM.logVerbose("Performing undo");
		Command undone = undoOneCommand();
		if(undone.isEndTransaction())
		{
			while(!undone.isBeginTransaction())
				undone = undoOneCommand();
		}
		transactionOrSingleCommandHasEnded();
	}

	private Command undoOneCommand() throws NothingToUndoException,
			CommandFailedException
	{
		Command cmd = getUndoRedoState().popCommandToUndo();
		try
		{
			enableIsExecuting();
			executeWithoutRecording(cmd.getReverseCommand());
			enterSideEffectModeAndFireCommandExecuted(cmd.getReverseCommand());
			return cmd;
		}
		finally
		{
			disableIsExecuting();
		}
	}

	public void redo() throws CommandFailedException, RuntimeException
	{
		EAM.logVerbose("Performing redo");
		Command redone = redoOneCommand();
		if(redone.isBeginTransaction())
		{
			while(!redone.isEndTransaction())
				redone = redoOneCommand();
		}
		transactionOrSingleCommandHasEnded();
	}

	private Command redoOneCommand() throws NothingToRedoException,
			CommandFailedException
	{
		Command cmd = getUndoRedoState().popCommandToRedo();
		try
		{
			EAM.logVerbose("Redoing: " + cmd.toString());
			enableIsExecuting();
			executeWithoutRecording(cmd);
			enterSideEffectModeAndFireCommandExecuted(cmd);
			return cmd;
		}
		finally
		{
			disableIsExecuting();
		}
	}

	protected void executeWithoutRecording(Command command) throws CommandFailedException
	{
		try 
		{
			EAM.logVerbose("Executing: " + command.toString());
			command.executeAndLog(getProject());
			EAM.logVerbose("Finished : " + command.toString());
		} 
		catch (CommandFailedException e) 
		{
			throw(e);
		}
	}
	
	public void executeAsSideEffect(CommandVector commands) throws CommandFailedException
	{
		for (int index = 0; index < commands.size(); ++index)
		{
			executeAsSideEffect(commands.get(index));
		}
	}
	
	public void executeAsSideEffect(Command command) throws UnexpectedSideEffectException, CommandFailedException
	{
		try
		{
			sideEffectExecutor.executeAsSideEffect(command);
		}
		catch (UnableToBeginTransactionException e)
		{
			EAM.panic(e);
		}
	}
	
	public void recordCommand(Command command)
	{
		Command lastCommand = getLastExecutedCommand();		
		try
		{
			if(command.isEndTransaction() && lastCommand != null && lastCommand.isBeginTransaction())
			{
				getUndoRedoState().discardLastUndoableCommand();
			}
			else
			{
				getUndoRedoState().pushUndoableCommand(command);
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	public Command getLastExecutedCommand()
	{
		return getUndoRedoState().getLastRecordedCommand();
	}
	
	public boolean isExecutingACommand()
	{
		return isExecuting;
	}
	
	private void enableIsExecuting()
	{
		isExecuting = true;
	}
	
	private void disableIsExecuting()
	{
		isExecuting = false;
	}

	public void addCommandExecutedListener(CommandExecutedListener listener)
	{
		if(commandExecutedListeners.contains(listener))
			throw new RuntimeException("Attempted to add listener twice: " + listener.getClass());
		EAM.logVerbose("addCommandExecutedListener: " + listener.getClass());
		commandExecutedListeners.add(listener);
	}
	
	public void removeCommandExecutedListener(CommandExecutedListener listener)
	{
		EAM.logVerbose("removeCommandExecutedListener: " + listener.getClass());
		if(!commandExecutedListeners.contains(listener))
			EAM.logWarning("removeCommandExecutedListener not in list: " + listener.getClass());
		commandExecutedListeners.remove(listener);
	}

	private void enterSideEffectModeAndFireCommandExecuted(Command command)
	{
		beginCommandSideEffectMode();
		try
		{
			fireCommandExecuted(command);
		}
		finally
		{
			endCommandSideEffectMode();
		}
	}

	public void fireCommandExecuted(Command command)
	{
		EAM.logVerbose("fireCommandExecuted: " + command.toString());
		CommandExecutedEvent event = new CommandExecutedEvent(command);
		Vector<CommandExecutedListener> copyForComparison = new Vector<CommandExecutedListener>(commandExecutedListeners);
		for(int i=0; i < getCommandListenerCount(); ++i)
		{
			CommandExecutedListener listener = commandExecutedListeners.get(i);
			listener.commandExecuted(event);
		}
		
		if (haveListenersChanged(command, commandExecutedListeners, copyForComparison) && !canBeListenerChangingCommand(command))
			EAM.logError("Command Listener list was changed during fireCommandExecuted");
	}
	
	private boolean haveListenersChanged(final Command command, final Vector<CommandExecutedListener> currentListenersList, final Vector<CommandExecutedListener> copyForComparison)
	{
		Vector<String> originalList = extractClassNames(currentListenersList);
		Collections.sort(originalList);
		
		Vector<String> copy = extractClassNames(copyForComparison);
		Collections.sort(copy);
		
		return !copy.equals(originalList);
	}
	
	private Vector<String> extractClassNames(Vector<CommandExecutedListener> listToGetClassNamesFrom)
	{
		Vector<String> classNames = new Vector<String>();
		for(CommandExecutedListener listener : listToGetClassNamesFrom)
		{
			classNames.add(listener.getClass().getName());
		}
		
		return classNames;
	}

	private boolean canBeListenerChangingCommand(Command command)
	{
		if (!command.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
			return false;
		
		CommandSetObjectData setCommand = (CommandSetObjectData) command;
		if (setCommand.isRefAndTag(getProject().getMetadata().getRef(), ProjectMetadata.TAG_CURRENT_WIZARD_SCREEN_NAME))
			return true;
		
		if (setCommand.isTypeAndTag(HumanWelfareTarget.getObjectType(), HumanWelfareTarget.TAG_VIABILITY_MODE))
			return true;
		
		if (setCommand.isTypeAndTag(Target.getObjectType(), Target.TAG_VIABILITY_MODE))
			return true;
				
		if (setCommand.isTypeAndTag(ViewData.getObjectType(), ViewData.TAG_CURRENT_TAB))
			return true;
				
		return false;
	}
	
	public int getCommandListenerCount()
	{
		return commandExecutedListeners.size();
	}
	
	public void logCommandListeners(PrintStream out)
	{
		for(int i=0; i < getCommandListenerCount(); ++i)
		{
			CommandExecutedListener listener = commandExecutedListeners.get(i);
			out.println(listener.getClass());
		}
	}
	
	public void logDebugCommandListeners()
	{
		for(int i=0; i < getCommandListenerCount(); ++i)
		{
			CommandExecutedListener listener = commandExecutedListeners.get(i);
			EAM.logDebug("orphan listener: " + listener.getClass().getName());
		}
	}

	public boolean canUndo()
	{
		if(!getProject().isOpen())
			return false;
		
		return getUndoRedoState().canUndo();
	}
	
	public boolean canRedo()
	{
		if(!getProject().isOpen())
			return false;
		
		return getUndoRedoState().canRedo();
	}
	
	public void beginTransaction() throws CommandFailedException
	{
		if(isInTransaction())
			throw new CommandFailedException("Attempted to nest transactions");
		try
		{
			getProject().getDatabase().beginTransaction();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException("Unable to initiate transaction");
		}
		inTransaction = true;
	}
	
	public void endTransaction() throws CommandFailedException
	{
		inTransaction = false;
		try
		{
			getProject().getDatabase().endTransaction();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException("Unable to complete transaction");
		}
	}
	
	public boolean isInTransaction()
	{
		return inTransaction;
	}	
	
	public void beginCommandSideEffectMode()
	{
		if (isInCommandSideEffectMode())
			throw new RuntimeException("trying to begin side effect mode when inside side effect mode");
		
		inCommandSideEffectMode = true;
	}

	public void endCommandSideEffectMode()
	{
		if (!isInCommandSideEffectMode())
			throw new RuntimeException("trying to end side effect mode when outside side effect mode");
		
		inCommandSideEffectMode = false;
	}
	
	public void enableIsDoNothingCommandOptimization()
	{
		if (isDoNothingCommandEnabledOptimization())
			throw new RuntimeException("Trying to enable isDoNothingCommandOptimization when its already enabled");
		
		isDoNothingCommandOptimizationEnabled = true;
	}
	
	public void disableIsDoNothingCommandOptimization()
	{
		if (!isDoNothingCommandEnabledOptimization())
			throw new RuntimeException("Trying to disable isDoNothingCommandOptimization when its already disabled");
		
		isDoNothingCommandOptimizationEnabled = false;
	}
	
	public boolean isDoNothingCommand(Command command)	throws CommandFailedException
	{
		return command.isDoNothingCommand(getProject());
	}
	
	public boolean isDoNothingCommandEnabledOptimization()
	{
		return isDoNothingCommandOptimizationEnabled;
	}

	public boolean isInCommandSideEffectMode()
	{
		return inCommandSideEffectMode;
	}
	
	private UndoRedoState getUndoRedoState()
	{
		return undoRedoState;
	}
	
	private Project getProject()
	{
		return project;
	}
	
	protected NormalExecutor getNormalExecutor()
	{
		return normalExecutor;
	}
	
	private class Executor
	{
		protected void internalExecuteCommand(Command command) throws CommandFailedException, UnableToBeginTransactionException
		{
			enableIsExecuting();
			try
			{
				if (shouldUpdateLastModfiedTime(command))
					getProject().getDatabase().updateLastModifiedTime();
				
				executeWithoutRecording(command);
				doPostExecutionProcessing(command);
				fireCommandExecuted(command);
			}
			catch (Exception e)
			{
				if (command.isBeginTransaction())
					throw new UnableToBeginTransactionException(e);
				
				throw new CommandFailedException(e);
			}
			finally
			{
				disableIsExecuting();
			}
		}

		protected void doPostExecutionProcessing(Command command) throws CommandFailedException
		{
		}		
	}
	
	protected class NormalExecutor extends Executor
	{
		public void executeCommand(Command command) throws UnexpectedNonSideEffectException, CommandFailedException, UnableToBeginTransactionException
		{
			if (isDoNothingCommandEnabledOptimization() && isDoNothingCommand(command))
			{
				EAM.logVerbose("Do-nothing command: " + command.toString());
				return;
			}
			
			if (isInCommandSideEffectMode())
			{
				EAM.logError("Should not have been in Side Effect mode for: " + command.logDataAsString());
				throw new UnexpectedNonSideEffectException();
			}

			beginCommandSideEffectMode();
			try
			{
				internalExecuteCommand(command);
			}
			finally 
			{
				endCommandSideEffectMode();
			}
		}
		
		@Override
		protected void doPostExecutionProcessing(Command command) throws CommandFailedException
		{
			recordCommand(command);
		}
	}
	
	private class SideEffectExecutor extends Executor
	{
		protected void executeAsSideEffect(Command command) throws UnexpectedSideEffectException, CommandFailedException, UnableToBeginTransactionException
		{
			if (isDoNothingCommandEnabledOptimization() && isDoNothingCommand(command))
				return;

			if (!isInCommandSideEffectMode())
			{
				EAM.logError("Should have been in Side Effect mode for: " + command.logDataAsString());
				throw new UnexpectedSideEffectException(command);
			}
			
			internalExecuteCommand(command);	
		}
	}
	
	private Project project;
	private NormalExecutor normalExecutor;
	private SideEffectExecutor sideEffectExecutor;
	private Vector<CommandExecutedListener> commandExecutedListeners;
	private UndoRedoState undoRedoState;
	private boolean inTransaction;
	private boolean isExecuting;
	private boolean inCommandSideEffectMode;
	private boolean isDoNothingCommandOptimizationEnabled;	
}
