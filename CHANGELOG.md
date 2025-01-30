# Change Log

All notable changes to this project will be documented in this file. This change log follows the conventions
of [keepachangelog.com](http://keepachangelog.com/).

## 0.2.2 - 2025-01-30

### Fixed

- Fix `handler-fn->interceptor` to make the interceptor compatible with Pedestal Interceptor. The `handle-fn` should
  always return a `context` map.

## 0.2.1 - 2025-01-30

### Added

- Added `job-execution-timing-interceptor` interceptor to measure the time it takes to execute a job.

## 0.2.0 - 2025-01-26

### Fixed

- Job execution is now correctly being stopped when the scheduler component is stopped.

### Added

- Added the compatibility with Pedestal Interceptor while scheduling jobs.

## 0.1.0 - 2025-01-14

### Added

- Initial version of the Scheduler component.
