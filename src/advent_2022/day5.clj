(ns advent-2022.day5
  (:require [advent-2022.util :refer [solve]]
            [clojure.string :as str]))

(defn parse-crates [line]
  (let [xform (comp
                (map #(str/replace % #"[\]\[\s]" ""))
                (map-indexed (fn [i x] [(+ 1 i) x]))
                (filter (fn [[_ x]] (not (str/blank? x))))
                (map (fn [[i x]] [i [x]])))]

    (transduce xform conj {} (re-seq #".{3,4}" line))))

(defn parse-command [line crates]
  (let [[count from to] (map #(Integer/parseInt %) (re-seq #"\d+" line))
        [head tail] (split-at count (get crates from []))
        to-list (concat (reverse head) (get crates to []))]
    (assoc crates to to-list from tail)))

(defn parse-command-part-2 [line crates]
  (let [[count from to] (map #(Integer/parseInt %) (re-seq #"\d+" line))
        [head tail] (split-at count (get crates from []))
        to-list (concat head (get crates to []))]
    (assoc crates to to-list from tail)))

(defn parse-command-f [line]
  (let [[count from to] (map #(Integer/parseInt %) (re-seq #"\d+" line))]
    (fn [crates]
      (let [[head tail] (split-at count (get crates from []))
            to-list (concat (reverse head) (get crates to []))]
        (assoc crates to to-list from tail)))))

(defn top-crates [crates cols]
  (str/join (for [i (range cols)
                  v (first (get crates (+ i 1) ""))]
              v)))

;; Solve part 1 using a stateful iteration strategy wherein we keep track of which section we're currently in.
(defn simulate
  ([parse-command lines]
   (loop [state "crates"
          lines lines
          crates {}]
     (let [line (first lines)]
       (case state
         "crates"
         (if (or (nil? line) (str/starts-with? line " 1"))
           ;; Consume two lines and proceed to commands
           (recur "commands" (rest (rest lines)) crates)
           ;; Parse the line and merge the crates list
           (recur "crates" (rest lines)
                  (merge-with
                    (fn [a [b]] (conj a b))
                    crates
                    (parse-crates line))))

         "commands"
         (if (nil? line)
           (top-crates crates 9)
           (recur "commands" (rest lines) (parse-command line crates))))))))


;; Apply a mapping function to each element x of the input col for as long as the predicate p(x) holds. Return the mapped results (as in (take (map ... col))), as well as the remaining input (as in (drop n col)). This allows sequential, imperative-like code for consuming segments of an input stream based on sentinels.
(defn take-map-while [f p col]
  (loop [x (first col)
         xs (rest col)
         acc '()]
    (cond
      (nil? x) [(reverse acc) xs]
      (p x) (recur (first xs) (rest xs) (conj acc (f x)))
      :else [(reverse acc) xs])))

;; Solve part 1 with something closer to a reduction strategy. Here we use take-map-while to operate on segments of the input stream sequentially, and use let-bindings as a simplistic stand-in for transducers.
(defn part1-2 [lines]
  (let
    ;; Map lines of input to maps of crates (one per row), until end of section.
    [[rows, lines] (take-map-while
                     parse-crates
                     #(not (or (nil? %) (str/starts-with? % " 1")))
                     lines)
     ;; Combine the rows of crates into a map of columns of crates
     crates (reduce
              (fn [acc x] (merge-with (fn [a [b]] (conj a b)) acc x))
              rows)
     ;; Advance past the label/blank line of input
     lines (drop-while #(not (str/starts-with? % "move")) lines)
     ;; Map lines to functions that apply the command to given crates
     commands (map parse-command-f lines)
     ;; Apply movement commands to crates
     crates (reduce (fn [acc f] (f acc)) crates commands)]
    (top-crates crates 9)))

(defn part1 [lines] (simulate parse-command lines))
(defn part2 [lines] (simulate parse-command-part-2 lines))

(comment
  (() (a b c) [])
  (solve part1 "day5.txt")
  (solve part1-2 "day5.txt")
  (solve part2 "day5.txt")
  :ref)