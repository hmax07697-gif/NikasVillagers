# NikasVillagers

![Minecraft Version](https://img.shields.io/badge/Minecraft-1.19+-green.svg)
![Java Version](https://img.shields.io/badge/Java-17+-orange.svg)
![License](https://img.shields.io/badge/License-MIT-blue.svg)

A Spigot plugin that automatically generates unique, persistent names for villagers based on their profession and biome.

## Features

- Villagers are automatically named on spawn
- Different name pools per profession (Librarian, Blacksmith, etc.)
- Names vary based on the villager's spawn biome
- 5% chance for legendary prefixes like "Elder", "Master", or "Wise"
- Profession level titles (Novice, Apprentice, Journeyman, Expert, Master)
- Names persist through curing, trading, chunk unloading, and restarts via PDC

### Name Formats

- **Simple**: `Aldric`
- **Titled**: `Aldric the Librarian`
- **Full Name**: `Aldric Thornwood`
- **With level**: `Aldric the Novice Librarian`
- **Legendary**: `Elder Marcus the Master Blacksmith`

## Installation

1. Download the latest release from the [Releases](../../releases) page
2. Place `NamedVillagers.jar` in your server's `plugins/` folder
3. Restart your server
4. Optionally configure name pools in `plugins/NamedVillagers/config.yml`

## Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/nv <name>` | Rename the villager you're looking at | `namedvillagers.rename` |
| `/nv random` | Give a random name to the villager you're looking at | `namedvillagers.rename` |
| `/nv reload` | Reload the plugin configuration | `namedvillagers.reload` |

**Aliases**: `/namedvillagers`, `/villagernames`

## Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `namedvillagers.rename` | Allows renaming villagers with `/nv` | op |
| `namedvillagers.reload` | Allows reloading the configuration | op |
| `namedvillagers.bypass` | Bypass auto-naming on spawn | false |

## Configuration

```yaml
settings:
  auto-name: true
  legendary-chance: 5
  format: "titled"           # simple, titled, or fullname
  show-level: true
  rename-on-cure: false
  biome-specific: true

names:
  professions:
    librarian:
      first: ["Aldric", "Beatrice", "Cedric", ...]
      last: ["Bookwright", "Thornwood", "Inkwell", ...]
    blacksmith:
      first: ["Bjorn", "Thora", "Gunnar", ...]
      last: ["Ironforge", "Steelheart", "Anvil", ...]

biomes:
  desert:
    first: ["Zahir", "Farah", "Rashid", ...]
  taiga:
    first: ["Olaf", "Astrid", "Sven", ...]

legendary-prefixes: ["Elder", "Master", "Wise", "Ancient", "Renowned"]
```

## Building from Source

Requires Java 17+ and Maven 3.6+.

```bash
git clone https://github.com/hmax07697-gif/NamedVillagers.git
cd NamedVillagers
mvn clean package
# Output: target/NamedVillagers-1.0.0.jar
```

## Compatibility

- **Minecraft**: 1.19+
- **Server Software**: Spigot, Paper, Purpur
- **Java**: 17+

## License

MIT — see [LICENSE](LICENSE) for details.
