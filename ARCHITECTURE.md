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
