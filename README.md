# Quarkus - Arthas

## Purpose

This Quarkus extension integrates [Arthas](https://github.com/alibaba/arthas) Quarkus Dev Mode thus allowing users to deeply
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
