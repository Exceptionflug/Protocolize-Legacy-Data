package dev.simplix.protocolize.legacy;

import de.exceptionflug.protocolize.items.ItemIDMapping;
import de.exceptionflug.protocolize.items.ItemType;
import de.exceptionflug.protocolize.items.SpawnEggItemIDMapping;
import de.exceptionflug.protocolize.world.Sound;
import dev.simplix.protocolize.api.mapping.AbstractProtocolMapping;
import dev.simplix.protocolize.api.mapping.ProtocolIdMapping;
import dev.simplix.protocolize.api.module.ProtocolizeModule;
import dev.simplix.protocolize.api.providers.MappingProvider;
import dev.simplix.protocolize.api.providers.ProtocolRegistrationProvider;
import dev.simplix.protocolize.data.mapping.LegacyItemProtocolIdMapping;
import dev.simplix.protocolize.data.mapping.LegacySpawnEggItemNBTProtocolIdMapping;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static dev.simplix.protocolize.api.util.ProtocolVersions.*;

/**
 * Date: 01.09.2021
 *
 * @author Exceptionflug
 */
@Slf4j(topic = "ProtocolizeLegacySupport")
public class LegacyModule implements ProtocolizeModule {

    private final Map<ItemType, dev.simplix.protocolize.data.ItemType> itemMap = new HashMap<>();

    public LegacyModule() {
        itemMap.put(ItemType.ZOMBIE_PIGMAN_SPAWN_EGG, dev.simplix.protocolize.data.ItemType.ZOMBIFIED_PIGLIN_SPAWN_EGG);
        itemMap.put(ItemType.GRASS_PATH, dev.simplix.protocolize.data.ItemType.DIRT_PATH);
        itemMap.put(ItemType.GRASS, dev.simplix.protocolize.data.ItemType.SHORT_GRASS);
    }

    @Override
    public void registerMappings(MappingProvider mappingProvider) {
        registerItemMappings(mappingProvider);
        registerSoundMappings(mappingProvider);
        log.info("Legacy support enabled.");
    }

    private void registerSoundMappings(MappingProvider mappingProvider) {
        for (int i = MINECRAFT_1_8; i <= MINECRAFT_1_13_2; i++) {
            for (Sound sound : Sound.values()) {
                String soundName = sound.getSoundName(i);
                if (soundName == null) {
                    continue;
                }
                try {
                    dev.simplix.protocolize.data.Sound p2type = dev.simplix.protocolize.data.Sound.valueOf(
                        sound.name().replace("ZOMBIE_PIGMAN", "ZOMBIFIED_PIGLIN"));
                    mappingProvider.registerMapping(p2type, AbstractProtocolMapping.rangedStringMapping(i, i, soundName, -1));
                } catch (IllegalArgumentException e) {
                    log.debug("Don't know what old sound " + sound + " is now.");
                }
            }
        }
    }

    private void registerItemMappings(MappingProvider mappingProvider) {
        for (int i = MINECRAFT_1_8; i <= MINECRAFT_1_12_2; i++) {
            for (ItemType type : ItemType.values()) {
                try {
                    if (type == ItemType.NO_DATA) {
                        continue;
                    }
                    ProtocolIdMapping mapping;
                    ItemIDMapping applicableMapping = type.getApplicableMapping(i);
                    if (applicableMapping == null) {
                        continue;
                    }
                    if (applicableMapping instanceof SpawnEggItemIDMapping) {
                        mapping = new LegacySpawnEggItemNBTProtocolIdMapping(i, i, ((SpawnEggItemIDMapping) applicableMapping).getEntityType());
                    } else {
                        mapping = new LegacyItemProtocolIdMapping(i, i, applicableMapping.getId(), (short) applicableMapping.getData());
                    }
                    dev.simplix.protocolize.data.ItemType p2type;
                    if (itemMap.containsKey(type)) {
                        p2type = itemMap.get(type);
                    } else {
                        p2type = dev.simplix.protocolize.data.ItemType.valueOf(type.name());
                    }
                    mappingProvider.registerMapping(p2type, mapping);
                } catch (IllegalArgumentException e) {
                    log.warn("Don't know what old item type " + type.name() +" is.");
                }
            }
        }
    }

    @Override
    public void registerPackets(ProtocolRegistrationProvider protocolRegistrationProvider) {
        // All packets are registered by default module
    }

}
