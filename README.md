# BetterBlockBreaking

A paper library to aid in manually ticking block breaking

Tested with paper 1.18.2, requires ProtocolLib v4.7.0

For questions, add me on discord U9G#0670

# How to use it in a project:

1. Clone this repo locally
2. run the publishToMavenLocal action
3. in the repositories of your own plugin, add mavenLocal() below mavenCentral()
4. add to your plugin.yml the line `depend: [ProtocolLib]`

# How to use this in your code:

1. In the onEnable method of your plugin, add the line: `BlockBreakManager blockBreakManager = new BlockBreakManager(this, numberOfBlocksToKeepBreakProgressPerPlayer);`

# API:

## Events

### PlayerBreakBlockEvent

When a player breaks a block, you can cancel the block break (don't actually break the block), change the type of block that will be put here after.

### PlayerDigBlockEvent

Allows you to cancel digging the block (not show break progress) or change the tickSize (allows you to get finite control over how fast or slow the blocks break)
