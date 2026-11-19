# 🟩 Wordle (Java)

A desktop word-guessing game built from scratch in Java Swing. Guess the hidden five-letter word in six tries, with colour-coded feedback after every guess.

## Features

- 🟩🟨⬜ Colour feedback for each letter: right spot, wrong spot, or not in the word
- ⌨️ On-screen virtual keyboard that tracks which letters you've used
- ✅ Word validation against a dictionary of allowed guesses
- 🎲 Random answer each game, drawn from curated word lists

## Tech stack

Java · Swing / AWT

## Run it

```bash
javac *.java
java Main
```

## How it's built

| Class | Role |
|---|---|
| `WordleGame` | Game state and rules |
| `WordChecker` | Scores a guess letter by letter |
| `WordleGUI` / `Tile` | Board and tile rendering |
| `VirtualKeyboard` | Clickable keyboard with letter states |
