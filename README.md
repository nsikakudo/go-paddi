# Gopaddi Trip Planner

Gopaddi is an Android trip planning application built with Jetpack Compose. It allows users to discover destinations, plan new itineraries, and manage their trips using a "Local-First" architecture.

## Features

**Local-First Architecture**: Trips are instantly accessible from the local Room database. The app automatically syncs with the remote API when a connection is available.

**Dynamic Destination Search**: Fetch real-time destinations from a remote API with a reactive search interface.

**Smart Filtering**: Filter trips by travel style (Solo, Group, Couple, etc.) directly from the local cache for zero-latency UI updates.

**Secure Planning Workflow**: A multi-step trip creation process using customized Material 3 BottomSheets and DatePickers.

**Custom Branding**: Fully integrated Satoshi font family across all UI components, maintaining specific font weights and sizes.

**Offline Resilience**: Integrated NetworkMonitor to handle "fail-fast" logic and project-aligned error messaging when the connection is lost.


## Technical Requirements & Implementation

This project adheres to modern Android development best practices and the specified requirements:

*   **MVVM Architecture**: The application is structured using the Model-View-ViewModel architectural pattern, ensuring separation of concerns and testability.
*   **UI Implementation**: The user interface is built using XML Layouts and Android Fragments.
*   **SOLID Principles**:
    *   **Single Responsibility Principle (SRP)**: Classes and functions are designed to have a single, well-defined responsibility (e.g., `TripApi` for API calls, `TripRepository` for data operations).
    *   **Open/Closed Principle (OCP)**: Achieved through interfaces and dependency injection, allowing extensions without modification of existing code.
    *   **Liskov Substitution Principle (LSP)**: `Resource` sealed class allows interchangeable handling of `Success`, `Error`, and `Loading` states.
    *   **Interface Segregation Principle (ISP)**: `ProductApiService` is focused solely on weather API interactions. `NetworkMonitor` is a small, specific interface.
    *   **Dependency Inversion Principle (DIP)**: High-level modules (`ViewModels`, `UseCases`) depend on abstractions (`TripRepository`, `NetworkMonitor`) rather than concrete implementations.
*   **Dependency Injection (Hilt)**: Dagger Hilt is used for managing dependencies, making the codebase more modular, testable, and maintainable.
    *   `NetworkModule` provides all necessary dependencies like `OkHttpClient`, `Retrofit`, `Room Database`, `DAOs`, and `Repositories`.
*   **API Integration (fakestoreapi)**:
    *   Uses Retrofit for making HTTP requests to the fakestoreapi API.
    *   Error handling for API calls (network issues, HTTP errors) is implemented using a `safeApiCall` wrapper.
*   **Local Data Storage (Room Database)**:
    *   Room Persistence Library is used to store product details locally.
    *   `TripEntity` defines the schema for product details.
    *   `TripDao` provides methods for database interactions (insert and get).
*   **Asynchronous Operations (Kotlin Coroutines & Flow)**:
    *   All asynchronous operations (network calls, database interactions) are handled using Kotlin Coroutines.
    *   `Flow` is utilized for observing changes in product details and network connectivity.


## Setup and Installation

Follow these steps to run the application on your local machine.
*   **Prerequisites**:
    *   Android Studio IDE
    *   Android SDK (Target SDK 36, Min SDK 24)
 
*   **Steps to Run**:
    *   Clone the repo `https://github.com/your-username/gopaddi-trip-planner.git` 
    *   sync and run.


## Error Handling

A custom safeApiCall utility catches IOException (network) and HttpException (server) errors, converting them into user-friendly "Gopaddi" branded messages like: "An unexpected hitch occurred: Expected BEGIN_ARRAY..."

