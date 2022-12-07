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

(defn part1 [lines]
  (transduce (map score-round) + 0 lines))

(defn solve-part-1 []
  (let [url (jio/resource "day2.txt")]
    (with-open [r ( jio/reader url)]
      (part1 (line-seq r)))))

(comment
  (solve-part-1)
  :ref)