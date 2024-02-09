# NuVotifier-Fabric
NuVotifier is a secure alternative to using the original Votifier project. NuVotifier will work in place of Votifier.

NuVotifier-Fabric is an implementation of [NuVotifier](https://github.com/NuVotifier/NuVotifier) for Fabric. This 
implementation has been moved away from the main repository for two reasons:
1. This module has been PRed to the original repository, however the author has never published a release of it.
2. Due to the nature of Fabric, it requires regular updates to support the latest Minecraft version.

[Setup Guide](https://github.com/NuVotifier/NuVotifier/wiki/Setup-Guide)

[Troubleshooting Guide](https://github.com/NuVotifier/NuVotifier/wiki/Troubleshooting-Guide)

[Developer Information](https://github.com/NuVotifier/NuVotifier/wiki/Developer-Documentation)

# Developing a vote listener
## Adding NuVotifier-Fabric as a dependency to your build system

You can include NuVotifier-Fabric into your gradle project using the following lines:

Latest NuVotifier-Fabric version: [![](https://jitpack.io/v/DrexHD/NuVotifier-Fabric.svg)](https://jitpack.io/#DrexHD/NuVotifier-Fabric)

Latest Votifier version: [![](https://jitpack.io/v/NuVotifier/NuVotifier.svg)](https://jitpack.io/#NuVotifier/NuVotifier)
```gradle
repositories {
	maven { url 'https://jitpack.io' }
}

dependencies {
	implementation("com.github.NuVotifier.NuVotifier:nuvotifier-api:VERSION")
	implementation 'com.github.DrexHD:NuVotifier-Fabric:VERSION'
}
```

## Writing Vote Listeners
A vote listener registers the `VoteListener.EVENT`.
A basic vote listener looks something like this:
```java
package me.drex.fabricvotelistener;

import com.vexsoftware.votifier.fabric.event.VoteListener;
import net.fabricmc.api.DedicatedServerModInitializer;

public class TestVoteListener implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        VoteListener.EVENT.register(vote -> {
            System.out.println("Received " + vote);
        });
    }
}

```

Add `nuvotifier-fabric` as a dependency in your fabric.mod.json
```json
{
  "depends": {
    "nuvotifier-fabric": "*"
  }
}
```


# License

NuVotifier is GNU GPLv3 licensed. This project's license can be viewed [here](LICENSE).