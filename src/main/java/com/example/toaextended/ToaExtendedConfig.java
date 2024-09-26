package com.example.toaextended;

import com.example.toaextended.features.QuickProceedSwaps.QuickProceedEnableMode;
import com.example.toaextended.features.hporbs.HpOrbMode;
import com.example.toaextended.features.scabaras.ScabarasHelperMode;
import com.example.toaextended.features.scabaras.SkipObeliskOverlay;
import com.example.toaextended.features.scabaras.overlay.MatchingTileDisplayMode;
import com.example.toaextended.features.timetracking.SplitsMode;
import com.example.toaextended.features.updatenotifier.UpdateNotifier;
import com.example.toaextended.util.FontStyle;
import com.example.toaextended.util.HighlightMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.client.config.*;

import java.awt.*;

@ConfigGroup(ToaExtendedConfig.CONFIG_GROUP)
public interface ToaExtendedConfig extends Config
{

	String CONFIG_GROUP = "tombsofamascut";

	// Sections

	@ConfigSection(
		name = "Miscellaneous",
		description = "Miscellaneous configurations.",
		position = 0,
		closedByDefault = false
	)
	String SECTION_MISCELLANEOUS = "sectionMiscellaneous";

	@ConfigSection(
			name = "Zebak",
			description = "Zebak configuration.",
			position = 0,
			closedByDefault = true
	)
	String SECTION_ZEBAK = "zebakSection";

	@ConfigSection(
			name = "Kephri",
			description = "Kephri configuration.",
			position = 1,
			closedByDefault = true
	)
	String SECTION_KEPHRI = "kephriSection";

	@ConfigSection(
			name = "Baba",
			description = "Baba configuration.",
			position = 2,
			closedByDefault = true
	)
	String SECTION_BABA = "babaSection";

	@ConfigSection(
		name = "Akkha",
		description = "Configuration for Akkha boss room.",
		position = 1,
		closedByDefault = true
	)
	String SECTION_AKKHA = "sectionAkkha";

	@ConfigSection(
			name = "Warden Phase 2",
			description = "Warden phase 2 configuration.",
			position = 4,
			closedByDefault = true
	)
	String SECTION_WARDEN_P2 = "wardenPhase2Section";

	@ConfigSection(
			name = "Warden Phase 3",
			description = "Warden phase 3 configuration.",
			position = 5,
			closedByDefault = true
	)
	String SECTION_WARDEN_P3 = "wardenPhase3Section";

	@ConfigSection(
		name = "Path of Apmeken",
		description = "Options for the Path of Apmeken.",
		position = 2,
		closedByDefault = true
	)
	String SECTION_APMEKEN = "sectionApmeken";

	@ConfigSection(
		name = "Path of Het",
		description = "Helpers for the Path of Het.",
		position = 3,
		closedByDefault = true
	)
	String SECTION_HET = "sectionHet";

	@ConfigSection(
			name = "Prayer",
			description = "Prayer configuration.",
			position = 9,
			closedByDefault = true
	)
	String SECTION_PRAYER = "prayerSection";

	@ConfigSection(
		name = "Path of Scabaras",
		description = "Options for the puzzles in the Path of Scabaras.",
		position = 4,
		closedByDefault = true
	)
	String SECTION_SCABARAS = "sectionScabaras";

	@ConfigSection(
		name = "Burial Tomb",
		description = "Configuration for the burial tomb.",
		position = 5,
		closedByDefault = true
	)
	String SECTION_BURIAL_TOMB = "sectionBurialTomb";

	@ConfigSection(
		name = "Points Tracker",
		description = "<html>Tracks points for the raid, used in calculating drop chance." +
			"<br/>NOTE: For teams, you MUST use the RuneLite Party plugin to receive team drop chance.</html>",
		position = 6,
		closedByDefault = true
	)
	String SECTION_POINTS_TRACKER = "sectionPointsTracker";

	@ConfigSection(
		name = "Invocation Presets",
		description = "Save presets of invocations to quickly restore your invocations between runs of different types.",
		position = 7,
		closedByDefault = true
	)
	String SECTION_INVOCATION_PRESETS = "invocationPresetsSection";

	@ConfigSection(
		name = "Invocation Screenshot",
		description = "All config options related to the Invocation Screenshot functionality",
		position = 8,
		closedByDefault = true
	)
	String SECTION_INVOCATION_SCREENSHOT = "invocationScreenshotSection";

	@ConfigSection(
		name = "Time Tracking",
		description = "Time tracking and splits.",
		position = 9,
		closedByDefault = true
	)
	String SECTION_TIME_TRACKING = "sectionTimeTracking";


	// Zebak

	@ConfigItem(
			name = "Prayer Overlay",
			description = "Enable prayer overlays for Zebak." +
					"<br>Requires prayer mode config to be set.",
			position = 0,
			keyName = "zebakPrayerIndicator",
			section = SECTION_ZEBAK
	)
	default boolean zebakPrayerIndicator()
	{
		return false;
	}

	@ConfigItem(
			name = "Health Counter",
			description = "Overlay hp until next phase." +
					"<br>Requires boss hp bar in-game setting turned on.",
			position = 1,
			keyName = "zebakHealthCounter",
			section = SECTION_ZEBAK
	)
	default boolean zebakHealthCounter()
	{
		return false;
	}

	@ConfigItem(
			name = "Blood Cloud Tile",
			description = "Highlight blood cloud tiles.",
			position = 2,
			keyName = "zebakBloodCloudTile",
			section = SECTION_ZEBAK
	)
	default Tile zebakBloodCloudTile()
	{
		return Tile.OFF;
	}

	@ConfigItem(
			name = "Wave Tile",
			description = "Highlight wave tiles.",
			position = 3,
			keyName = "zebakWaveTile",
			section = SECTION_ZEBAK
	)
	default Tile zebakWaveTile()
	{
		return Tile.OFF;
	}

	@ConfigItem(
			name = "Projectile Tile",
			description = "Highlight where rocks, jugs, and poison will land.",
			position = 4,
			keyName = "zebakProjectileTiles",
			section = SECTION_ZEBAK
	)
	default boolean zebakProjectileTiles()
	{
		return false;
	}

	@ConfigItem(
			name = "Blood Magic Outline",
			description = "Outline blood magic for visibility.",
			position = 5,
			keyName = "zebakBloodMagicOutline",
			section = SECTION_ZEBAK
	)
	default boolean zebakBloodMagicOutline()
	{
		return false;
	}

	@ConfigItem(
			name = "Jug Outline",
			description = "Outline jugs.",
			position = 6,
			keyName = "zebakJugOutline",
			section = SECTION_ZEBAK
	)
	default boolean zebakJugOutline()
	{
		return false;
	}

	@ConfigItem(
			name = "Boulder Outline",
			description = "Outline boulders.",
			position = 7,
			keyName = "zebakBoulderOutline",
			section = SECTION_ZEBAK
	)
	default boolean zebakBoulderOutline()
	{
		return false;
	}

