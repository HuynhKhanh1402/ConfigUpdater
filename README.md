[![](https://jitpack.io/v/HuynhKhanh1402/ConfigUpdater.svg)](https://jitpack.io/#HuynhKhanh1402/ConfigUpdater)
# ConfigUpdater
Used to update files for Bukkit/Spigot API
## Example
```
    @Override
    public void onEnable() {
        //Make sure file is exist
        saveDefaultConfig();

        File file = new File(getDataFolder(), "config.yml");

        try {
            ConfigUpdater.update(file, Objects.requireNonNull(getResource("config.yml")), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
```
## Maven
```
<repository>
	<id>jitpack.io</id>
        <url>https://jitpack.io</url>
</repository>
```

```
<dependency>
	  <groupId>com.github.HuynhKhanh1402</groupId>
	  <artifactId>ConfigUpdater</artifactId>
	  <version>{version}</version>
</dependency>
```
## Gradle
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

```
dependencies {
	      implementation 'com.github.HuynhKhanh1402:ConfigUpdater:Tag'
}
```  
