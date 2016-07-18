
# Metadata Plugin Code Generator

## Running the Plugin

Simply execute the following

```bash
mvn org.jboss.perspicuus:metadata-maven-plugin:1.0-SNAPSHOT:sayhi
```

To reduce the amount you can add the package name into your ~/.m2/settings.xml:

```xml
<pluginGroups>
        <!--    pluginGroup  Specifies a further group identifier to use for plugin lookup.  -->
    <pluginGroup>org.jboss.perspicuus</pluginGroup>
</pluginGroups>
```

Short hand notation for using this plugin is as follows:

```bash
mvn metadata:sayhi
```