	@ConfigItem(
			name = "Hide Wave",
			description = "Prevent rendering waves." +
					"<br>Use with wave tile highlighting.",
			position = 8,
			keyName = "zebakHideWaves",
			section = SECTION_ZEBAK
	)
	default boolean zebakHideWaves()
	{
		return false;
	}

	// Kephri

	@ConfigItem(
			name = "Attack Counter",
			description = "Overlay Kephri with an attack counter.",
			position = 0,
			keyName = "kephriAttackCounter",
			section = SECTION_KEPHRI
	)
	default AttackCounter kephriAttackCounter()
	{
		return AttackCounter.OFF;
	}

	@ConfigItem(
			name = "Fireball Tile",
			description = "Highlight where fireball projectiles will land." +
					"<br>Includes bomber scarabs.",
			position = 1,
			keyName = "kephriFireballTiles",
			section = SECTION_KEPHRI
	)
	default FireballRadius kephriFireballTiles()
	{
		return FireballRadius.OFF;
	}

	@ConfigItem(
			name = "Egg Tile",
			description = "Highlight egg explosion radius tiles.",
			position = 2,
			keyName = "kephriEggTiles",
			section = SECTION_KEPHRI
	)
	default boolean kephriEggTiles()
	{
		return false;
	}

	@ConfigItem(
			name = "Dung Outline",
			description = "Outline Kephri and player when targetted by dung attack.",
			position = 3,
			keyName = "kephriDungOutline",
			section = SECTION_KEPHRI
	)
	default boolean kephriDungOutline()
	{
		return false;
	}

	@ConfigItem(
			name = "Overlord Outline",
			description = "Outline overlord scarabs.",
			position = 4,
			keyName = "kephriOverlordOutline",
			section = SECTION_KEPHRI
	)
	default boolean kephriOverlordOutline()
	{
		return false;
	}

	@ConfigItem(
			name = "Swarm Outline",
			description = "Outline scarab swarms.",
			position = 5,
			keyName = "kephriScarabSwarmOutline",
			section = SECTION_KEPHRI
	)
	default boolean kephriScarabSwarmOutline()
	{
		return false;
	}

	@ConfigItem(
			name = "Hide Fireball",
			description = "Prevent rendering fireballs." +
					"<br>Use with fireball tile highlighting.",
			position = 6,
			keyName = "kephriHideFireballProjectile",
			section = SECTION_KEPHRI
	)
	default boolean kephriHideFireballProjectile()
	{
		return false;
	}

	@ConfigItem(
			name = "Hide Bomber Scarab",
			description = "Prevent rendering bomber scarabs." +
					"<br>Use with fireball tile highlighting.",
			position = 7,
			keyName = "kephriHideBomberScarabProjectile",
			section = SECTION_KEPHRI
	)
	default boolean kephriHideBomberScarabProjectile()
	{
		return false;
	}

	@ConfigItem(
			name = "Hide Unattackable Swarm",
			description = "Prevent rendering unattackable swarms.",
			position = 8,
			keyName = "kephriHideUnattackableScarabSwarm",
			section = SECTION_KEPHRI
	)
	default boolean kephriHideUnattackableScarabSwarm()
	{
		return false;
	}

	@ConfigItem(
			name = "Hide Agile Scarab",
			description = "Prevent rendering agile scarabs.",
			position = 9,
			keyName = "kephriHideAgileScarabNpc",
			section = SECTION_KEPHRI
	)
	default boolean kephriHideAgileScarabNpc()
	{
		return false;
	}

	@ConfigItem(
			name = "Hide Agile Scarab Projectile",
			description = "Prevent rendering agile scarab projectiles.",
			position = 10,
			keyName = "kephriHideAgileScarabProjectile",
			section = SECTION_KEPHRI
	)
	default boolean kephriHideAgileScarabProjectile()
	{
		return false;
	}

	// Baba

	@ConfigItem(
			name = "Health Counter",
			description = "Overlay hp until next phase." +
					"<br>Requires boss hp bar in-game setting turned on.",
			position = 0,
			keyName = "babaHealthCounter",
			section = SECTION_BABA
	)
	default boolean babaHealthCounter()
	{
		return false;
	}

	@ConfigItem(
			name = "Baba Tile",
			description = "Highlight Baba's tile.",
			position = 1,
			keyName = "babaTile",
			section = SECTION_BABA
	)
	default Tile babaTile()
	{
		return Tile.OFF;
	}

	@ConfigItem(
			name = "Shockwave Tile",
			description = "Highlight where shockwaves will hit.",
			position = 2,
			keyName = "babaShockwaveTiles",
			section = SECTION_BABA
	)
	default boolean babaShockwaveTiles()
	{
		return false;
	}

	@ConfigItem(
			name = "Rolling Boulder Tile",
			description = "Highlight non-weakened rolling boulders.",
			position = 3,
			keyName = "babaNonWeakenedRollingBoulderTiles",
			section = SECTION_BABA
	)
	default boolean babaNonWeakenedRollingBoulderTiles()
	{
		return false;
	}

	@ConfigItem(
			name = "Falling Boulder Tile",
			description = "Highlight where falling boulders will land.",
			position = 4,
			keyName = "babaFallingBoulderTiles",
			section = SECTION_BABA
	)
	default boolean babaFallingBoulderTiles()
	{
		return false;
	}

	@ConfigItem(
			name = "Sarcophagus Projectile Tile",
			description = "Highlight where projectiles will land.",
			position = 5,
			keyName = "babaSarcophagusTiles",
			section = SECTION_BABA
	)
	default boolean babaSarcophagusTiles()
	{
		return false;
	}

	@ConfigItem(
			name = "Rubble Tile",
			description = "Highlight perimiter of rubble.",
			position = 6,
			keyName = "babaRubbleTiles",
			section = SECTION_BABA
	)
	default boolean babaRubbleTiles()
	{
		return false;
	}

	@ConfigItem(
			name = "Banana Peel Tile",
			description = "Highlight banana peels.",
			position = 7,
			keyName = "babaBananaPeelTiles",
			section = SECTION_BABA
	)
	default boolean babaBananaPeelTiles()
	{
		return false;
	}

	@ConfigItem(
			name = "Boulder Throw Outline",
			description = "Outline Baba when she throws a boulder.",
			position = 8,
			keyName = "babaBabaSpecOutline",
			section = SECTION_BABA
	)
	default boolean babaSpecialAttackOutline()
	{
		return false;
	}

	@ConfigItem(
			name = "Baboon Outline",
			description = "Outline baboons.",
			position = 9,
			keyName = "babaBaboonOutline",
			section = SECTION_BABA
	)
	default boolean babaBaboonOutline()
	{
		return false;
	}

	@ConfigItem(
			name = "Hide Rolling Boulder",
			description = "Prevent rendering non-weakened boulders." +
					"<br>Use with rolling boulder tile highlighting.",
			position = 10,
			keyName = "babaHideNonWeakenedRollingBoulders",
			section = SECTION_BABA
	)
	default boolean babaHideNonWeakenedRollingBoulders()
	{
		return false;
	}

