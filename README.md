# Codex

**A universal settings framework for Hytale server mods.**

Codex gives players a single, polished in-game GUI to manage settings from every mod that supports it — and gives mod developers a dead-simple way to plug in without writing any UI code themselves.

---

## What It Does

- Provides a **unified `/settings` command** that opens a browsable, categorized settings menu.
- **Auto-generates UI pages** from your data codec — booleans become checkboxes, numbers become number fields, strings become text inputs. No `.ui` files to write.
- Persists settings per-player via Hytale's `EntityStore` component system.
- Ships with built-in **Hub Settings** (window size, compact mode, back-button visibility) so you can see it working immediately.
- Fully extensible: register your own `CodecUIProvider` for custom field types, or implement `PlayerSettingsProvider` for a completely custom page.

## Quick Start

### Requirements

- Java 25+ (JetBrains Runtime recommended)
- Hytale server (`2026.02.19-1a311a592` or later)
- [ScaffoldIt Gradle Plugin](https://scaffoldit.dev) `0.2.x`

### Installation

1. Download the latest `dev.byt3.codex.jar` from [CurseForge](#) (or build from source).
2. Drop it into your server's `mods/` folder.
3. Start (or restart) the server. Codex registers itself automatically.

### Building From Source

```bash
git clone https://github.com/Exotik850/Codex.git
cd Codex
./gradlew build          # Windows: .\gradlew.bat build
```

The compiled jar lands in `build/libs/`.

## Usage (Players)

Type `/settings` in chat (aliases: `/codexsettings`, `/codexconfig`, `/config`).  
A categorized menu opens. Click any category to view and edit its settings — changes apply immediately.

## Usage (Mod Developers)

Codex is designed to get out of your way. The fastest integration path is **three lines of code** in your plugin's `setup()`:

### 1. Define your config as a `BuilderCodec`

```java
public class MyModConfig implements Component<EntityStore> {
    public Boolean pvpEnabled;
    public Integer maxPartySize;
    public String welcomeMessage;

    public static final BuilderCodec<MyModConfig> CODEC =
        BuilderCodec.builder(MyModConfig.class, MyModConfig::new)
            .append(new KeyedCodec<>("PvpEnabled",      Codec.BOOLEAN), (o, v) -> o.pvpEnabled = v,      o -> o.pvpEnabled).add()
            .append(new KeyedCodec<>("MaxPartySize",     Codec.INTEGER), (o, v) -> o.maxPartySize = v,    o -> o.maxPartySize).add()
            .append(new KeyedCodec<>("WelcomeMessage",   Codec.STRING),  (o, v) -> o.welcomeMessage = v,  o -> o.welcomeMessage).add()
            .build();

    // ... clone(), getters, defaults ...
}
```

### 2. Register a component type and hand it to Codex

```java
@Override
public void setup() {
    // Register with Hytale's entity store
    ComponentType<EntityStore, MyModConfig> ct =
        this.getEntityStoreRegistry().registerComponent(MyModConfig.class, "MyModConfig", MyModConfig.CODEC);

    // Register with Codex — that's it
    PlayerSettingsRegistry.get().registerCodec("MyMod", MyModConfig.CODEC, ct);
}
```

Codex reads the codec, generates a settings page with the correct controls for each field type, and wires up persistence automatically. Your mod now has a "My Mod" category inside the `/settings` menu.

### Supported Field Types (Built-in)

| Codec            | UI Control   | Provider Class           |
| ---------------- | ------------ | ------------------------ |
| `Codec.BOOLEAN`  | Checkbox     | `BooleanCodecUIProvider` |
| `Codec.INTEGER`  | Number field | `NumberCodecUIProvider`  |
| `Codec.FLOAT`    | Number field | `NumberCodecUIProvider`  |
| `Codec.DOUBLE`   | Number field | `NumberCodecUIProvider`  |
| `Codec.STRING`   | Text input   | `StringCodecUIProvider`  |

### Custom Field Types

Need a slider, a dropdown, or a color picker? Implement `CodecUIProvider<T>` and register it:

```java
GeneratedSettingsRegistry.register(MyCodec.ENUM_CODEC, new MyEnumUIProvider());
```

Or if something we've made doesn't fit your needs, you can replace it with `put`:

```java
GeneratedSettingsRegistry.put(Codec.BOOLEAN, new MyCustomBooleanUIProvider());
```


### Fully Custom Pages

If auto-generation isn't enough, implement `PlayerSettingsProvider` directly and call `PlayerSettingsRegistry.get().registerProvider(...)`. You get full control over the UI build while still appearing in the Codex menu.

## License

MIT - see [LICENSE](LICENSE) for details.
