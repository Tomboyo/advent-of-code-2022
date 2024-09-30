(ns advent-2022.day1
  (:require clojure.core
            clojure.java.io
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
(defn part1 []
  (let [url (clojure.java.io/resource "day1.txt")]
    (with-open [reader ( clojure.java.io/reader url)]
      (transduce xform max 0 (line-seq reader)))))

;; A reduction operation tht keeps the top 3 largest values seen.
(defn max3
  ([] [0 0 0])
  ([acc] acc)
  ([[a b c] x] (if (> x a) [x a b]
                   (if (> x b) [a x b]
                       (if (> x c) [a b x] [a b c])))))

;; Like part 1, but the transduction reduces to alist of the top 3 values, and then reduces those to their sum.
(defn part2 []
  (let [url (clojure.java.io/resource "day1.txt")]
    (with-open [reader (clojure.java.io/reader url)]
      (reduce + (transduce xform max3 (line-seq reader))))))

(comment
  (part1)
  (part2)
  :ref)
