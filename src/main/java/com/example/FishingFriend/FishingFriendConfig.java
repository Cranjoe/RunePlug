package com.example.FishingFriend;


import net.runelite.client.config.*;

@ConfigGroup("FishingFriendConfig")
public interface FishingFriendConfig extends Config {
    @ConfigItem(
            keyName = "Toggle",
            name = "Toggle",
            description = "",
            position = 0
    )
    default Keybind toggle() {
        return Keybind.NOT_SET;
    }

    @ConfigSection(
            name = "Game Tick Configuration",
            description = "Configure how to handles game tick delays, 1 game tick equates to roughly 600ms",
            position = 1,
            closedByDefault = true
    )
    String delayTickConfig = "delayTickConfig";
    @ConfigSection(
            name = "Drop Config",
            description = "Configuration for amount of items to drop",
            position = 5

    )
    String dropConfigSection = "Drop Config";

    @Range(
            max = 10
    )
    @ConfigItem(
            keyName = "tickDelayMin",
            name = "Game Tick Min",
            description = "",
            position = 2,
            section = delayTickConfig
    )
    default int tickDelayMin() {
        return 1;
    }

    @Range(
            max = 10
    )
    @ConfigItem(
            keyName = "tickDelayMax",
            name = "Game Tick Max",
            description = "",
            position = 3,
            section = delayTickConfig
    )
    default int tickDelayMax() {
        return 3;
    }

    @ConfigItem(
            keyName = "tickDelayEnabled",
            name = "Tick delay",
            description = "enables some tick delays",
            position = 4,
            section = delayTickConfig
    )
    default boolean tickDelay() {
        return true;
    }

    @Range(
            max = 9
    )
    @ConfigItem(
            name = "Drop Per Tick Min",
            keyName = "numToDrop1",
            description = "Minimum amount of items dropped per tick",
            position = 6,
            section = dropConfigSection
    )
    default int dropPerTickOne() {
        return 1;
    }

    @Range(
            max = 9
    )
    @ConfigItem(
            name = "Drop Per Tick Max",
            keyName = "numToDrop2",
            description = "Maximum amount of items dropped per tick",
            position = 7,
            section = dropConfigSection
    )
    default int dropPerTickTwo() {
        return 3;
    }

    @ConfigItem(
            name = "Keep Items",
            keyName = "itemToKeep",
            description = "Items you don't want dropped. Separate items by comma,no space. Good for UIM",
            position = 4
    )
    default String itemsToKeep() {
        return "coins,rune pouch,divine rune pouch,looting bag,clue scroll";
    }

    @ConfigItem(
            name = "Bank Sturgeon",
            keyName = "bankSturgeon",
            description = "Should bank sturgeon",
            position = 15
    )
    default boolean bankSturgeon() {
        return true;
    }
}