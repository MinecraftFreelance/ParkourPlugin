![Author banner](https://i.imgur.com/2Pqnm9N.png)

[![AUTHOR - BillyDotWS](https://img.shields.io/static/v1?label=AUTHOR&message=BillyDotWS&color=2ea44f&style=for-the-badge&logo=discord+)](https://billy.ws) [![LinkedIn][linkedin-shield]][linkedin-url]

---
<br>
<h3 align="center" style="font-size: 2vw;text-transform: uppercase;text-align: center;line-height: 1;">Parkour Plugin</h3>
<p align="center">For Minecraft version(s): 1.18.2</p>
<p  align="center">A parkour plugin that logs best attempts into an SQL database</p>
<br>
<br>

## 🙏 Client Information
This plugin was created as part of a commission. Please refer to the agreed upon terms and conditions for information regarding license agreements, modification ability, upkeep responsibilities etc. This can typically be found on the platform initially contacted on (for example: discord, fiverr).

#### Client contact information:
- Server name: `DevRoom`
- Discord - `https://discord.gg/devroom

## 🗒️ Given Requirements
- [ ] It should be configurable in checkpoints.json
- [ ] The plugin will need to track the time on each parkour attempt
- [ ] WorldGuard. When a player enters a region named "parkour_region", they will have to get a scoreboard at the right side of their screen with their best attempt and top 5 players with best attempts.
- [ ] Scoreboard - https://i.imgur.com/0sBLL2r.png
 
## ⚙️ Configuration Options
This plugin is configurable based on the client requirements:
`checkpoints.json` is generated automatically and has the following format:
```
{
  "checkpointsData": [
    {
      "worldName": "world",
      "x": 258,
      "y": 69,
      "z": 38
    },
    {
      "worldName": "world",
      "x": 245,
      "y": 74,
      "z": 30
    },
    {
      "worldName": "world",
      "x": 233,
      "y": 78,
      "z": 44
    },
    {
      "worldName": "world",
      "x": 233,
      "y": 79,
      "z": 64
    }
  ]
}
```
Each item in the array represents a checkpoint - each checkpoint being a place players must go to continue to the next. Within this, we define the world, x, y and z coordinates, aligning with the Minecraft coordinates of each desired checkpoint.

## 🛠️ Tools Used
Multiple tools have been used during development to speed up development and keep things neat!
- [CheckStyle](https://plugins.jetbrains.com/plugin/1065-checkstyle-idea) has been used to create well formatted, consistent code (and enforce final usage)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/old/) has been used to code the plugin, as it offers great plugin support!

## 👣 Versioning
Whilst developing this plugin, I have used [Semantic versioning](http://semver.org/) for the current versions of the plugin, see the [list of created versions so far](/tags)!. I have created an automatic versioning system that automatically increments the version (see `.github/workflows/release.yaml`), this automatically increases the version every time a commit is made to the main branch of this repository.

## 🏗️ Building the plugin
This plugin is automatically versioned using a GitHub action (as mentioned above), in order to build the plugin. Create an environment variable called "RELEASE_VERSION" on your machine and give it a value (you can do this using `$RELEASE_VERSION=value` on a Linux/Unix machine.

This Spigot plugin is built using maven, using the provided `pom.xml` file.
```
mvn build
```
This will create a jar file in the root directory of the repository, this can be used to put into the `/plugins` folder.

[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://www.linkedin.com/in/billy-robinson-a6486714a/