	@ConfigItem(
			name = "Hide Rolling Boulder Projectile",
			description = "Prevent rendering rolling boulder projectiles.",
			position = 11,
			keyName = "babaHideRollingBoulderProjectiles",
			section = SECTION_BABA
	)
	default boolean babaHideRollingBoulderProjectiles()
	{
		return false;
	}

	@ConfigItem(
			name = "Hide Falling Boulder",
			description = "Prevent rendering falling boulders." +
					"<br>Use with falling boulder tile highlighting.",
			position = 12,
			keyName = "babaHideFallingBoulders",
			section = SECTION_BABA
	)
	default boolean babaHideFallingBoulders()
	{
		return false;
	}

	@ConfigItem(
			name = "Hide Baboon Projectile",
			description = "Prevent rendering baboon projectiles.",
			position = 13,
			keyName = "babaHideBaboonProjectiles",
			section = SECTION_BABA
	)
	default boolean babaHideBaboonProjectiles()
	{
		return false;
	}

	// Akkha

	@ConfigItem(
		name = "Shadows Hp Overlay",
		description = "Overlay Akkha's Shadows Hp.",
		position = 0,
		keyName = "akkhaShadowHpOverlay",
		section = SECTION_AKKHA
	)
	default boolean akkhaShadowHpOverlay()
	{
		return false;
	}

	@ConfigItem(
		name = "Font Style",
		description = "Font style of text overlay.",
		position = 1,
		keyName = "akkhaFontStyle",
		section = SECTION_AKKHA
	)
	default FontStyle akkhaFontStyle()
	{
		return FontStyle.PLAIN;
	}

	@ConfigItem(
		name = "Font Size",
		description = "Font size of text overlay.",
		position = 2,
		keyName = "akkhaFontSize",
		section = SECTION_AKKHA
	)
	@Units(Units.PIXELS)
	@Range(min = 12)
	default int akkhaFontSize()
	{
		return 12;
	}

	@ConfigItem(
			name = "Prayer Overlay",
			description = "Enable prayer overlays for Akkha." +
					"<br>Requires prayer mode config to be set.",
			position = 0,
			keyName = "akkhaPrayerIndicator",
			section = SECTION_AKKHA
	)
	default boolean akkhaPrayerIndicator()
	{
		return false;
	}

	@ConfigItem(
			name = "Health Counter",
			description = "Overlay hp until next phase." +
					"<br>Requires boss hp bar in-game setting turned on.",
			position = 1,
			keyName = "akkhaHealthCounter",
			section = SECTION_AKKHA
	)
	default boolean akkhaHealthCounter()
	{
		return false;
	}

	@ConfigItem(
			name = "Akkha Tile",
			description = "Highlight Akkha's tile with color indicating attack style.",
			position = 2,
			keyName = "akkhaTile",
			section = SECTION_AKKHA
	)
	default Tile akkhaTile()
	{
		return Tile.OFF;
	}

	@ConfigItem(
			name = "Special Attack Outline",
			description = "Outline Akkha when a special attack is imminent." +
					"<br>Trailing orbs or memory blast.",
			position = 3,
			keyName = "akkhaSpecialAttackOutline",
			section = SECTION_AKKHA
	)
	default boolean akkhaSpecialAttackOutline()
	{
		return false;
	}

	@ConfigItem(
			name = "Attack Tick Counter",
			description = "Overlay ticks until Akkha's next attack on player." +
					"<br>Step back invocation.",
			position = 4,
			keyName = "akkhaAttackTickCounter",
			section = SECTION_AKKHA
	)
	default boolean akkhaAttackTickCounter()
	{
		return false;
	}

	@ConfigItem(
			name = "Memory Blast Tracker",
			description = "Highlight the memorized tiles.",
			position = 5,
			keyName = "akkhaMemoryBlastTracker",
			section = SECTION_AKKHA
	)
	default boolean akkhaMemoryBlastTracker()
	{
		return false;
	}

	@ConfigItem(
			name = "Unstable Orb Radius",
			description = "Highlight radius of unstable orbs.",
			position = 6,
			keyName = "akkhaUnstableOrbRadius",
			section = SECTION_AKKHA
	)
	default boolean akkhaUnstableOrbRadius()
	{
		return false;
	}

	@Range(
			max = 7
	)
	@ConfigItem(
			name = "Radius Distance",
			description = "How many tiles to highlight.",
			position = 7,
			keyName = "akkhaRadiusDistance",
			section = SECTION_AKKHA
	)
	default int akkhaRadiusDistance()
	{
		return 3;
	}

	@ConfigItem(
			name = "Unstable Orb Tile",
			description = "Highlight unstable orbs.",
			position = 8,
			keyName = "akkhaUnstableOrbTiles",
			section = SECTION_AKKHA
	)
	default Tile akkhaUnstableOrbTiles()
	{
		return Tile.OFF;
	}

	@ConfigItem(
			name = "Hide Unstable Orbs",
			description = "Prevent rendering unstable orbs." +
					"<br>Use with unstable orb highlighting.",
			position = 9,
			keyName = "akkhaHideUnstableOrbs",
			section = SECTION_AKKHA
	)
	default boolean akkhaHideUnstableOrbs()
	{
		return false;
	}

	// Warden Phase 2

	@ConfigItem(
			name = "Prayer Overlay",
			description = "Enable prayer overlays for Wardens." +
					"<br>Requires prayer mode config to be set.",
			position = 0,
			keyName = "wardenP2PrayerIndicator",
			section = SECTION_WARDEN_P2
	)
	default boolean wardenP2PrayerIndicator()
	{
		return false;
	}

	@ConfigItem(
			name = "Health Counter",
			description = "Overlay hp until next phase." +
					"<br>Requires boss hp bar in-game setting turned on.",
			position = 1,
			keyName = "wardenP2HealthCounter",
			section = SECTION_WARDEN_P2
	)
	default boolean wardenP2HealthCounter()
	{
		return false;
	}

	@ConfigItem(
			name = "Warden Core Tick Timer",
			description = "Overlay warden core with a tick timer.",
			position = 2,
			keyName = "wardenCoreTickTimer",
			section = SECTION_WARDEN_P2
	)
	default boolean wardenCoreTickTimer()
	{
		return false;
	}

	@ConfigItem(
			name = "Warden Tile",
			description = "Highlight warden's tile.",
			position = 3,
			keyName = "wardenTile",
			section = SECTION_WARDEN_P2
	)
	default boolean wardenTile()
	{
		return false;
	}

	@ConfigItem(
			name = "Warden Core Tile",
			description = "Highlight warden core tile.",
			position = 4,
			keyName = "wardenCoreTile",
			section = SECTION_WARDEN_P2
	)
	default boolean wardenCoreTile()
	{
		return false;
	}

