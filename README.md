Quiz Leaderboard System

bjective

Fetch quiz data from API, remove duplicate entries, and generate leaderboard.

Approach

Called API 10 times (poll 0–9)
Maintained 5 sec delay between calls
Used (roundId + participant) to remove duplicates
Aggregated scores using HashMap
Sorted leaderboard in descending order

How to Run

1. Compile:
   javac QuizApp.java

2. Run:
   java QuizApp

Output

Correct leaderboard
Correct total score

Tech Used
Java
HashSet
HashMap
