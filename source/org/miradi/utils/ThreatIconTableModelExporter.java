package org.miradi.utils;

import org.miradi.dialogs.threatrating.upperPanel.ThreatIconColumnTableModel;
import org.miradi.questions.ChoiceItem;

public class ThreatIconTableModelExporter extends MainThreatTableModelExporter
{
    public ThreatIconTableModelExporter(ThreatIconColumnTableModel threatIconColumnTableModelToUse)
    {
        super(threatIconColumnTableModelToUse);

        threatIconColumnTableModel = threatIconColumnTableModelToUse;
    }

    @Override
    public ChoiceItem getModelChoiceItemAt(int row, int modelColumn)
    {
        return getThreatIconModel().getChoiceItemAt(row, modelColumn);
    }

    private ThreatIconColumnTableModel getThreatIconModel()
    {
        return threatIconColumnTableModel;
    }

    private ThreatIconColumnTableModel threatIconColumnTableModel;
}
