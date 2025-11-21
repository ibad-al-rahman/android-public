# Ibad 🤖 App

The code behind Ibad's awesome 🤖 App.

## Requirements

The usual stuff:

- Android Studio
- Git

## Architecture

We're using MVI (Model View Intention)

## Module Graph

```mermaid
%%{
  init: {
    'theme': 'dark'
  }
}%%

graph LR
  subgraph :app:common
    :app:common:fp["fp"]
    :app:common:base["base"]
    :app:common:network["network"]
    :app:common:mvi["mvi"]
    :app:common:resources["resources"]
  end
  subgraph :app:data
    :app:data:prayer-times-repository["prayer-times-repository"]
  end
  subgraph :app:screens
    :app:screens:settings["settings"]
    :app:screens:prayer-times["prayer-times"]
  end
  subgraph :app:widgets
    :app:widgets:prayer-times["prayer-times"]
  end
  :app:data:prayer-times-repository --> :app:common:fp
  :app:data:prayer-times-repository --> :app:common:base
  :app:data:prayer-times-repository --> :app:common:network
  :app:screens:settings --> :app:common:fp
  :app:screens:settings --> :app:common:mvi
  :app:screens:settings --> :app:common:base
  :app:screens:settings --> :app:common:resources
  :app:screens:settings --> :app:data:prayer-times-repository
  :app --> :app:common:base
  :app --> :app:common:mvi
  :app --> :app:common:resources
  :app --> :app:screens:prayer-times
  :app --> :app:screens:settings
  :app --> :app:widgets:prayer-times
  :app:widgets:prayer-times --> :app:common:fp
  :app:widgets:prayer-times --> :app:common:mvi
  :app:widgets:prayer-times --> :app:common:base
  :app:widgets:prayer-times --> :app:common:resources
  :app:widgets:prayer-times --> :app:data:prayer-times-repository
  :app:screens:prayer-times --> :app:common:fp
  :app:screens:prayer-times --> :app:common:mvi
  :app:screens:prayer-times --> :app:common:base
  :app:screens:prayer-times --> :app:common:resources
  :app:screens:prayer-times --> :app:data:prayer-times-repository
  :app:common:network --> :app:common:base
  :app:common:mvi --> :app:common:base
```
> Run `./gradlew createModuleGraph` to regenerate the graph
