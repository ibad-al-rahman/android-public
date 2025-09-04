# Architecture

This project uses the **MVI (Model-View-Intent)** architectural pattern.

## MVI Flow

```mermaid
graph LR
    A((Interaction)) --> B{UI}
    B -->|intention| C[Intention Processor]
    C -->|action| D[Interactor]
    D --> E[(Repository)]
    E --> D
    D -->|result| H@{ shape: processes, label: "Result Flow" }
    H --> C
    H --> J[ViewActionHandler]
    C -->|result| I[Reducer]
    I -->|new state| K[State]
    J -.->|ViewAction| L[ViewActionProcessor]
    L -.->|intention| C

    M[Lifecycle Observer]
    M -.->|intention| C

    subgraph "Screen"
        K
        B
        L
        M
    end

    subgraph "View Model"
        C
        I
        J
    end

    subgraph "Data Layer"
        E --> F[(Local Repo)]
        E --> G[(Remote Repo)]
        F --> E
        G --> E
    end
```

## MVI Components

- **Model**: Represents the state of the application
- **View**: Displays the current state and captures user intents
- **Intent**: Represents user actions and triggers state changes

The MVI pattern ensures unidirectional data flow and predictable state management throughout the application.
