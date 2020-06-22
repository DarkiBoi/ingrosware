<h1 align="center">IngrosWare</h1>
<div align="center">
  <strong> 1.12 Minecraft injection cheat.</strong>
</div>
<br />

# Requirements
- **JDK 8+**
- **Git** __(optional)__ 

# How to setup
Clone the workspace
```
git clone https://github.com/Ingros/ingrosware.git
```
Go into folder and open up a command prompt and do

**Eclipse** -
gradlew setupDevWorkspace eclipse build

**Intelij** -
gradlew setupDevWorkspace idea genIntellijRuns build

# Open in IDE
**Eclipse**
```Right click -> New -> Java Project -> Browse location -> Select IngrosWare folder -> Finish```

**Intelij**
```Open -> Select IngrosWare folder -> Import gradle project```

# Run

Add ```-Dfml.coreMods.load=best.reich.ingrosware.mixin.IngrosLoader``` to VM options.
