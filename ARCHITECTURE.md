# Architecture

This project uses the **MVI (Model-View-Intent)** architectural pattern.

## MVI Flow

```mermaid
graph LR
    INT((Interaction)) --> UI{UI}
    UI -->|intention| IP[intentionProcessor]
    IP -->|action| RF[resultFrom]
    RF --> REPO[(Repository)]
    REPO --> RF
    RF -->|result| RFL@{ shape: processes, label: "Result Flow" }
    RFL -->|result| RED[Reducer]
    RFL -->|result| VAF[viewActionFrom]
    RED -->|new state| ST[State]
    VAF -.->|ViewAction| VAP[viewActionProcessor]
    VAP -.->|intention| IP

    LO[Lifecycle Observer]
    LO -.->|intention| IP

    subgraph "Screen"
        ST
        UI
        VAP
        LO
    end

    subgraph "View Model"
        IP
        RED
        VAF
    end

    subgraph "Interactor"
        RFL@{ shape: processes, label: "Result Flow" }
        RF
    end

    subgraph "Data Layer"
        REPO --> LR[(Local Repo)]
        REPO --> RR[(Remote Repo)]
        LR --> REPO
        RR --> REPO
    end
```

## MVI Components

- **Model**: Represents the state of the application
- **View**: Displays the current state and captures user intents
- **Intent**: Represents user actions and triggers state changes

The MVI pattern ensures unidirectional data flow and predictable state management throughout the application.
