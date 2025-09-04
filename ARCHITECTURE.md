# Architecture

This project uses the **MVI (Model-View-Intent)** architectural pattern.

## MVI Flow

```mermaid
graph LR
    INT((Interaction)) --> UI{UI}
    UI -->|intention| IP[Intention Processor]
    IP -->|action| IA[Interactor]
    IA --> REPO[(Repository)]
    REPO --> IA
    IA -->|result| RF@{ shape: processes, label: "Result Flow" }
    RF --> IP
    RF --> VAH[ViewActionHandler]
    IP -->|result| RED[Reducer]
    RED -->|new state| ST[State]
    VAH -.->|ViewAction| VAP[ViewActionProcessor]
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
        VAH
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
