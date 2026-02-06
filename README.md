# NikasVillagers

A Minecraft Spigot plugin that automatically generates unique, persistent names for villagers based on their profession and biome.

![Minecraft Version](https://img.shields.io/badge/Minecraft-1.19+-green.svg)
![Java Version](https://img.shields.io/badge/Java-17+-orange.svg)
![License](https://img.shields.io/badge/License-MIT-blue.svg)

## Features

### 🎯 Core Features
- **Automatic Naming**: Villagers are automatically named upon spawn
- **Profession-Based Names**: Different name pools for each profession (Librarian, Blacksmith, etc.)
- **Biome-Themed Names**: Names vary based on the biome where the villager spawns
- **Persistent Names**: Names survive curing, trading, chunk unloading, and server restarts via PDC (PersistentDataContainer)
- **Legendary Prefixes**: 5% chance for special titles like "Elder", "Master", or "Wise"
- **Professional Titles**: Display villager profession levels (Novice, Apprentice, Journeyman, Expert, Master)

### 📝 Name Formats
The plugin generates names in three different formats:
- **Simple**: `"Aldric"`
- **Titled**: `"Aldric the Librarian"`
- **Full Name**: `"Aldric Thornwood"`

Level indicators are shown as:
- `"Aldric the Novice Librarian"`
- `"Elder Marcus the Master Blacksmith"`

## Installation

1. Download the latest release from the [Releases](../../releases) page
2. Place `NamedVillagers.jar` in your server's `plugins/` folder
3. Restart your server
4. Configure name pools in `plugins/NamedVillagers/config.yml` (optional)

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
| `namedvillagers.bypass` | Bypass name generation (no auto-naming) | false |

## Configuration

The `config.yml` file contains extensive customization options:

### Settings
```yaml
settings:
  auto-name: true                    # Automatically name villagers on spawn
  legendary-chance: 5                # Percentage chance for legendary prefix (0-100)
  format: "titled"                   # Name format: simple, titled, or fullname
  show-level: true                   # Show profession level (Novice, Master, etc.)
  rename-on-cure: false              # Rename zombie villagers when cured
  biome-specific: true               # Use biome-specific name pools
```

### Name Pools
Each profession has its own name pool, and names can vary by biome:

```yaml
names:
  professions:
    librarian:
      first: ["Aldric", "Beatrice", "Cedric", ...]
      last: ["Bookwright", "Thornwood", "Inkwell", ...]
    blacksmith:
      first: ["Bjorn", "Thora", "Gunnar", ...]
      last: ["Ironforge", "Steelheart", "Anvil", ...]
```

Biome pools allow for cultural naming variations:
```yaml
biomes:
  desert:
    first: ["Zahir", "Farah", "Rashid", ...]
  taiga:
    first: ["Olaf", "Astrid", "Sven", ...]
```

### Legendary Prefixes
Customize the rare legendary titles that appear before names:
```yaml
legendary-prefixes: ["Elder", "Master", "Wise", "Ancient", "Renowned"]
```

## How It Works

### Name Generation Process
1. **Villager Spawns**: When a villager spawns naturally or is bred
2. **Check Existing Name**: Plugin checks if villager already has a custom name via PDC
3. **Generate Name**: If no name exists, generate one based on:
   - Villager profession (Librarian, Farmer, etc.)
   - Spawn biome (Desert, Plains, Taiga, etc.)
   - Random legendary prefix (5% chance)
   - Profession level (Novice → Master)
4. **Store in PDC**: Name is saved to PersistentDataContainer with key `namedvillagers:custom_name`
5. **Apply Name**: Custom name is set on the villager entity

### Persistence Technology
The plugin uses Minecraft's **PersistentDataContainer (PDC)** to store custom names directly on villager entities. This means:
- ✅ Names survive server restarts
- ✅ Names survive chunk unloading/loading
- ✅ Names persist through zombie villager curing
- ✅ Names remain after trading interactions
- ✅ No external database required
- ✅ No performance impact from lookups

## Examples

### Desert Librarian
```
Zahir the Novice Librarian
```

### Plains Blacksmith with Legendary Prefix
```
Elder Bjorn the Master Blacksmith
```

### Taiga Farmer (Simple Format)
```
Astrid
```

### Swamp Cleric (Full Name Format)
```
Mordecai Nightshade the Expert Cleric
```

## Building from Source

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Build Steps
```bash
git clone https://github.com/hmax07697-gif/NamedVillagers.git
cd NamedVillagers
mvn clean package
```

The compiled JAR will be in `target/NamedVillagers-1.0.0.jar`

## Compatibility

- **Minecraft Version**: 1.19+
- **Server Software**: Spigot, Paper, Purpur
- **Java Version**: 17+


## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
