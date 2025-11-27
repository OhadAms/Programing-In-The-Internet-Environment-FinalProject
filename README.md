# Programing-In-The-Internet-Environment-FinalProject
## Java Client-Server Graph Algorithms Project



## Table of Contents

- [Overview](#overview)  
- [Algorithms](#algorithms)  
  - [AllConnectedComponents](#1-allconnectedcomponents)  
  - [BfsTls](#2-bfstls)  
  - [SubmarineCounter](#3-submarinecounter)  
  - [Dijkstra](#4-dijkstra)  
- [Libraries Used](#libraries-used)  
- [Server Architecture](#server-architecture)  
- [Usage](#usage)  
- [Features](#features)  
- [Notes](#notes)  
- [Class Diagrams](#class-diagrams)  

---

## Overview
Well designed Java Client Server Program where the server handles difficult tasks in a multithreaded environment.

This Java project is a **client-server system** designed to handle graph-related computations and algorithms.  
The server is capable of handling multiple clients concurrently, performing tasks such as:

- Finding connected components in a graph  
- Calculating shortest paths  
- Counting valid submarines on a grid  
- Determining lightest paths using Dijkstra’s algorithm  

Clients can send requests to the server, which processes the data and returns results efficiently.

---

## Algorithms

### 1. AllConnectedComponents

- Finds all connected components in a graph.  
- Uses `DfsVisitTls` to identify the component of each node.  
- Iterates over all nodes, adding unique components to a master list.  
- Returns the full list of connected components to the client.

### 2. BfsTls

- Computes shortest paths between a source and target node.  
- First checks if both nodes are in the same connected component using `DfsVisitTls`.  
- Implements BFS level by level using `levelSize`.  
- Reconstructs the shortest path once the target is reached.  
- Returns all shortest paths from source to target.

### 3. SubmarineCounter

- Counts valid submarines on a grid.  
- Uses `AllConnectedComponents` to get all connected components.  
- Validates each component as a proper submarine (rectangular or square blocks of `1`s).  
- Returns the total count of valid submarines.

### 4. Dijkstra

- Finds the lightest paths between a source and target node.  
- Processes paths iteratively using a queue until all paths are explored.  
- Updates minimum weight paths and generates new paths for neighbors.  
- Returns all lightest paths between source and target nodes.

---

## Libraries Used

- `java.io.Serializable`  
- `java.util.*`  
- `java.io.*`  
- `org.jetbrains.annotations.NotNull`  
- `java.net.Socket`  
- `java.net.ServerSocket`  

---

## Server Architecture

1. Create an `InternetServer` object.  
2. Call `SupportClient()` with the concrete handler.  
3. Initialize a `ThreadPoolExecutor` to manage clients.  
4. Create a `ServerSocket` and enter a loop to accept connections.  
5. For each connection:
   - Start an `acceptThread` to listen for incoming clients.  
   - Spawn a `specificClientHandling` thread that calls `handleClient()` on the handler.  
   - Execute the thread using the thread pool.
6. Each client is handled concurrently without blocking others.  

**Client Handling Thread Pseudocode:**
```java
Thread clientHandling = new Thread(() -> {
    while (!stopServer) {
        Thread acceptThread = new Thread(() -> {
            Socket clientSocket = serverSocket.accept();
            Thread specificClientHandling = new Thread(() -> handler.handleClient(clientSocket));
            clientPool.execute(specificClientHandling);
            specificClientHandling.start();
        });
        acceptThread.start();
    }
});
clientHandling.start();
```

---

## Usage

1. Start the server:
```bash
java InternetServer
```
2. Connect a client:
```bash
java Clien
```
3. Send the data (graph or board) and request a specific algorithm.  
4. Receive the results from the server.

---

## Features

- **Concurrent Client Handling:** Uses threads and a thread pool for efficient parallel execution.  
- **Thread Safety:** Algorithms use thread-local storage (TLS) to prevent conflicts between clients.  
- **Versatile Algorithms:** Supports graph traversal, shortest path, connected components, and submarine counting.  

---

## Notes

- Algorithms are designed for correctness and performance.  
- Server handles multiple clients seamlessly without blocking.  
- Suitable for educational purposes, algorithm demonstrations, and graph-based computation tasks.  

---

## Class Diagrams

![Flow Chart 1](https://github.com/OhadAms/Programing-In-The-Internet-Environment-FinalProject/blob/main/Flow%20Charts/Flow_Chart_1.jpg)

---

![Flow Chart 2](https://github.com/OhadAms/Programing-In-The-Internet-Environment-FinalProject/blob/main/Flow%20Charts/Flow_Chart_2.jpg)

---

![Flow Chart 3](https://github.com/OhadAms/Programing-In-The-Internet-Environment-FinalProject/blob/main/Flow%20Charts/Flow_Chart_3.jpg)

---

![Flow Chart 4](https://github.com/OhadAms/Programing-In-The-Internet-Environment-FinalProject/blob/main/Flow%20Charts/Flow_Chart_4.png)

