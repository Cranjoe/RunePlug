package com.example.toaextended.module;

import com.example.toaextended.ToaExtendedConfig;
import com.example.toaextended.features.*;
import com.example.toaextended.features.apmeken.ApmekenBaboonIndicator;
import com.example.toaextended.features.apmeken.ApmekenBaboonIndicatorOverlay;
import com.example.toaextended.features.apmeken.ApmekenWaveInstaller;
import com.example.toaextended.features.boss.akkha.*;

import com.example.toaextended.features.boss.baba.Baba;
import com.example.toaextended.features.boss.baba.BabaSceneOverlay;
import com.example.toaextended.features.boss.kephri.Kephri;
import com.example.toaextended.features.boss.kephri.KephriSceneOverlay;
import com.example.toaextended.features.boss.warden.phase2.WardenP2;
import com.example.toaextended.features.boss.warden.phase2.WardenP2PrayerInfoboxOverlay;
import com.example.toaextended.features.boss.warden.phase2.WardenP2PrayerWidgetOverlay;
import com.example.toaextended.features.boss.warden.phase2.WardenP2SceneOverlay;
import com.example.toaextended.features.boss.warden.phase3.WardenP3;
import com.example.toaextended.features.boss.warden.phase3.WardenP3PrayerInfoboxOverlay;
import com.example.toaextended.features.boss.warden.phase3.WardenP3PrayerWidgetOverlay;
import com.example.toaextended.features.boss.warden.phase3.WardenP3SceneOverlay;
import com.example.toaextended.features.boss.zebak.Zebak;
import com.example.toaextended.features.boss.zebak.ZebakPrayerInfoboxOverlay;
import com.example.toaextended.features.boss.zebak.ZebakPrayerWidgetOverlay;
import com.example.toaextended.features.boss.zebak.ZebakSceneOverlay;

import com.example.toaextended.features.het.beamtimer.BeamTimerOverlay;
import com.example.toaextended.features.het.beamtimer.BeamTimerTracker;
import com.example.toaextended.features.het.pickaxe.DepositPickaxeOverlay;
import com.example.toaextended.features.het.pickaxe.DepositPickaxePreventEntry;
import com.example.toaextended.features.het.pickaxe.DepositPickaxeSwap;
import com.example.toaextended.features.het.solver.HetSolver;
import com.example.toaextended.features.het.solver.HetSolverOverlay;
import com.example.toaextended.features.hporbs.HpOrbManager;
import com.example.toaextended.features.invocationpresets.InvocationPresetsManager;
import com.example.toaextended.features.pointstracker.PartyPointsTracker;
import com.example.toaextended.features.pointstracker.PointsOverlay;
import com.example.toaextended.features.pointstracker.PointsTracker;
import com.example.toaextended.features.scabaras.SkipObeliskOverlay;
import com.example.toaextended.features.scabaras.overlay.*;
import com.example.toaextended.features.scabaras.panel.ScabarasPanelManager;
import com.example.toaextended.features.timetracking.SplitsOverlay;
import com.example.toaextended.features.timetracking.SplitsTracker;
import com.example.toaextended.features.timetracking.TargetTimeManager;
import com.example.toaextended.features.tomb.CursedPhalanxDetector;
import com.example.toaextended.features.tomb.DryStreakTracker;
import com.example.toaextended.features.tomb.SarcophagusOpeningSoundPlayer;
import com.example.toaextended.features.tomb.SarcophagusRecolorer;
import com.example.toaextended.features.updatenotifier.UpdateNotifier;
import com.example.toaextended.util.RaidStateTracker;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;



@Slf4j
public class TombsOfAmascutModule extends AbstractModule
{

