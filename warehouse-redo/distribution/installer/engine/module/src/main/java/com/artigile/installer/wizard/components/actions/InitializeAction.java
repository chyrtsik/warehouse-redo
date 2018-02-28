package com.artigile.installer.wizard.components.actions;

import org.netbeans.installer.utils.ResourceUtils;
import org.netbeans.installer.utils.progress.CompositeProgress;
import org.netbeans.installer.wizard.components.WizardAction;
import org.netbeans.installer.wizard.components.actions.DownloadConfigurationLogicAction;
import org.netbeans.installer.wizard.components.actions.InitializeRegistryAction;

/**
 *
 * @author Valery Barysok
 */
public class InitializeAction extends WizardAction {

    private InitializeRegistryAction initReg;
    private DownloadConfigurationLogicAction downloadLogic;

    public InitializeAction() {
        setProperty(TITLE_PROPERTY, DEFAULT_TITLE);
        setProperty(DESCRIPTION_PROPERTY, DEFAULT_DESCRIPTION);

        initReg = new InitializeRegistryAction();
        downloadLogic = new DownloadConfigurationLogicAction();
    }

    @Override
    public void execute() {
        CompositeProgress progress = new CompositeProgress(getWizardUi());
        progress.setTitle(getProperty(TITLE_PROPERTY));
        progress.synchronizeDetails(false);

        if (initReg.canExecuteForward()) {
            initReg.setWizard(getWizard());
            initReg.execute();
        }

        if (downloadLogic.canExecuteForward()) {
            downloadLogic.setWizard(getWizard());
            downloadLogic.execute();
        }
    }

    public static final String DEFAULT_TITLE = ResourceUtils.getString(InitializeAction.class, "IA.title"); // NOI18N
    public static final String PROGRESS_TITLE_PROPERTY = ResourceUtils.getString(InitializeAction.class, "IA.progress.title"); // NOI18N
    public static final String DEFAULT_DESCRIPTION = ResourceUtils.getString(InitializeAction.class, "IA.description"); // NOI18N
}
