# Quarkus - Arthas
<!-- ALL-CONTRIBUTORS-BADGE:START - Do not remove or modify this section -->
[![All Contributors](https://img.shields.io/badge/all_contributors-1-orange.svg?style=flat-square)](#contributors-)
<!-- ALL-CONTRIBUTORS-BADGE:END -->

## Purpose

This Quarkus extension integrates [Arthas](https://github.com/alibaba/arthas) into Quarkus Dev Mode thus allowing users to deeply
probe their application using Arthas.

---
**NOTE**

The extension has absolutely no impact on the production Quarkus application
---

## Usage

* Add the extension the application's dependencies
* Start Quarkus in Dev Mode with the following configuration property `quarkus.arthas.enabled=true`
* A short while after the application has started, you should see something like `Arthas started and is accessible at http://localhost:8563` in the application logs
* Browse the URL mentioned above to access the Web UI of Arthas

## Contributors ✨

Thanks goes to these wonderful people ([emoji key](https://allcontributors.org/docs/en/emoji-key)):

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tbody>
    <tr>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/geoand"><img src="https://avatars.githubusercontent.com/u/4374975?v=4?s=100" width="100px;" alt="Georgios Andrianakis"/><br /><sub><b>Georgios Andrianakis</b></sub></a><br /><a href="https://github.com/quarkiverse/quarkus-arthas/commits?author=geoand" title="Code">💻</a> <a href="#maintenance-geoand" title="Maintenance">🚧</a></td>
    </tr>
  </tbody>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->

This project follows the [all-contributors](https://github.com/all-contributors/all-contributors) specification. Contributions of any kind welcome!