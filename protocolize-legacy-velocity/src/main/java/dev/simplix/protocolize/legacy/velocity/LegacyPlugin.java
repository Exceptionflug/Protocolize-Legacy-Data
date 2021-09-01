package dev.simplix.protocolize.legacy.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import dev.simplix.protocolize.api.Protocolize;
import dev.simplix.protocolize.api.providers.ModuleProvider;
import dev.simplix.protocolize.legacy.LegacyModule;

/**
 * Date: 01.09.2021
 *
 * @author Exceptionflug
 */
@Plugin(name = "ProtocolizeLegacySupport",
        id = "protocolizelegacysupport",
        version = "1.0.0",
        authors = "Exceptionflug",
        dependencies = @Dependency(id = "protocolize"))
public class LegacyPlugin {

    @Subscribe
    public void onInit(ProxyInitializeEvent event) {
        Protocolize.getService(ModuleProvider.class).registerModule(new LegacyModule());
    }

}
