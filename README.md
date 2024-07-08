# Zezimax Bot

## Overview

Zezimax is a bot script designed for RuneScape 3, created using BotWithUs injection and reflection capabilities. The bot reads memory addresses from the game in real time, and leverages a decision tree to determine its actions, allowing it to perform a variety of tasks that I am always adding to. This README provides an overview of the bot's features, how to set it up, and how it operates.

## Features

Decision Tree Logic: Zezimax uses a decision tree to select its next action based on various in-game conditions such as GP count, items in the bank, items in the inventory, skill levels, etc.

State Management: The bot maintains different states for each activity, ensuring smooth transitions between tasks.
Randomization: Zezimax uses a combination of implicit waits, condition-based waits, decision tree randomization, and X until randomization to ensure the bot acts without following easily detectable patterns.

Grand Exchange Logic: Zezimax can buy and sell items based on conditions at the Grand Exchange, and can fetch up-to-date pricing information for smart market pricing.

Automated Banking: Zezimax can automatically bank items, ensuring the inventory is managed efficiently.

Skill Training: The bot can train multiple skills including Mining, Smithing, Woodcutting, Firemaking, Fishing, Cooking, and Fletching.


## Installation

Clone the Repository:
```sh
git clone https://github.com/yourusername/zezimax-bot.git
```

Set Up Your Environment:
Ensure you have Kotlin installed.
Set up the BotWithUs API and its dependencies, create an account, and load the compiled script into the BotWithUs Script folder.
