(ns advent-2022.day3 
  (:require [clojure.java.io :as jio]
            [advent-2022.bitset :as bitset]))

;; Divide a list in half into two equal sublists
(defn bisect [list]
  (split-at (/ (count list) 2) list))

;; Convert a character to its "priority"
;; character ascii ints:
;; A - Z => 65 - 90
;; a - z => 97 - 122
;; character priorities:
;; A - Z => 27 - 52
;; a - z => 1 - 26
(defn priority [char]
  (let [i (int char)]
    (if (< i 91)
      ;; A - Z
      (+ 27 (- i 65))
      ;; a - z
      (+ 1 (- i 97)))))

(defn part1 [lines]
  (let
   [xform (comp
           ;=> "abcAbC"
           (map #(map priority %))
           ;=> (1 2 3 27 2 29)
           (map bisect)
           ;=> [(1 2 3) (27 2 29)]
           (map #(map bitset/bitset64 %))
           ;=> [{1 2 3} {27 2 29}]
           (map (fn [[a b]] (bitset/intersection a b)))
           ;=> {2}
           (map bitset/first)
           ;=> 2
           )]
    (transduce xform + 0 lines)))

;; Apply a solver to the problem line-by-line
(defn solve [solver]
  (let [url (jio/resource "day3.txt")]
    (with-open [r (jio/reader url)]
      (solver (line-seq r)))))

(comment
  (map priority "a")
  (solve part1)
  :ref)
  