## Notice: Project inactive/dead

As there appears to be some confusion, this mod has not been in development in many years. The project is dead.

I know I announced an in-progress rework to overhaul a lot of the mod's structural problems, but that got cut short by a bunch of stuff that doesn't matter anymore, and I never got back to it afterwards.

Mainly this was a combination of burnout and lack of interest. Overhauling a broken buggy out-of-date mod by essentially rewriting most of it was a very bad decision. Especially so when my entire commitment to the project in the first place was in the code I had written, which was of very poor quality.

I never once used this in my own "real" playthroughs. The gameplay style was just never to my interest, so I never had any personal stake in keeping it up to date either. Eventually I just assumed everyone stopped trying to use it.

In any case, I don't plan on ever continuing development. I don't have much interest in Java development anymore and I most certainly don't have the time or patience to continue a hopeless overhaul that was never going to see the light of day.

I apologize for taking this long to make it clear, and I apologize for promising far more than I could handle.

However, this is licensed under GPL v3, and that alone grants full permission to copy the source code, modify it, and publish those modifications. **You don't need to ask**; that permission is already granted as part of the license.

That means that anyone can continue work on this mod, be it maintenance, or breaking it up into pieces and only using the good parts. All you have to do is clone the repo.

I'll be archiving this GitHub project and my forks to halt all further issue tracking and put a big banner up top. If anyone wants to fork it or clone it or take just the parts that are actually decent, you will still be able to do so.

---

Better Beginnings Mod
=========

Better Beginnings mod for Minecraft v1.7.10. Requires Forge (built with 10.13.3.1448).

Kind of complicated. See the [forum topic](http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/wip-mods/2192122-betterbeginnings-mod).

1.8 Version:
-----------
This repository is for the 1.7.10 branch of BetterBeginnings. It was moved to its own repository because GitHub in IntelliJ can be very finnicky. Source for the 1.8 version can be found [here](https://github.com/einsteinsci/betterbeginnings).

Issues:
-------
Issues regarding this repository should be posted on the [1.8 repository](https://github.com/einsteinsci/betterbeginnings/issues), to keep them together. They will be marked with a "1.7.10" version tag if the issue is confirmed.

Contributing to this project:
-----------------------------
1. Download Forge v10.13.3.1448
2. Setup with gradlew setupDecompWorkspace and gradlew eclipse
3. Fork and clone this repository in a separate folder
4. Add a new project, with src/main/java and src/main/resources
5. Change the src/main/java and src/main/resources folder to use the src/main/java and src/main/resources folders from the source you downloaded.
6. Delete the src/main/java and src/main/resources from the default "minecraft" package
7. Add the project to the build path (or something like that)
8. Do cool stuff with the code
9. When you're finished with your changes, submit a pull request.