	@ConfigItem(
			name = "Black Skull Projectile Tile",
			description = "Highlight black skull projectile.",
			position = 5,
			keyName = "wardenBlackSkullProjectileTile",
			section = SECTION_WARDEN_P2
	)
	default boolean wardenBlackSkullProjectileTile()
	{
		return false;
	}

	@ConfigItem(
			name = "Lightning Projectile Tile",
			description = "Highlight obelisk's lightning projectile.",
			position = 6,
			keyName = "wardenLightningProjectileTile",
			section = SECTION_WARDEN_P2
	)
	default boolean wardenLightningProjectileTile()
	{
		return false;
	}

	@ConfigItem(
			name = "Hide Lightning Tile",
			description = "Prevent rendering lightning gfx objects." +
					"<br>Use with lightning tile highlighting.",
			position = 7,
			keyName = "wardenHideLightningTiles",
			section = SECTION_WARDEN_P2
	)
	default boolean wardenHideLightningTiles()
	{
		return false;
	}

	@ConfigItem(
			name = "Hide Lightning Projectile",
			description = "Prevent rendering lightning projectiles." +
					"<br>Use with lightning tile highlighting.",
			position = 8,
			keyName = "wardenHideLightningProjectiles",
			section = SECTION_WARDEN_P2
	)
	default boolean wardenHideLightningProjectiles()
	{
		return false;
	}

	// Warden Phase 3

	@ConfigItem(
			name = "Prayer Overlay",
			description = "Enable prayer overlays for Wardens." +
					"<br>Requires prayer mode config to be set.",
			position = 0,
			keyName = "wardenP3PrayerIndicator",
			section = SECTION_WARDEN_P3
	)
	default boolean wardenP3PrayerIndicator()
	{
		return false;
	}

	@ConfigItem(
			name = "Health Counter",
			description = "Overlay hp until next phase." +
					"<br>Requires boss hp bar in-game setting turned on.",
			position = 1,
			keyName = "wardenP3HealthCounter",
			section = SECTION_WARDEN_P3
	)
	default boolean wardenP3HealthCounter()
	{
		return false;
	}

	@ConfigItem(
			name = "Warden Slam Indicator",
			description = "Indicate where to stand when slamming starts/resumes." +
					"<br>Insanity invocation.",
			position = 2,
			keyName = "wardenSlamIndicator",
			section = SECTION_WARDEN_P3
	)
	default boolean wardenSlamIndicator()
	{
		return false;
	}

	@ConfigItem(
			name = "Siphon Tick Timer",
			description = "Overlay energy siphons with a tick timer." +
					"<br>Solo and Insanity invocation.",
			position = 3,
			keyName = "wardenEnergySiphonTickTimer",
			section = SECTION_WARDEN_P3
	)
	default boolean wardenEnergySiphonTickTimer()
	{
		return false;
	}

	@ConfigItem(
			name = "Siphon Projectile Tile",
			description = "Highlight energy siphon projectiles.",
			position = 4,
			keyName = "wardenEnergySiphonProjectileTile",
			section = SECTION_WARDEN_P3
	)
	default boolean wardenEnergySiphonProjectileTile()
	{
		return false;
	}

	@ConfigItem(
			name = "Boulder Tile",
			description = "Highlight Phantom Baba's falling boulder.",
			position = 5,
			keyName = "wardenBabaFallingBoulderTile",
			section = SECTION_WARDEN_P3
	)
	default boolean wardenBabaFallingBoulderTile()
	{
		return false;
	}

	@ConfigItem(
			name = "Fireball Tile",
			description = "Highlight Phantom Kephri's fireball.",
			position = 6,
			keyName = "wardenKephriFireballTile",
			section = SECTION_WARDEN_P3
	)
	default FireballRadius wardenKephriFireballTile()
	{
		return FireballRadius.OFF;
	}

	@ConfigItem(
			name = "Red Lightning Tile",
			description = "Highlight red lightning gfx objects." +
					"<br>Insanity phase.",
			position = 7,
			keyName = "wardenRedLightningTiles",
			section = SECTION_WARDEN_P3
	)
	default boolean wardenRedLightningTiles()
	{
		return false;
	}

	@ConfigItem(
			name = "Hide Tile Debris Projectile",
			description = "Prevent rendering tile debris projectiles." +
					"<br>Insanity phase.",
			position = 8,
			keyName = "wardenHideTileDebrisProjectile",
			section = SECTION_WARDEN_P3
	)
	default boolean wardenHideTileDebrisProjectile()
	{
		return false;
	}

	@ConfigItem(
			name = "Hide Siphon Projectile",
			description = "Prevent rendering energy siphon projectiles.",
			position = 9,
			keyName = "wardenHideEnergySiphonProjectile",
			section = SECTION_WARDEN_P3
	)
	default boolean wardenHideEnergySiphonProjectile()
	{
		return false;
	}

	@ConfigItem(
			name = "Hide Charge Projectile",
			description = "Prevent rendering energy siphon charge projectiles.",
			position = 10,
			keyName = "wardenHideEnergySiphonChargeProjectiles",
			section = SECTION_WARDEN_P3
	)
	default boolean wardenHideEnergySiphonChargeProjectiles()
	{
		return false;
	}

	@ConfigItem(
			name = "Hide Dead Siphon",
			description = "Prevent rendering dead energy siphons.",
			position = 11,
			keyName = "wardenHideDeadEnergySiphon",
			section = SECTION_WARDEN_P3
	)
	default boolean wardenHideDeadEnergySiphon()
	{
		return false;
	}

	@ConfigItem(
			name = "Hide Baba Boulder",
			description = "Prevent rendering Phantom Baba's falling boulders." +
					"<br>Use with boulder tile highlighting.",
			position = 12,
			keyName = "wardenHideBabaFallingBoulders",
			section = SECTION_WARDEN_P3
	)
	default boolean wardenHideBabaFallingBoulders()
	{
		return false;
	}

	@ConfigItem(
			name = "Hide Kephri Fireball",
			description = "Prevent rendering Phantom Kephri's fireballs." +
					"<br>Use with fireball tile highlighting.",
			position = 13,
			keyName = "wardenHideKephriFireballProjectile",
			section = SECTION_WARDEN_P3
	)
	default boolean wardenHideKephriFireballProjectile()
	{
		return false;
	}

	@ConfigItem(
			name = "Hide Zebak Projectile",
			description = "Prevent renndering Zebak's range and mage projectiles." +
					"<br>Use with prayer overlay.",
			position = 14,
			keyName = "wardenHideZebakProjectile",
			section = SECTION_WARDEN_P3
	)
	default boolean wardenHideZebakProjectile()
	{
		return false;
	}

	@ConfigItem(
			name = "Hide Red Lightning",
			description = "Prevent rendering red lightning gfx objects." +
					"<br>Insanity phase. Use with red lighting tile highlighting.",
			position = 15,
			keyName = "wardenHideRedLightning",
			section = SECTION_WARDEN_P3
	)
	default boolean wardenHideRedLightning()
	{
		return false;
	}

