(ns advent-2022.day8
  (:require
    [advent-2022.util :refer [read-lines-eager]]
    [clojure.set :as set]
    [clojure.string :as str]))

;; Given a 2D vector g ("grid") of tree heights, a starting coordinate p0, and a
;; step-function that defines what direction to look in, return the set of
;; coordinates of visible trees.
(defn visible-trees-p1 [g p0 step-fn]
  (loop [m -1
         s #{}
         p p0]
    ;; Note: (reverse p) not necessary, but x is actually the row, so it's
    ;; actually y. Flip them so debug output makes sense.
    (let [h (get-in g (reverse p))]
      (cond
        ;; We have advanced past the edge of the grid, so we're done.
        (nil? h) s
        ;; If this tree is the tallest, it is visible. Accumulate its
        ;; coordinates continue.
        (> h m) (recur h (conj s p) (step-fn p))
        ;; Otherwise, it's not visible, but a tree later on might still be, so
        ;; continue.
        :default (recur m s (step-fn p))))))

;; Step functions that translate a coordinate in a cardinal direction.
(defn step-down [[x y]] [x (+ y 1)])
(defn step-up [[x y]] [x (- y 1)])
(defn step-left [[x y]] [(- x 1) y])
(defn step-right [[x y]] [(+ x 1) y])

;; A sequence of [p step-fn] pairs for each starting point and direction
;; to test in.
(defn arg-set-p1
  [w h]
  ;; successive concat to remove unwanted nesting, but not all nesting as
  ;; flatten would.
  (apply concat (concat
                  (for [x (range w)] [[[x 0] step-down]
                                      [[x (- h 1)] step-up]])
                  (for [y (range h)] [[[0 y] step-right]
                                      [[(- w 1) y] step-left]]))))

;; PART 1
;; Get the set of all distinct visible trees, then get the count.
(defn part1 [g]
  (let [w (count (first g))
        h (count g)
        ;; for each [p step-fn] pair, apply visible-trees-p1 to [g p step-fn],
        ;; producing a set for each.
        xform (map #(apply (partial visible-trees-p1 g) %))]
    (count (transduce
             xform
             set/union
             (arg-set-p1 w h)))))

(def G (vec (transduce
              (comp (map #(str/split % #""))
                    (map #(map (fn [x] (Integer/parseInt x)) %))
                    (map vec))
              conj
              (read-lines-eager "day8.txt"))))
(println (part1 G))


;; Given a 2D vector g ("grid") of tree heights, a starting coordinate p0, and a
;; step-function that defines what direction to look in, return the set of
;; coordinates of visible trees.
(defn visible-trees-p2 [g p0 step-fn]
  (loop [m (get-in g (reverse p0))
         c 0
         p (step-fn p0)]
    ;; Note: (reverse p) not necessary, but x is actually the row, so it's
    ;; actually y. Flip them so debug output makes sense.
    (let [h (get-in g (reverse p))]
      (cond
        ;; We have advanced past the edge of the grid, so we're done.
        (nil? h) c
        ;; If this tree is at least as tall as the starting tree, it counts,
        ;; but we're also done.
        (>= h m) (+ c 1)
        ;; Otherwise, this tree is shorter, so it counts and we can continue
        ;; going.
        :default (recur m (+ 1 c) (step-fn p))))))

;; PART 2
(defn part2 [g]
  (let [w (count (first g))
        h (count g)]
    (apply max
           (for [x (range w)
                 y (range h)]
             (*
               (visible-trees-p2 g [x y] step-up)
               (visible-trees-p2 g [x y] step-down)
               (visible-trees-p2 g [x y] step-left)
               (visible-trees-p2 g [x y] step-right))))))
(println (part2 G))