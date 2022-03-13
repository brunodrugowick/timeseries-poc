# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html). An `Unreleased` section is allowed when changes are being merged but not released yet.

Types of changes: `Added`, `Changed`, `Deprecated`, `Removed`, `Fixed` and `Security`.

[//]: # (The latest version must start on line 9. The GitHub Actions of this repo rely on it. You ca use UNRELEASED as the version if you don't want to release.)
## [0.4.0]
### Add
- Start saving user profile.

## [0.3.0]
### Add
- Add basic cache with TTL of up to 100 seconds.
- Clear user cache on new data for that user.
- Clear all cache data every 5 minutes.

## [0.2.0]
### Add
- Add average widgets.

## [0.1.0]
### Add
- Add filter (last X days).

## [0.0.9]
### Fix
- Fix legend and axis label position.

## [0.0.8]
### Added
- Events now show up in the main widget.
- Small visual improvements on the main widget (spacing, size within the container...).

## [0.0.7]
### Fixed
- Pages that create entities now correctly show username.

## [0.0.6]
### Added
- Ability to save events.
- New UI includes Heart Rate and table of Events.
### Fixed
- Header responsiveness now works on mobile devices.
- Username aligned to right.

## [0.0.5]
### Added
- Add simple table for data.

## [0.0.4]
### Changed
- Now the app uses a Postgres database.
### Added
- Add `dev-mode` to facilitate development - local form login, creates data n startup (make sure to combine with an in-memory DB as suggested on README).

## [0.0.3]
### Added
- Add some dev stuff that will be changed in a bit (just wanted to make sure my actions work :)

## [0.0.2]
### Added
- GitHub Actions to test code in a PR
- Automatic release when merging to main

## [0.0.1]
### Added
- Basic functioning page to show measurements in a graph.
- Basic page to add measurements.
