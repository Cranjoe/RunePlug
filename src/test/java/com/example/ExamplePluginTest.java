package com.example;

import com.example.DaeyaltDispenser.DaeyaltDispenserPlugin;
import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.FishingFriend.FishingFriendPlugin;
import com.example.GauntletFlicker.gauntletFlicker;
import com.example.GlassGuy.GlassGuyPlugin;
import com.example.PacketUtils.PacketUtilsPlugin;
import com.example.PathingTesting.PathingTesting;
import com.example.SepulchreSteward.SepulchreStewardPlugin;
import com.example.toaextended.ToaExtendedPlugin;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ExamplePluginTest {
    public static void main(String[] args) throws Exception {
        ExternalPluginManager.loadBuiltin(EthanApiPlugin.class, PacketUtilsPlugin.class);
        RuneLite.main(args);
    }
}
