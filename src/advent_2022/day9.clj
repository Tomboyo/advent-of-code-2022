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


;; Maximize the absolute value of the given x, preserving its sign.
;; -1/2 => -1
;;  1/2 => 1
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

;; Round x away from 0 such that abs(x) is maximized.
;; (ceil-abs(1/2)) => 1,
;; (ceil-abs(-1/2) => -1.
(defn ceil-abs [x]
  (if (< 0 x)
    (int (Math/ceil x))
    (int (Math/floor x))))

;; Given the current position h of H and t of T, return the updated position of
;; T so that it "follows" H, moving diagonally whenever H and T are not aligned
;; on both axes.
;;
;; If we take e.g. x(h) - x(t) over 2, then round that "away" from t towards the
;; greatest magnitude, it causes T to travel diagonally whenever H and T are not
;; aligned, as desired. MATHS BBY
(defn follow [[xh yh :as h] [xt yt :as t]]
  (if (touching? h t)
    t
    [(+ xt (ceil-abs (/ (- xh xt) 2)))
     (+ yt (ceil-abs (/ (- yh yt) 2)))]))

;; A xform which reds the input of DIR COUNT statements into a flat list of
;; xlate functions.
(def to-xlate
  (mapcat
    (comp
      (fn [[d n]] (for [_ (range n)] (xlate d)))
      (fn [[d n]] [d (Integer/parseInt n)])
      #(str/split % #"\s"))))

;; PART 1
;; Given H and T starting on the same arbitrary point, count the number of
;; spaces that T occupies as it follows H.
(println
  "PART 1: "
  (transduce
    to-xlate
    ;; Transforms the positions of H and T, accumulating the points that T
    ;; occupies over time.
    (fn [[acc h t] & [move]]
      (if (nil? move)
        ;; On EOF, return the number of distinct positions held by T
        (count acc)
        ;; Otherwise, move H and T and acc the new position of T
        (let [h' (move h)
              t (follow h' t)]
          [(conj acc t) h' t])))
    ;; Initially, H and T start on the same arbitrary point.
    [#{[0 0]} [0 0] [0 0]]
    (read-lines "day9.txt")))

;; PART 2
;; Like part 1, but now there 9 instances of T that follow along in a chain.
(println
  "PART 2: "
  (transduce
    to-xlate
    ;; Transforms the positions of H and Ti, accumulating the points that T9
    ;; occupies over time.
    (fn [[acc xs] & [move]]
      (if (nil? move)
        ;; On EOF, return the number of distinct positions held by T9
        (count acc)
        ;; Otherwise move H, then the Ti, an return the updated acc and
        ;; positions of H and Ti.
        (loop [a (move (first xs))
               b (second xs)
               xs (drop 2 xs)
               xs' [a]]
          (let [b' (follow a b)]
            (if (empty? xs)
              ;; b' is T9. We're done.
              [(conj acc b') (conj xs' b')]
              (recur b' (first xs) (rest xs) (conj xs' b')))))))
    ;; Initially, H and T1 - T9 start on the same arbitrary point.
    [#{[0 0]} (repeat 10 [0 0])]
    (read-lines "day9.txt")))