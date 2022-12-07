(ns advent-2022.day2 
  (:require [clojure.java.io :as jio]))

;; Gives the score for the round based on what you played and the outcome of the round.
;; Rock, Paper, and Scissors throws are worth 1, 2, and 3 points respectively.
;; Losing, tying, and winning are woth 0, 3, and 6 points respectively.
(defn score-round [s]
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
(defn score-round-2 [s]
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

(defn scorer [score-round]
  (fn [lines]
    (transduce (map score-round) + 0 lines)))

(defn part1 [lines] (apply (scorer score-round) lines))

(defn part2 [lines] (apply (scorer score-round-2) lines))

(defn solve [resource scorer]
  (let [url (jio/resource resource)]
    (with-open [r (jio/reader url)]
      (apply scorer (line-seq r)))))

(comment
  (solve-part-1)
  :ref)