(ns advent-2022.day3 
  (:require [clojure.java.io :as jio]))

;; Given a string representing a rucksack, returns a list of character lists representing the compartments of the rucksack.
(defn to-compartment-lists [line]
  (split-at (/ (count line) 2) line))

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

;; Given a list of character lists representing the compartments of a rucksack, return the equivalent list using prioritizes instead of characters.
(defn prioritize [parts]
  (map #(map priority %) parts))

;; Get the position of the only set bit.
;; Not that it matters, but our perf is already at least linear w.r.t. elements per compartment, so the naive strategy is fine
(defn get-bit-pos [n]
  (loop [i n
         p 0]
    (if (= 0 i)
      p
      (recur (bit-shift-right i 1) (+ p 1)))))
(comment
  ;; Get the position of the only set bit: 3
  (get-bit-pos 4)
  :ref)

;; Convert the given list of integers to a bitmask, where each integer represents the position of the bit to set starting from 1, the least-significant bit.
(defn mask [ipart]
  (bit-shift-right (reduce bit-set 0 ipart) 1))
(comment
  ;; set the first four bits, giving the number 15
  (mask '(1 2 3 4))
  ;; set the 52nd bit
  (mask '(52))
  :ref)

(defn get-common-priority [[a b]]
  (get-bit-pos (bit-and (mask a) (mask b))))
(comment
  (get-common-priority [[1 2 3 4] [9 8 2 7]])
  :ref)

(def xform
  (comp
   ;; split the rucksack into compartments
   (map to-compartment-lists)
   ;; map elements of compartments to their prioritizes (1 - 52)
   (map prioritize)
   ;; find the common element priority between the compartments
   (map get-common-priority)
   ))

(defn part1 [lines]
  (transduce xform + 0 lines))

;; Aplly the given solver line-by-line to the input file
(defn solve [solver]
  (let [url (jio/resource "day3.txt")]
    (with-open [r (jio/reader url)]
      (solver (line-seq r)))))
(comment
  (solve part1)
  )