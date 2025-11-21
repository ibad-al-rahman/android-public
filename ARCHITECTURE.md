# Architecture

This project uses the **MVI (Model-View-Intent)** architectural pattern.

## Video Resources

- [MVI explanation](https://youtu.be/n3tt8ApYLb4)

## MVI Flow

```mermaid
graph LR
    INT((Interaction)) --> UI{UI}
    UI -->|intention| ROU[router]
    ROU -->|action| RF[resultFrom]
    RF <--> REPO[(Repository)]
    RF -->|result| RFL@{ shape: processes, label: "Result Flow" }
    RFL -->|result| RED[Reducer]
    RFL -->|result| VAF[viewActionFrom]
    RED -->|new state| ST[State]
    VAF -.->|ViewAction| Screen[viewActionProcessor]

    LO[Lifecycle Observer]
    LO -.->|intention| ROU

    subgraph "Screen"
        ST
        UI
        LO
    end

    subgraph "View Model"
        ROU
        RED
        VAF
    end

    subgraph "Interactor"
        RFL@{ shape: processes, label: "Result Flow" }
        RF
    end

    subgraph "System"
        JCR[JetpackComposeReactivity]
    end

    JCR -->|observes| ST
    JCR -->|rerender| UI

    subgraph "Data Layer"
        REPO <--> LR[(Local Repo)]
        REPO <--> RR[(Remote Repo)]
    end
```

## MVI Components

- **Model**: Represents the state of the application
- **View**: Displays the current state and captures user intents
- **Intent**: Represents user actions and triggers state changes

The MVI pattern ensures unidirectional data flow and predictable state management throughout the application.

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