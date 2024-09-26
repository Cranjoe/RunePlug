package com.example.toaextended;

import com.example.toaextended.module.ComponentManager;
import com.example.toaextended.module.TombsOfAmascutModule;
import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.RuneLite;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.io.File;

@Slf4j
@PluginDescriptor(
	name = "Toa Extended",
	description = "Utilities and information for raiding the Tombs of Amascut.",
	tags = {"toa", "raid", "3", "invocation", "preset"},
	conflicts = {"Tombs of Amascut"},
	enabledByDefault = false
)
public class ToaExtendedPlugin extends Plugin
{

	public static final String EVENT_NAMESPACE = "tombs-of-amascut";

	public static final File TOA_FOLDER = new File(RuneLite.RUNELITE_DIR, "tombs-of-amascut");

	@Inject
	private Injector injector;

	@Inject
	private ConfigMigrationService configMigrationService;

	private ComponentManager componentManager = null;

	@Override
	public void configure(Binder binder)
	{
		binder.install(new TombsOfAmascutModule());
	}

	@Override
	protected void startUp() throws Exception
	{
		if (!TOA_FOLDER.exists() && !TOA_FOLDER.mkdirs())
		{
			log.warn("Failed to create ToA folder {}", TOA_FOLDER.getAbsolutePath());
		}
		configMigrationService.migrate();

		if (componentManager == null)
		{
			componentManager = injector.getInstance(ComponentManager.class);
		}
		componentManager.onPluginStart();
	}

	@Override
	protected void shutDown() throws Exception
	{
		componentManager.onPluginStop();
	}
}