	// Apmeken

	@ConfigItem(
		keyName = "apmekenWaveHelper",
		name = "Apmeken Wave Helper",
		description = "When entering the Path of Apmeken, displays a list of the waves in the RuneLite side panel.",
		position = 0,
		section = SECTION_APMEKEN
	)
	default boolean apmekenWaveHelper()
	{
		return true;
	}

	@ConfigItem(
		name = "Baboon Outline",
		description = "Highlight baboons.",
		position = 1,
		keyName = "apmekenBaboonOutline",
		section = SECTION_APMEKEN
	)
	default HighlightMode apmekenBaboonOutline()
	{
		return HighlightMode.OFF;
	}

	@ConfigItem(
		name = "Outline Width",
		description = "Highlight the tiles of the explode radius.",
		position = 2,
		keyName = "apmekenBaboonOutlineWidth",
		section = SECTION_APMEKEN
	)
	default int apmekenBaboonOutlineWidth()
	{
		return 2;
	}

	@ConfigItem(
		name = "Melee Baboon",
		description = "Color to highlight the melee baboon.",
		position = 3,
		keyName = "apemekenBaboonColorMelee",
		section = SECTION_APMEKEN
	)
	@Alpha
	default Color apmekenBaboonColorMelee()
	{
		return new Color(0x40FF0000, true);
	}

	@ConfigItem(
		name = "Range Baboon",
		description = "Color to highlight the range baboon.",
		position = 4,
		keyName = "apemekenBaboonColorRange",
		section = SECTION_APMEKEN
	)
	@Alpha
	default Color apmekenBaboonColorRange()
	{
		return new Color(0x4000FF00, true);
	}

	@ConfigItem(
		name = "Mage Baboon",
		description = "Color to highlight the mage baboon.",
		position = 5,
		keyName = "apemekenBaboonColorMage",
		section = SECTION_APMEKEN
	)
	@Alpha
	default Color apmekenBaboonColorMage()
	{
		return new Color(0x400000FF, true);
	}

	@ConfigItem(
		name = "Shaman Baboon",
		description = "Color to highlight the shaman baboon.",
		position = 6,
		keyName = "apemekenBaboonColorShaman",
		section = SECTION_APMEKEN
	)
	@Alpha
	default Color apmekenBaboonColorShaman()
	{
		return new Color(0x4000FFFF, true);
	}

	@ConfigItem(
		name = "Thrall Baboon",
		description = "Color to highlight the thrall baboon.",
		position = 7,
		keyName = "apemekenBaboonColorThrall",
		section = SECTION_APMEKEN
	)
	@Alpha
	default Color apmekenBaboonColorThrall()
	{
		return new Color(0x0000FFFF, true);
	}

	@ConfigItem(
		name = "Cursed Baboon",
		description = "Color to highlight the cursed baboon.",
		position = 8,
		keyName = "apemekenBaboonColorCursed",
		section = SECTION_APMEKEN
	)
	@Alpha
	default Color apmekenBaboonColorCursed()
	{
		return new Color(0x40FF00FF, true);
	}

	@ConfigItem(
		name = "Volatile Baboon Radius",
		description = "Highlight the tiles of the explode radius.",
		position = 9,
		keyName = "apmekenVolatileBaboonTiles",
		section = SECTION_APMEKEN
	)
	default boolean apmekenVolatileBaboonTiles()
	{
		return false;
	}

	@ConfigItem(
		name = "Volatile Baboon",
		description = "Color to highlight the volatile baboon.",
		position = 10,
		keyName = "apemekenBaboonColorVolatile",
		section = SECTION_APMEKEN
	)
	@Alpha
	default Color apmekenBaboonColorVolatile()
	{
		return new Color(0x40FFC800, true);
	}

	// Het

	@ConfigItem(
		keyName = "hetBeamTimerEnable",
		name = "Beam Timer",
		description = "<html>Display an overlay of when the Caster Statue will fire." +
			"<br/>Click Het's Seal from one tile away when the indicator is GREEN to get an extra damage tick.</html>",
		position = 0,
		section = SECTION_HET
	)
	default boolean hetBeamTimerEnable()
	{
		return true;
	}

	@ConfigItem(
		keyName = "hetSolverEnable",
		name = "Mirror Puzzle Solver",
		description = "Show where to place/clean mirrors for the active puzzle layout.",
		position = 1,
		section = SECTION_HET
	)
	default boolean hetSolverEnable()
	{
		return true;
	}

	String KEY_HET_PICKAXE_MENU_SWAP = "hetPickaxeMenuSwap";

	@ConfigItem(
		keyName = KEY_HET_PICKAXE_MENU_SWAP,
		name = "Deposit-Pickaxe",
		description = "Automatically swap to Deposit-pickaxe when a pickaxe is in your inventory.",
		position = 2,
		section = SECTION_HET
	)
	default boolean hetPickaxeMenuSwap()
	{
		return true;
	}

	String KEY_HET_PICKAXE_PREVENT_EXIT = "hetPickaxePreventExit";

	@ConfigItem(
		keyName = KEY_HET_PICKAXE_PREVENT_EXIT,
		name = "Prevent Room Exit",
		description = "Deprioritize the option to leave the puzzle room until you have deposited your pickaxe in the statue.",
		position = 3,
		section = SECTION_HET
	)
	default boolean hetPickaxePreventExit()
	{
		return false;
	}

	@ConfigItem(
		keyName = "hetPickaxePreventRaidStart",
		name = "Prevent Raid Start",
		description = "Deprioritize the option to enter the raid until you have deposited your pickaxe in the lobby wall cavity.",
		position = 4,
		section = SECTION_HET
	)
	default boolean hetPickaxePreventRaidStart()
	{
		return false;
	}

	@ConfigItem(
		keyName = "hetPickaxePuzzleOverlay",
		name = "Puzzle Room Visual Warning",
		description = "Add a visual warning reminder to deposit your pickaxe at the end of the mirror puzzle room.",
		position = 5,
		section = SECTION_HET
	)
	default boolean hetPickaxePuzzleOverlay()
	{
		return false;
	}

	@ConfigItem(
		keyName = "hetPickaxeLobbyOverlay",
		name = "Lobby Visual Warning",
		description = "Add a visual warning reminder to deposit your pickaxe in the raid lobby.",
		position = 6,
		section = SECTION_HET
	)
	default boolean hetPickaxeLobbyOverlay()
	{
		return false;
	}

	// Scabaras

	@ConfigItem(
		keyName = "scabarasHelperMode",
		name = "Scabaras Helpers",
		description = "Puzzle helpers for the Path of Scabaras (leading to Kephri).",
		position = 0,
		section = SECTION_SCABARAS
	)
	default ScabarasHelperMode scabarasHelperMode()
	{
		return ScabarasHelperMode.OVERLAY;
	}

