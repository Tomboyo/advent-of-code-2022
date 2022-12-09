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

(comment
  (re-seq #"\d+" "move 15 from 26 to 39")
  :ref)

(defn top-crates [crates cols]
  (str/join (for [i (range cols)
               v (first (get crates (+ i 1) ""))]
           v)))

(comment
  (re-seq #"\d" "move 5 from 4 to 3"))
  

(defn part1
  ([lines]
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
           (let [before-size (reduce + (for [[k v] crates] (count v)))
                 after (parse-command line crates)
                 after-size (reduce + (for [[k v] after] (count v)))]
             (if (not= before-size after-size)
               ;; I'm leaving this here for historical reference. I couldn't figure out how to get debugging to work, but you know what? Some carefully-plased assertions really does the trick.
               (do (println "before: " crates)
                   (println "cmd:    " line)
                   (println "after:  " after)
                   {})
               (recur "commands" (rest lines) (parse-command line crates))))))))))

(comment
  (solve part1 "day5.txt")
  :ref)