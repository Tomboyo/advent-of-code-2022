(ns advent-2022.day15 
  (:require
   [clojure.java.io :as io]))


;; We're given a file of sensor readings containing the coordinates of a sensor
;; and the unique nearest beacon to it by taxicab distance. Because the beacon
;; is the unique closes beacon to the sensor, no other beacons are located
;; within the sensor's radius (where "radius" is the taxicab distance to the
;; beacon.)
;;
;; In part 1, we need to return the number of coordinates that cannot be
;; occupied by a beacon on a line at some y. This number should not include the
;; coordinates of spaces occupied by a beacon.
;;
;; In part 2, we need to find a single beacon that hasn't been detected by any
;; sensor. The problem is constrainted to x and y in [0, 4_000_000].
;;
;;
;; Our approach to part 1 is to find the x-axis intervals of intersection between
;; the given horizontal line and each sensor. After reducing this to a set of
;; disjoint intervals that minimally cover the original intervals, we sum up
;; their ranges and subtract one for each beacon on the line.
;;
;; In part two, we build on this by applying a similar strategy to each y value
;; in the permitted range. For each such horizontal line, we get the set of
;; intervals where the line intersects with sensor radii, reduce that down to
;; the disjoint set of minimal covering intervals, and stop when we find any
;; set with two or more elements (in practice, always exactly two). This line
;; has a "gap" between sensors, so this is exactly where the beacon must be. The
;; output must be the linear offset of the beacon (y * 4_000_000 + x).

(defn read-problem-input
  "Given an io/resource of problem input, return a seq of vectors
  [sensor-x sensor-y beacon-x beacon-y]."
  [resource]
  (with-open [reader (io/reader resource)]
    (doall (->> (line-seq reader)
         ; Get the numbers
         (map (partial re-seq #"-?\d+"))
         ; Filter results from blank lines (e.g. trailing newline)
         (filter (comp not nil?))
         (flatten)
         (map #(Integer/parseInt %))
         ; Group numbers by line: [sensor-x sensor-y beacon-x beacon-y]
         (partition 4)))))
(comment (read-problem-input (io/resource "day15-test.txt")))

(defn taxicab-distance
  ([ax ay bx by] (+ (abs (- bx ax)) (abs (- by ay))))
  ([[ ax ay bx by ]] (taxicab-distance ax ay bx by)))

(defn disjoint-intervals? [[a b] [c d]]
  (or (< b c) (< d a)))


(defn intersecting-intervals
  "Given the line at y and a seq of sensor readings, return a set of intervals
  describing where the line intersects with sensor radii."
  [y readings]
  (->> readings
       ; Decorate each reading with its radius
       (map (fn [[sx sy bx by]] [sx sy bx by (taxicab-distance sx sy bx by)]))
       ; Only consider sensors whose circumference intersects y
       (filter (fn [[_ sy _ _ r]] (<= (abs (- sy y)) r)))
       ; Convert readings to maps containing the coordinates of the beacon and
       ; the x-axis interval of the intersection between the sensor radius and
       ; the line at y.
       (map (fn [[sx sy bx by r]]
              (let [dy (abs (- sy y))
                    dx (abs (- r dy))]
                {:beacon [bx by]
                 :interval [(- sx dx) (+ sx dx)]})))
       ; Reduce the data down to a collection of disjoint x-axis intervals and
       ; the distinct beacons associated with the intersecting sensors.
       (reduce
        (fn [{intervals :intervals
              beacons :beacons}
             {beacon :beacon
              interval :interval}]
          (let [grouped (group-by (partial disjoint-intervals? interval) intervals)
                intersecting (get grouped false)
                ; The interval that covers all intersecting intervals
                cover (if (< 0 (count intersecting))
                        (reduce (fn [[a b] [c d]] [(min a c) (max b d)])
                                interval
                                intersecting)
                        interval)]
            {:intervals (conj (get grouped true) cover)
             :beacons (conj beacons beacon)}))
        {:intervals {} :beacons {}})))


(defn part1
  [y resource]
  (let [{intervals :intervals
         beacons :beacons} (intersecting-intervals y (read-problem-input resource))
        covered-by-intervals (transduce
                               (map (fn [[a b]] (+ 1 (- b a))))
                               +
                               intervals)
        covered-by-beacons (->> beacons
                                (filter (fn [[_ by]] (= y by)))
                                (count)) ]
    (- covered-by-intervals covered-by-beacons)))

(comment
  (part1 10 (io/resource "day15-test.txt"))
  (part1 2000000 (io/resource "day15.txt")))

(defn part2
  [resource]
  (let [readings (read-problem-input resource)
        {y :y [[a b] [c d] :as intervals] :intervals}
        (some
         (fn [y]
           (let [{intervals :intervals} (intersecting-intervals y readings)]
             (if (< 1 (count intervals))
               {:y y :intervals intervals}
               false)))
         (range 0 4000001))
        x (if (< a c)
            (+ 1 b)
            (+ 1 d))]
    {:x x :y y :intervals intervals :offset (+ 1 y (* 4000000 x))}))

;; TODO:
;; Sort intervals by their first coordinate. There's only ONE line where they
;; don't intersect, so you can basically just stop when you have an interval
;; that doesn't intersect with the previous one. This should help with
;; performance.
(comment (part2 (io/resource "day15-test.txt"))
         (part2 (io/resource "day15.txt")))
