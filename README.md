##Redstone Energy Field (for Minecraft 1.7.10)

Creates a live redstone energy field for mass powering equipment. Features multiple tiers of energy fields.


>Stable Builds: https://github.com/QMXTech/Redstone-Energy-Field/releases<br>
Latest Builds: (currently unavailable)<br>
Original Minecraft Forum Post: http://bit.ly/Y5mRan

####Known Issues:

* Disabling by redstone signal is currently broken, so tier 2 cells are currently useless, tier 3 only handles particle effects, tier 4 only allows one to select its range and disable/enable particle effects.
* Particle effects are only shown at cardinal directions at the last block of the range rather than covering the entire range.

####Recent Changes:

>2014-11-18 by Malacheye:

* Changed package names to reflect new development team.
* Modified particle effects to better represent full range.
* Other various tweaks.
* Bumped version to 0.8.2-alpha.

>2014-11-15 by Korynkai:

* Some work on particle effects, adds particle effects at set range in cardinal directions (N,S,E,W + top and bottom) (note: an additional block is still powered, as would be the case with redstone dust *This is a feature, not a bug, as it allows one to visualize the redstone signal pathway rather than show which blocks are powered, which can be confusing*)
* Bump version to 0.8.1-alpha
* Bump Forge to 10.13.2.1230
* Removed unnecessary references directory (this was moved to the src tree already...)

>2014-11-11 by Korynkai:

* Pulled in ForgeGradle
* Additional fixes for Minecraft Forge

