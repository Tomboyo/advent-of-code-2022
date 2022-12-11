(ns advent-2022.day9
  (:require [advent-2022.util
             :refer
             [read-lines]]
            [clojure.string :as str]))

;; Return a function which translates a point in the indicated cardinal
;; direction.
(defn xlate [dir]
  (case dir
    "U" (fn [[x y]] [x (- y 1)])
    "D" (fn [[x y]] [x (+ y 1)])
    "L" (fn [[x y]] [(- x 1) y])
    "R" (fn [[x y]] [(+ x 1) y])))

;; True if the two points are within are touching.
(defn touching? [[x y] [x2 y2]]
  (and (<= (- x 1) x2 (+ x 1))
       (<= (- y 1) y2 (+ y 1))))

;; Given the current and prior position of H (h' and h respectively) and the
;; current position of T (t), return the new position of T.
;;
;; Essentially, if T is no longer touching H, T occupies the former position of
;; H. THis means T moves diagonally when it's supposed to and cardinally
;; otherwise.
(defn follow [h' h t]
  (if (touching? h' t)
    t
    h))
(comment
  ;; . H' .      . H .
  ;; . H  .  =>  . T .
  ;; T .  .      . . .
  (follow [0 2] [0 1] [-1 0])
  ;; .  . .      . . .
  ;; H' H T  =>  H T .
  ;; .  . .      . . .
  (follow [-1 0] [0 0] [1 0])
  :ref)

;; PART 1
;; Given H and T starting on the same arbitrary point, count the number of
;; spaces that T occupies as it follows H.
(println
  "PART 1: "
  (transduce
    ;; Reads the input of DIR COUNT statements into a flat list of xlate
    ;; functions.
    (mapcat
      (comp
        (fn [[d n]] (for [_ (range n)] (xlate d)))
        (fn [[d n]] [d (Integer/parseInt n)])
        #(str/split % #"\s")))
    ;; Transforms the positions of H and T, accumulating the points that T
    ;; occupies over time.
    (fn [[acc h t] & [move]]
      (if (nil? move)
        ;; On EOF, return the number of distinct positions held by T
        (count acc)
        ;; Otherwise, move H and T and acc the new position of T
        (let [h' (move h)
              t (follow h' h t)]
          [(conj acc t) h' t])))
    ;; Initially, H and T start on the same arbitrary point.
    [#{[0 0]} [0 0] [0 0]]
    (read-lines "day9.txt")))