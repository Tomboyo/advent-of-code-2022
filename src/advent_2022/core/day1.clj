(ns advent-2022.core.day1
  (:require clojure.java.io
            clojure.core
            clojure.string))

;; True if a collection contains only blank strings.
(defn blank-partition? [x] (every? identity (map clojure.string/blank? x)))

;; A transform which will take a lazy-seq of lines and produce a number, one per inventory, representing the total calories in that inventory.
(def xform (comp
            ;; partition inventory around blank entries. Blank entries become their own partition.
            (partition-by clojure.string/blank?)
            ;; remove the partitions which contain only the blank entries
            (filter (comp not blank-partition?))
            ;; convert list of string to list of int
            (map (fn [x] (map #(Integer/parseInt %) x)))
            ;; reduce each list to a sum of elements
            (map #(reduce + 0 %))))

;; Open the input file and create a lazy sequence from its contents. Transduce those lines using xform, which finds the largest calorie inventory of all inventories in the file.
(let [url (clojure.java.io/resource "day1.txt")]
  (with-open [reader ( clojure.java.io/reader url)]
    (transduce xform max 0 (line-seq reader))))