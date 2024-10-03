(ns advent-2022.day14
  (:require [clojure.java.io :as io]
             [clojure.string :as str]
             clojure.set))

;; We receivve lines of input of the form
;;   x,y -> x,y -> x,y
;; describing paths made of line segments between coordinates. We interpret
;; these as solid paths of rock on a 2D plane. All other space extending
;; infinitely across the plane is empty. At point 500,0 there is a source of
;; sand that falls downwards in the positive-y direction. When sand "collides"
;; with rock or sand directly below it, it can fall diagonally down and left,
;; down and right, or stop moving (in that order). We're meant to simulate
;; falling sand and count the number of instances until 100% of all subsequent
;; sand would fall infinitely down, and return the count.

(defn expand [p1 p2]
  (let [[x1 x2] (sort [ (:x p1) (:x p2)])
        [y1 y2] (sort [ (:y p1) (:y p2)])]
    (for [x (range x1 (+ 1 x2))
          y (range y1 (+ 1 y2))]
      {:x x :y y})))

(comment
  (expand {:x 1 :y 5} {:x 1 :y 3}))

(defn dimensions [world]
  (let [ys (keys world)
        xs (apply clojure.set/union (vals world))]
  {
   :min-x (reduce min xs)
   :max-x (reduce max xs)
   :min-y (reduce min ys)
   :max-y (reduce max ys)
  }))

(defn draw [world]
  (let [d (dimensions world)
        max-x (:max-x d)
        max-y (:max-y d)
        min-x (:min-x d)
        min-y (:min-y d)]
    (doall (for [y (range min-y (+ 1 max-y))]
             (do
               (doall
                 (for [x (range min-x (+ 1 max-x))]
                   (if (and  (= 500 x) (= 0 y))
                     (print "+")
                     (print (if (contains? (get world y) x) "#" ".")))
                   )) 
               (println ""))))
    nil))

(defn world-empty?
  "True if the world does not have a wall or sand at p"
  [world x y]
  (let [row (get world y)]
    (or (nil? row)
        (not (contains? row x)))))

(world-empty? {13 #{497 498 499 500 501 502}} 503 13)

(defn solver [update-world]
  (with-open [reader (io/reader  (io/resource "day14.txt"))]
    (as-> (line-seq reader) it
      (map (fn [y] (str/split y #" -> ")) it)
      (map (fn [path]
             (->> path
                  (map (fn [s] (str/split s #",")))
                  (map (fn [[a b]] {
                                    :x (Integer/parseInt a)
                                    :y (Integer/parseInt b)
                                    }))))
           it)
      (mapcat (partial partition 2 1) it)
      (mapcat (fn [[a b]] (expand a b)) it)
      (reduce (fn [acc p]
                (update acc
                        (:y p)
                        (fn [old] (conj (or old (sorted-set)) (:x p)))))
              {}
              it)
      ; We now have a { y => #{ x1 x2 x3 ...}} of occupied spaces
      (do (comment (draw it))
          (let [w (update-world it)
                d (dimensions w)
                max-y (:max-y d) 
                max-x (:max-x d) 
                min-x (:min-x d)]
            (println min-x max-x max-y)
            (loop [x 500 y 0 counter 0 world (update-world w)]
              (cond
                (or (> y max-y)
                    (or (< x min-x) (> x max-x))) counter
                (world-empty? world x (+ 1 y)) (recur x (+ 1 y) counter world)
                (world-empty? world (- x 1) (+ 1 y)) (recur (- x 1) (+ 1 y) counter world)
                (world-empty? world (+ x 1) (+ 1 y)) (recur (+ 1 x) (+ 1 y) counter world)
                ; Is the sand piled up to the origin?
                (and (= 500 x) (= 0 y)) (+ 1 counter)
                :else (recur 500 0 (+ 1 counter) (update world y (fn [xs] (into #{} (conj xs x)))))))                
            )))))

;; Part 1 -- 979
(solver identity)

;; In part two, there is an infinitely-wide horizontal wall at y = 2 + the
;; greatest y-coordinate. We know based on our println that y=174 is the largest
;; y coordinate, so we'll stick a very wide path in there. The slope of the sand
;; is +/-1, so if the tip of the sand is y=178, the base would extend 178 units
;; in each direction from the origin at x=500. So, the line just needs to be from
;; x=323 to x=679.
;; Part 2 -- 29,044
(solver (fn [world] (assoc world 176 (into (sorted-set) (range 323 679)))))
