import dev.scaffoldit.hytale.wire.HytaleManifest

rootProject.name = "Codex"

plugins {
    // See documentation on https://scaffoldit.dev
    id("dev.scaffoldit") version "0.2.+"
}

// Would you like to do a split project?
// Create a folder named "common", then configure details with `common { }`

hytale {
    usePatchline("release")
    useVersion("latest")

    repositories {
        // Any external repositories besides: MavenLocal, MavenCentral, HytaleMaven, and CurseMaven
    }

    dependencies {
        // Any external dependency you also want to include
    }

    manifest {
        Group = "Exotik850"
        Name = "Codex"
        Main = "dev.byt3.codex.CodexPlugin"
        Description = "Adds a User-friendly Mod Settings GUI that can be used by any mod, and is also used by the Codex Mod itself."
        Version = "0.0.1"
        Authors = listOf(HytaleManifest.Author("Exotik850"))
        ServerVersion = "2026.02.19-1a311a592"
        IncludesAssetPack = true
    }
}