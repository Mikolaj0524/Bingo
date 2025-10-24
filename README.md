# 🎲 Bingo

![GitHub Release (latest by date)](https://img.shields.io/github/v/release/Mikolaj0524/Bingo?label=latest%20release)
![GPL v3 License](https://img.shields.io/badge/License-GPLv3-blue)
![Spigot](https://img.shields.io/badge/Server-Spigot%201.21.4-brightgreen)
![Paper](https://img.shields.io/badge/Server-Paper%201.21.4-blueviolet)
![Java](https://img.shields.io/badge/Java-17+-orange)
![PlaceholderAPI](https://img.shields.io/badge/PlaceholderAPI-Supported-success)

A fast-paced minigame where players are teleported into a generated Bingo world and race to collect items or blocks displayed on their map in the right-hand slot. Players can compete for either a **Line** (collect 5 in a row) or **Full House** (collect all 25 items). The plugin automatically generates the world and handles gameplay.

---

## ⚙️ Features

* Automatic Bingo world generation
* Supports **Line** and **Full House** game modes
* Collect items/blocks based on the in-hand map
* Supports PlaceholderAPI for scoreboards or displays
* Fully configurable materials and messages
* Simple setup and intuitive gameplay
* Compatible with Spigot 1.21.4 (runs on Paper as well)

---

## 🔐 Permissions

| Permission    | Description              |
| ------------- | ------------------------ |
| `bingo.start` | Allows starting the game |

---

## 🧩 PlaceholderAPI Placeholders

| Placeholder        | Description                         |
| ------------------ | ----------------------------------- |
| `%bingo_cardtype%` | Card type (`Line` or `Full House`)  |
| `%bingo_gametime%` | Time elapsed since the game started |
| `%bingo_queue%`    | Number of players in the queue      |

---

## 🧱 Configuration (`config.yml`)

Example configuration:

```yaml
spawn:
  x: 0
  y: 101
  z: 0
  yaw: 90

signs:
  game:
    x: -9
    y: 102
    z: 0
    facing: "EAST"
  start:
    x: -9
    y: 101
    z: 0
    facing: "EAST"
  type:
    x: -9
    y: 103
    z: 0
    facing: "EAST"

map:
  width: 5000
  height: 5000

messages:
  color: "&7"
  prefix: " &8[&6&lBingo&8] &6» &7"
  welcome: "Welcome!"
  not_enough_players: "Not enough players!"
  no_perms: "You don't have enough permissions!"
  player_died: "%player% died!"
  game_end: "Game end!"
  start_text: "Get BINGO!"
  player_completed_bingo: "%player% completed bingo!"
  someone_won: "%player% won!"
  teleporting: "Teleporting!"
  starting: "Starting the game!"
  queue_join: "You have joined the queue!"
  queue_leave: "You have left the queue!"
  server_join: "%player% joined the game!"
  server_leave: "%player% has left the game!"

materials:
  - DIRT
  - STONE
  - ENDER_PEARL
  - SUGAR_CANE
  - OAK_SIGN
  - WOODEN_SHOVEL
  - REDSTONE
  - RAIL
  - BRICK
  - YELLOW_DYE
  - STONE_SWORD
  - IRON_SWORD
  - WOODEN_AXE
  - GOLDEN_HELMET
  - CHAINMAIL_CHESTPLATE
  - BEETROOT_SOUP
  - RED_MUSHROOM
  - RAIL
  - LADDER
  - PORKCHOP
  - TROPICAL_FISH
  - PAPER
  - FEATHER
  - STONE_HOE
  - LADDER
  - BLACK_DYE
  - GREEN_DYE
```

---

## 🧠 Setup Instructions

1. Place all textures of the materials used in the config into:

```
plugins/Bingo/images/
```

For example, if your config has `STONE`, you need `plugins/Bingo/images/stone.png`.

2. Configure the **spawn**, **signs**, **map size**, **materials**, and **messages** in `config.yml`.

3. Players can start the game by clicking the **start sign**.

4. Players will be teleported into the generated Bingo world automatically.

5. Collect the items/blocks shown on the right-hand map to complete either a **Line** or **Full House**.

---

## 🧾 Notes

* Fully automated game logic; no commands are required.
* Supports PlaceholderAPI for real-time stats and displays.
* Designed for **Spigot 1.21.4** but runs on Paper as well.

---

## 📦 License

This project is licensed under the **[GPL v3 License](LICENSE)**.

---

`Readme by ChatGPT`