	@ConfigItem(
		keyName = "scabarasAdditionTileColor",
		name = "Addition Colour",
		description = "Highlight colour for tiles in the addition puzzle." +
			"<br/>Set alpha to 0 to disable.",
		position = 1,
		section = SECTION_SCABARAS
	)
	@Alpha
	default Color scabarasAdditionTileColor()
	{
		return Color.red;
	}

	@ConfigItem(
		keyName = "scabarasLightTileColor",
		name = "Light Flip Colour",
		description = "Highlight colour for tiles in the light flips puzzle." +
			"<br/>Set alpha to 0 to disable.",
		position = 2,
		section = SECTION_SCABARAS
	)
	@Alpha
	default Color scabarasLightTileColor()
	{
		return Color.red;
	}

	@ConfigItem(
		keyName = "scabarasObeliskColor1",
		name = "Obelisk Start",
		description = "Start colour for highlighting the obelisks in the obelisk puzzle." +
			"<br/>Set alpha to 0 to disable.",
		position = 3,
		section = SECTION_SCABARAS
	)
	@Alpha
	default Color scabarasObeliskColorStart()
	{
		return Color.cyan;
	}

	@ConfigItem(
		keyName = "scabarasObeliskColor2",
		name = "Obelisk End",
		description = "End colour for highlighting the obelisks in the obelisk puzzle." +
			"<br/>Set alpha to 0 to disable.",
		position = 4,
		section = SECTION_SCABARAS
	)
	@Alpha
	default Color scabarasObeliskColorEnd()
	{
		return Color.blue;
	}

	@ConfigItem(
		keyName = "scabarasSequenceColor1",
		name = "Sequence Start",
		description = "Start colour for highlighting the tiles in the sequence (simon says) puzzle." +
			"<br/>Set alpha to 0 to disable.",
		position = 5,
		section = SECTION_SCABARAS
	)
	@Alpha
	default Color scabarasSequenceColorStart()
	{
		return Color.cyan;
	}

	@ConfigItem(
		keyName = "scabarasSequenceColor2",
		name = "Sequence End",
		description = "End colour for highlighting the tiles in the sequence (simon says) puzzle." +
			"<br/>Set alpha to 0 to disable.",
		position = 6,
		section = SECTION_SCABARAS
	)
	@Alpha
	default Color scabarasSequenceColorEnd()
	{
		return Color.blue;
	}

	@ConfigItem(
		keyName = "scabarasMatchingDisplayMode",
		name = "Matching Display",
		description = "Whether to show highlight tiles, show names of tiles, or both for the matching puzzle.",
		position = 7,
		section = SECTION_SCABARAS
	)
	default MatchingTileDisplayMode scabarasMatchingDisplayMode()
	{
		return MatchingTileDisplayMode.BOTH;
	}

	@ConfigItem(
		keyName = "scabarasMatchingCompletedOpacity",
		name = "Matched Opacity",
		description = "Opacity (transparency) of completed tiles in the matching puzzle." +
			"<br/>Set to 0 to hide completed tiles completely.",
		position = 8,
		section = SECTION_SCABARAS
	)
	@Range(
		min = 0,
		max = 255
	)
	default int scabarasMatchingCompletedOpacity()
	{
		return 64;
	}

	@ConfigItem(
		keyName = "scabarasHighlightSkipObeliskEntry",
		name = "Show Obelisk Skip",
		description = "Highlight which entrance will skip requiring the obelisk puzzle.",
		position = 9,
		section = SECTION_SCABARAS
	)
	default SkipObeliskOverlay.EnableMode scabarasHighlightSkipObeliskEntry()
	{
		return SkipObeliskOverlay.EnableMode.OFF;
	}

	// Burial Tomb

	@ConfigItem(
		keyName = "leftClickBankAll",
		name = "Bank-all Single Click",
		description = "Allows you to Bank-all loot without requiring a second click on the minimenu.",
		section = SECTION_BURIAL_TOMB,
		position = 0
	)
	default boolean leftClickBankAll()
	{
		return false;
	}

	@ConfigItem(
		keyName = "chestAudioEnable",
		name = "Purple Chest Audio",
		description = "<html>Either disables the feature or plays an audio file whenever the purple chest is opened." +
			"<br/>The custom audio file should be named toa-chest.wav inside the .runelite/tombs-of-amascut folder</html>",
		section = SECTION_BURIAL_TOMB,
		position = 1
	)
	default boolean chestAudioEnable()
	{
		return false;
	}

	String CHEST_AUDIO_VOLUME_KEY = "chestAudioVolume";

	@Range(
		max = 200
	)
	@ConfigItem(
		keyName = CHEST_AUDIO_VOLUME_KEY,
		name = "Audio Volume",
		description = "Adjusts how loud the chest audio is when played. 100 is no change to file volume.",
		section = SECTION_BURIAL_TOMB,
		position = 2
	)
	default int chestAudioVolume()
	{
		return 100;
	}

	String SARCOPHAGUS_RECOLOR_WHITE = "sarcophagusRecolorWhite";

	@ConfigItem(
		name = "Recolour White Chest",
		description = "Recolour the white sarcophagus.",
		position = 3,
		keyName = SARCOPHAGUS_RECOLOR_WHITE,
		section = SECTION_BURIAL_TOMB
	)
	default boolean sarcophagusRecolorWhite()
	{
		return false;
	}

	String SARCOPHAGUS_WHITE_RECOLOR = "sarcophagusWhiteRecolor";

	@ConfigItem(
		name = "White Colour",
		description = "Colour to replace the white sarcophagus.",
		position = 4,
		keyName = SARCOPHAGUS_WHITE_RECOLOR,
		section = SECTION_BURIAL_TOMB
	)
	default Color sarcophagusWhiteRecolor()
	{
		return new Color(237, 177, 23);
	}

	String SARCOPHAGUS_RECOLOR_MY_PURPLE = "sarcophagusRecolorMyPurple";

	@ConfigItem(
		name = "Recolour Purple Chest (Mine)",
		description = "Recolour the purple sarcophagus." +
			"<br>When the loot is mine.",
		position = 5,
		keyName = SARCOPHAGUS_RECOLOR_MY_PURPLE,
		section = SECTION_BURIAL_TOMB
	)
	default boolean sarcophagusRecolorMyPurple()
	{
		return false;
	}

	String SARCOPHAGUS_MY_PURPLE_RECOLOR = "sarcophagusMyPurpleRecolor";

	@ConfigItem(
		name = "Purple Colour (Mine)",
		description = "Colour to replace the purple sarcophagus." +
			"<br>When the loot is mine.",
		position = 6,
		keyName = SARCOPHAGUS_MY_PURPLE_RECOLOR,
		section = SECTION_BURIAL_TOMB
	)
	default Color sarcophagusMyPurpleRecolor()
	{
		return new Color(192, 20, 124);
	}

	String SARCOPHAGUS_RECOLOR_OTHER_PURPLE = "sarcophagusRecolorOtherPurple";

