# Connect Four Game with Heuristic Evaluation

This code implements a game of Connect Four, a two-player connection game in which the players take turns dropping colored discs into a grid. The objective is to be the first to form a horizontal, vertical, or diagonal line of four discs of your color.

## Code Overview

### Connect4.java

- `Connect4` is the main class that represents the Connect Four game.
- It includes game logic, the game board, player moves, and the main game loop.
- The class supports both human and AI players.
- AI players can be configured to use different search algorithms, such as Alpha-Beta Pruning.

### MyEvaluation.java

- `MyEvaluation` is a class that implements the heuristic evaluation function for the Connect Four game.
- It calculates a score for a given game state based on several conditions:
  - Built sequences of discs
  - First move strategy
  - Occupying corners of the board
  - Playing in the middle columns
- The heuristic helps the AI make strategic decisions during gameplay.

### SAC (Search Algorithms Collection) Library

This code uses the SAC library for implementing game search algorithms. The SAC library provides a collection of search algorithms commonly used in AI, such as Alpha-Beta Pruning and Min-Max. It simplifies the implementation of search-based games like Connect Four.

## How to Use

To run the Connect Four game:

1. Compile the Java files.
2. Run the `Connect4` class.
3. Follow the on-screen instructions to make moves (for human players) or watch the AI's moves.

## Dependencies

This code uses the SAC library for game search algorithms. You can find the SAC library at [SAC GitHub Repository](https://github.com/bieganski/sac).

## Contributing

Feel free to contribute to this project by making improvements or adding new features. You can fork the repository, make your changes, and submit a pull request.

## License

This code is provided under the [MIT License](LICENSE).

Enjoy playing Connect Four and experimenting with different AI strategies!

If you have any questions or need further assistance, please don't hesitate to contact us.