	@Override
	protected void configure()
	{
		Multibinder<PluginLifecycleComponent> lifecycleComponents = Multibinder.newSetBinder(binder(), PluginLifecycleComponent.class);
		lifecycleComponents.addBinding().to(AdditionPuzzleSolver.class);
		lifecycleComponents.addBinding().to(AkkhaShadowHealth.class);
		lifecycleComponents.addBinding().to(AkkhaShadowHealthOverlay.class);
		lifecycleComponents.addBinding().to(ApmekenBaboonIndicator.class);
		lifecycleComponents.addBinding().to(ApmekenBaboonIndicatorOverlay.class);
		lifecycleComponents.addBinding().to(ApmekenWaveInstaller.class);
		lifecycleComponents.addBinding().to(BeamTimerOverlay.class);
		lifecycleComponents.addBinding().to(BeamTimerTracker.class);
		lifecycleComponents.addBinding().to(CameraShakeDisabler.class);
		lifecycleComponents.addBinding().to(CursedPhalanxDetector.class);
		lifecycleComponents.addBinding().to(DepositPickaxeOverlay.class);
		lifecycleComponents.addBinding().to(DepositPickaxePreventEntry.class);
		lifecycleComponents.addBinding().to(DepositPickaxeSwap.class);
		lifecycleComponents.addBinding().to(DryStreakTracker.class);
		lifecycleComponents.addBinding().to(FadeDisabler.class);
		lifecycleComponents.addBinding().to(HetSolver.class);
		lifecycleComponents.addBinding().to(HetSolverOverlay.class);
		lifecycleComponents.addBinding().to(HpOrbManager.class);
		lifecycleComponents.addBinding().to(InvocationPresetsManager.class);
		lifecycleComponents.addBinding().to(InvocationScreenshot.class);
		lifecycleComponents.addBinding().to(LeftClickBankAll.class);
		lifecycleComponents.addBinding().to(LightPuzzleSolver.class);
		lifecycleComponents.addBinding().to(MatchingPuzzleSolver.class);
		lifecycleComponents.addBinding().to(ObeliskPuzzleSolver.class);
		lifecycleComponents.addBinding().to(PartyPointsTracker.class);
		lifecycleComponents.addBinding().to(PathLevelTracker.class);
		lifecycleComponents.addBinding().to(PointsOverlay.class);
		lifecycleComponents.addBinding().to(PointsTracker.class);
		lifecycleComponents.addBinding().to(QuickProceedSwaps.class);
		lifecycleComponents.addBinding().to(RaidStateTracker.class);
		lifecycleComponents.addBinding().to(SarcophagusOpeningSoundPlayer.class);
		lifecycleComponents.addBinding().to(SarcophagusRecolorer.class);
		lifecycleComponents.addBinding().to(ScabarasOverlayManager.class);
		lifecycleComponents.addBinding().to(ScabarasPanelManager.class);
		lifecycleComponents.addBinding().to(SequencePuzzleSolver.class);
		lifecycleComponents.addBinding().to(SkipObeliskOverlay.class);
		lifecycleComponents.addBinding().to(SmellingSaltsCooldown.class);
		lifecycleComponents.addBinding().to(SplitsOverlay.class);
		lifecycleComponents.addBinding().to(SplitsTracker.class);
		lifecycleComponents.addBinding().to(TargetTimeManager.class);
		lifecycleComponents.addBinding().to(UpdateNotifier.class);

		lifecycleComponents.addBinding().to(Zebak.class);
		lifecycleComponents.addBinding().to(ZebakSceneOverlay.class);
		lifecycleComponents.addBinding().to(ZebakPrayerWidgetOverlay.class);
		lifecycleComponents.addBinding().to(ZebakPrayerInfoboxOverlay.class);
		lifecycleComponents.addBinding().to(Kephri.class);
		lifecycleComponents.addBinding().to(KephriSceneOverlay.class);
		lifecycleComponents.addBinding().to(Akkha.class);
		lifecycleComponents.addBinding().to(AkkhaSceneOverlay.class);
		lifecycleComponents.addBinding().to(AkkhaPrayerWidgetOverlay.class);
		lifecycleComponents.addBinding().to(AkkhaPrayerInfoboxOverlay.class);
		lifecycleComponents.addBinding().to(AkkhaMemoryBlast.class);
		lifecycleComponents.addBinding().to(AkkhaFinalStand.class);
		lifecycleComponents.addBinding().to(Baba.class);
		lifecycleComponents.addBinding().to(BabaSceneOverlay.class);

		lifecycleComponents.addBinding().to(WardenP2.class);
		lifecycleComponents.addBinding().to(WardenP2SceneOverlay.class);
		lifecycleComponents.addBinding().to(WardenP2PrayerWidgetOverlay.class);
		lifecycleComponents.addBinding().to(WardenP2PrayerInfoboxOverlay.class);

		lifecycleComponents.addBinding().to(WardenP3.class);
		lifecycleComponents.addBinding().to(WardenP3SceneOverlay.class);
		lifecycleComponents.addBinding().to(WardenP3PrayerWidgetOverlay.class);
		lifecycleComponents.addBinding().to(WardenP3PrayerInfoboxOverlay.class);





	}

	@Provides
	@Singleton
	ToaExtendedConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ToaExtendedConfig.class);
	}

}
