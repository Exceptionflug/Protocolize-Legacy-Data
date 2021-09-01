package dev.simplix.protocolize.legacy.bungee;

import dev.simplix.protocolize.api.Protocolize;
import dev.simplix.protocolize.api.providers.ModuleProvider;
import dev.simplix.protocolize.legacy.LegacyModule;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * Date: 01.09.2021
 *
 * @author Exceptionflug
 */
public class LegacyPlugin extends Plugin {

    @Override
    public void onEnable() {
        Protocolize.getService(ModuleProvider.class).registerModule(new LegacyModule());
    }
}
