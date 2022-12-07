(ns advent-2022.day2 
  (:require [clojure.java.io :as jio]))

;; Gives the score for the round based on what you played and the outcome of the round.
;; Rock, Paper, and Scissors throws are worth 1, 2, and 3 points respectively.
;; Losing, tying, and winning are woth 0, 3, and 6 points respectively.
(defn score-by-throw [s]
  (case s
    "A X" 4
    "A Y" 8
    "A Z" 3
    "B X" 1
    "B Y" 5
    "B Z" 9
    "C X" 7
    "C Y" 2
    "C Z" 6))

;; Similar to score-round-1, this determines how many points a round is worth usingthe same rules. However, the second argument of each round string indicates how the round needs to end, which implies the play that is thrown.
;; X, Y, and Z mean lose, draw, and win respectively.
(defn score-by-outcome [s]
  (case s
    "A X" 3
    "A Y" 4
    "A Z" 8
    "B X" 1
    "B Y" 5
    "B Z" 9
    "C X" 2
    "C Y" 6
    "C Z" 7))

;; Sums the score of all rounds using the given scoring strategy
(defn score [strategy]
  (fn [lines]
    (transduce (map strategy) + 0 lines)))

(def part1 (score score-by-throw))
(def part2 (score score-by-outcome))

;; Load the problem from a file and apply the given solver to its contents
(defn solve [solver]
  (let [url (jio/resource "day2.txt")]
    (with-open [r (jio/reader url)]
      (solver (line-seq r)))))

(comment
  (solve part1)
  (solve part2)
  :ref)