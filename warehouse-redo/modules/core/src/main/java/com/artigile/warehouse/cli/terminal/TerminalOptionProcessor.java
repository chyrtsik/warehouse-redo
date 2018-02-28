package com.artigile.warehouse.cli.terminal;

import com.artigile.warehouse.gui.baselayout.WareHouse;
import org.netbeans.api.sendopts.CommandException;
import org.netbeans.spi.sendopts.Env;
import org.netbeans.spi.sendopts.Option;
import org.netbeans.spi.sendopts.OptionProcessor;
import org.openide.awt.Actions;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.WindowManager;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author Valery Barysok, 2013-07-16
 */
@ServiceProvider(service = OptionProcessor.class)
public class TerminalOptionProcessor extends OptionProcessor implements Runnable {

    @Override
    protected Set<Option> getOptions() {
        return Collections.singleton(Option.withoutArgument(Option.NO_SHORT_NAME, "terminal"));
    }

    @Override
    protected void process(Env env, Map<Option, String[]> optionValues) throws CommandException {
        WareHouse.setTerminal(true);
        WindowManager.getDefault().invokeWhenUIReady(this);
    }

    @Override
    public void run() {
        Actions.forID("Window", "org.netbeans.core.windows.actions.ToggleFullScreenAction").actionPerformed(null);
    }
}