	@ConfigItem(
		name = "Recolour Purple Chest (Other)",
		description = "Recolour the purple sarcophagus." +
			"<br>When the loot is NOT mine.",
		position = 7,
		keyName = SARCOPHAGUS_RECOLOR_OTHER_PURPLE,
		section = SECTION_BURIAL_TOMB
	)
	default boolean sarcophagusRecolorOtherPurple()
	{
		return false;
	}

	String SARCOPHAGUS_OTHER_PURPLE_RECOLOR = "sarcophagusOtherPurpleRecolor";

	@ConfigItem(
		name = "Purple Colour (Other)",
		description = "Colour to replace the purple sarcophagus." +
			"<br>When the loot is NOT mine.",
		position = 8,
		keyName = SARCOPHAGUS_OTHER_PURPLE_RECOLOR,
		section = SECTION_BURIAL_TOMB
	)
	default Color sarcophagusOtherPurpleRecolor()
	{
		return new Color(17, 88, 152);
	}

	@ConfigItem(
		name = "Detect Cursed Phalanx",
		description = "Prevents opening chests if player is carrying a cursed phalanx" +
			"<br>or Osmumten's fang (or).",
		position = 9,
		keyName = "cursedPhalanxDetect",
		section = SECTION_BURIAL_TOMB
	)
	default boolean cursedPhalanxDetect()
	{
		return false;
	}

	@ConfigItem(
		name = "Track Purple Dry Count",
		description = "Show purple dry streak count in chat upon raid completion.",
		position = 10,
		keyName = "trackPurpleDryCount",
		section = SECTION_BURIAL_TOMB
	)
	default boolean trackPurpleDryCount()
	{
		return false;
	}

	// Invocation Presets

	@ConfigItem(
		keyName = "invocationPresetsEnable",
		name = "Enable Presets",
		description = "Allows for saving and restoring of invocation presets. Right-click \"Preset\" button to save/load.",
		section = SECTION_INVOCATION_PRESETS,
		position = 0
	)
	default boolean invocationPresetsEnable()
	{
		return false;
	}

	@ConfigItem(
		keyName = "invocationPresetsScroll",
		name = "Auto-Scroll",
		description = "Automatically scroll to invocations which need to be changed for the current preset.",
		section = SECTION_INVOCATION_PRESETS,
		position = 1
	)
	default boolean invocationPresetsScroll()
	{
		return true;
	}

	// Invocation Screenshot

	@ConfigItem(
		keyName = "invocationScreenshotEnable",
		name = "Enable Screenshot button",
		description = "Adds a button to the ToA Invocation interface that will copy all invocations as an image to your system clipboard",
		section = SECTION_INVOCATION_SCREENSHOT,
		position = 0
	)
	default boolean invocationScreenshotEnable()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showRewardsSection",
		name = "Show Rewards Section",
		description = "<html>Should the rewards section be included<br/>(requires the Reward button to be selected within the interface)</html>",
		section = SECTION_INVOCATION_SCREENSHOT,
		position = 1
	)
	default boolean showRewardsSection()
	{
		return true;
	}

	@ConfigItem(
		keyName = "useResourcePack",
		name = "Use Resource Pack",
		description = "Use Resource Pack Theme for screenshot background",
		section = SECTION_INVOCATION_SCREENSHOT,
		position = 2
	)
	default boolean useResourcePack()
	{
		return true;
	}

	// Points Tracker

	@ConfigItem(
		keyName = "pointsTrackerOverlayEnable",
		name = "Enable Overlay",
		description = "Show points earned within the raid.",
		position = 0,
		section = SECTION_POINTS_TRACKER
	)
	default boolean pointsTrackerOverlayEnable()
	{
		return true;
	}

	@ConfigItem(
		keyName = "pointsTrackerShowRoomPoints",
		name = "Separate Room Points",
		description = "Show points for the current room separate from total points.",
		position = 1,
		section = SECTION_POINTS_TRACKER
	)
	default boolean pointsTrackerShowRoomPoints()
	{
		return false;
	}

	@ConfigItem(
		keyName = "pointsTrackerShowUniqueChance",
		name = "Show Unique %",
		description = "Show unique chance on the overlay.",
		position = 2,
		section = SECTION_POINTS_TRACKER
	)
	default boolean pointsTrackerShowUniqueChance()
	{
		return true;
	}

	@ConfigItem(
		keyName = "pointsTrackerShowPetChance",
		name = "Show Pet %",
		description = "Show pet chance on the overlay.",
		position = 3,
		section = SECTION_POINTS_TRACKER
	)
	default boolean pointsTrackerShowPetChance()
	{
		return false;
	}

	@ConfigItem(
		keyName = "pointsTrackerPostRaidMessage",
		name = "Points Total Message",
		description = "Show the total points in chat after the raid, akin to the Chambers of Xeric.",
		position = 4,
		section = SECTION_POINTS_TRACKER
	)
	default boolean pointsTrackerPostRaidMessage()
	{
		return true;
	}

	@ConfigItem(
		keyName = "pointsTrackerAllowExternal",
		name = "Send to External Plugins",
		description = "Sends the points totals to other locally installed plugins on raid completion." +
			"<br />Disabling this may prevent other plugins from working properly.",
		position = 5,
		section = SECTION_POINTS_TRACKER
	)
	default boolean pointsTrackerAllowExternal()
	{
		return true;
	}

	// Time Tracking

	@ConfigItem(
		keyName = "targetTimeDisplay",
		name = "Target Time in Timer",
		description = "Expand the in-raid timer to also show the target time to beat.",
		position = 0,
		section = SECTION_TIME_TRACKING
	)
	default boolean targetTimeDisplay()
	{
		return true;
	}

	@ConfigItem(
		keyName = "splitsMessage",
		name = "Splits Post-Raid Message",
		description = "Show room splits in a chat message at the end of the raid. Path shows boss completion times, room shows each individual room (can be very long).",
		position = 1,
		section = SECTION_TIME_TRACKING
	)
	default SplitsMode splitsMessage()
	{
		return SplitsMode.OFF;
	}

	@ConfigItem(
		keyName = "splitsOverlay",
		name = "Splits Overlay",
		description = "Show room splits in an on-screen overlay. Path shows boss completion times, room shows each individual room (can be very long).",
		position = 2,
		section = SECTION_TIME_TRACKING
	)
	default SplitsMode splitsOverlay()
	{
		return SplitsMode.OFF;
	}

	// Miscellaneous

	String KEY_QUICK_PROCEED_ENABLE_MODE = "quickProceedEnableMode";

	@ConfigItem(
		keyName = KEY_QUICK_PROCEED_ENABLE_MODE,
		name = "Quick Proceed",
		description = "Left click proceed/begin/leave on Osmumten and quick-enter/quick-use entryways and teleport crystals.",
		position = 0,
		section = SECTION_MISCELLANEOUS
	)
	default QuickProceedEnableMode quickProceedEnableMode()
	{
		return QuickProceedEnableMode.ALL;
	}

	String KEY_HP_ORB_MODE = "hpOrbsMode";

	@ConfigItem(
		keyName = KEY_HP_ORB_MODE,
		name = "HP Orbs",
		description = "Removes HP orbs from the screen or replaces them with health bars.",
		position = 1,
		section = SECTION_MISCELLANEOUS
	)
	default HpOrbMode hpOrbsMode()
	{
		return HpOrbMode.ORBS;
	}

	@ConfigItem(
		keyName = "showUpdateMessages",
		name = "Show Updates",
		description = "Opens a panel describing plugin updates after new features are added to the plugin.",
		position = 3,
		section = SECTION_MISCELLANEOUS
	)
	default boolean showUpdateMessages()
	{
		return true;
	}

	@ConfigItem(
		keyName = "hideFadeTransition",
		name = "Hide Fade Transition",
		description = "Hides the fade transition between loading zones.",
		position = 4,
		section = SECTION_MISCELLANEOUS
	)
	default boolean hideFadeTransition()
	{
		return false;
	}

	@ConfigItem(
		keyName = "smellingSaltsCooldown",
		name = "Salts Cooldown",
		description = "After using Smelling salts, prevents re-using them for this long.",
		position = 5,
		section = SECTION_MISCELLANEOUS
	)
	@Units(Units.SECONDS)
	@Range(
		min = 0,
		max = 480
	)
	default int smellingSaltsCooldown()
	{
		return 15;
	}

	@ConfigItem(
		keyName = "disableCameraShake",
		name = "Disable Camera Shake",
		description = "Disables camera shake effects at P4 Wardens.",
		position = 6,
		section = SECTION_MISCELLANEOUS
	)
	default boolean disableCameraShake()
	{
		return false;
	}

	@ConfigItem(
			name = "Font Style",
			description = "Font style of most text overlays.",
			position = 3,
			keyName = "fontStyle",
			section = SECTION_MISCELLANEOUS
	)
	default FontStyle fontStyle()
	{
		return FontStyle.BOLD;
	}

	@ConfigItem(
			name = "Font Size",
			description = "Font size of most text overlays.",
			position = 4,
			keyName = "fontSize",
			section = SECTION_MISCELLANEOUS
	)
	@Units(Units.PIXELS)
	@Range(min = 12)
	default int fontSize()
	{
		return 12;
	}

	@Alpha
	@ConfigItem(
			name = "Danger Outline",
			description = "Color used to mark \"dangerous\" tiles.",
			position = 5,
			keyName = "dangerOutlineColor",
			section = SECTION_MISCELLANEOUS
	)
	default Color dangerOutlineColor()
	{
		return Color.RED;
	}

	@Alpha
	@ConfigItem(
			name = "Danger Fill",
			description = "Color used to mark \"dangerous\" tiles.",
			position = 6,
			keyName = "dangerFillColor",
			section = SECTION_MISCELLANEOUS
	)
	default Color dangerFillColor()
	{
		return new Color(255, 0, 0, 20);
	}

	@Alpha
	@ConfigItem(
			name = "Tile Outline",
			description = "Color used to outline npc tiles.",
			position = 7,
			keyName = "tileOutlineColor",
			section = SECTION_MISCELLANEOUS
	)
	default Color tileOutlineColor()
	{
		return Color.BLACK;
	}

	@Alpha
	@ConfigItem(
			name = "Tile Fill",
			description = "Color used to fill npc tiles.",
			position = 8,
			keyName = "tileFillColor",
			section = SECTION_MISCELLANEOUS
	)
	default Color tileFillColor()
	{
		return new Color(0, 0, 0, 10);
	}

	// Hidden

	@ConfigItem(
		keyName = "updateNotifierLastVersion",
		name = "",
		description = "",
		hidden = true
	)
	default int updateNotifierLastVersion()
	{
		return UpdateNotifier.TARGET_VERSION - 1;
	}

	@ConfigItem(
		keyName = "updateNotifierLastVersion",
		name = "",
		description = "",
		hidden = true
	)
	void updateNotifierLastVersion(int newVersion);

	@ConfigItem(
		keyName = "purpleDryStreakCount",
		name = "",
		description = "",
		hidden = true
	)
	default int getPurpleDryStreakCount()
	{
		return 0;
	}

	@ConfigItem(
		keyName = "purpleDryStreakCount",
		name = "",
		description = "",
		hidden = true
	)
	void setPurpleDryStreakCount(int count);

	// Prayer

	@ConfigItem(
			name = "Prayer Mode",
			description = "Types of overlays to use to indicate prayers.",
			position = 0,
			keyName = "prayerMode",
			section = SECTION_PRAYER
	)
	default PrayerMode prayerMode()
	{
		return PrayerMode.OFF;
	}

	@ConfigItem(
			name = "Descending Boxes (Guitar Hero)",
			description = "Overlay descending boxes on prayer widgets.",
			position = 2,
			keyName = "prayerDescendingBoxes",
			section = SECTION_PRAYER
	)
	default boolean prayerDescendingBoxes()
	{
		return false;
	}

	@ConfigItem(
			name = "Non-Priority Boxes (Guitar Hero)",
			description = "Show boxes for all upcoming attacks." +
					"<br>Requires descending boxes to be enabled.",
			position = 3,
			keyName = "prayerNonPriority",
			section = SECTION_PRAYER
	)
	default boolean prayerNonPriorityBoxes()
	{
		return false;
	}

	@Alpha
	@ConfigItem(
			name = "Box Color",
			description = "Color of boxes that have > 1 ticks remaining.",
			position = 4,
			keyName = "prayerBoxColor",
			section = SECTION_PRAYER
	)
	default Color prayerBoxColor()
	{
		return Color.ORANGE;
	}

	@Alpha
	@ConfigItem(
			name = "Box Warning Color",
			description = "Color of boxes that have 1 tick remaining.",
			position = 5,
			keyName = "prayerBoxWarnColor",
			section = SECTION_PRAYER
	)
	default Color prayerBoxWarnColor()
	{
		return Color.RED;
	}


	// Enums

	@AllArgsConstructor
	enum PrayerMode
	{
		WIDGET("Widget"),
		INFO_BOX("Infobox"),
		ALL("All"),
		OFF("Off");

		private final String name;

		@Override
		public String toString()
		{
			return name;
		}
	}

	@Getter
	@AllArgsConstructor
	enum FontStyle
	{
		BOLD("Bold", Font.BOLD),
		ITALIC("Italic", Font.ITALIC),
		PLAIN("Plain", Font.PLAIN);

		private final String name;
		private final int font;

		@Override
		public String toString()
		{
			return name;
		}
	}

	enum FireballRadius
	{
		AERIAL,
		DEFAULT,
		OFF
	}

	enum Tile
	{
		TILE,
		TRUE_TILE,
		OFF
	}

	enum QuickProceed
	{
		ON,
		SPEEDRUN,
		OFF
	}

	enum AttackCounter
	{
		MEDIC,
		DEFAULT,
		OFF
	}
}

