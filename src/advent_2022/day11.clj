(ns advent-2022.day11
  (:require [clojure.tools.trace :refer [trace]]))

(defrecord Monkey [id items worry throw-to])

(defn square [x] (* x x))
(defn divisible-by [x t f] #(if (= 0 (mod % x)) t f))

; The indexed collection of monkeys.
(def M {0 (Monkey. 0
                   [89 84 88 78 70]
                   (partial * 5)
                   (divisible-by 7 6 7))
        1 (Monkey. 1
                   [76 62 61 54 69 60 85]
                   (partial + 1)
                   (divisible-by 17 0 6))
        2 (Monkey. 2
                   [83 89 53]
                   (partial + 8)
                   (divisible-by 11 5 3))
        3 (Monkey. 3
                   [95 94 85 57]
                   (partial + 4)
                   (divisible-by 13 0 1))
        4 (Monkey. 4
                   [82 98]
                   (partial + 7)
                   (divisible-by 19 5 2))
        5 (Monkey. 5
                   [69]
                   (partial + 2)
                   (divisible-by 2 1 3))
        6 (Monkey. 6
                   [82 70 58 87 59 99 92 65]
                   (partial * 11)
                   (divisible-by 5 7 4))
        7 (Monkey. 7
                   [91 53 96 98 68 82]
                   square
                   (divisible-by 3 4 2))})

(defn throw-to-monkey [monkeys [item from to]]
  "Remove the first item of from's inventory, then append the given item to
  to's inventory, returning the update monkeys collection. This simulates
  trowing an item"
  (assoc (assoc monkeys to (update (monkeys to) :items conj item))
    from
    (update (monkeys from) :items #(subvec % 1))))

(defn simulate
  "Generate a lazy-seq of events representing the outcome of each turn of monkey
  business."
  ([relief turn-order monkeys] (simulate 1 relief (into [] turn-order) monkeys))
  ([turn relief turn-order monkeys]
   (let [id (first turn-order)
         m (monkeys id)
         ; The updated vec of monkeys, after throwing items around. We map the
         ; items into pairs of worry and who to throw two, then reduce that into
         ; an updated monkeys vec.
         monkeys' (->> (:items m)
                       (map (:worry m))
                       (map relief)
                       (map (fn [item] [item id ((:throw-to m) item)]))
                       (reduce throw-to-monkey monkeys))
         ; the number of inspections m performed
         c (count (:items m))]
     (lazy-seq (cons [turn id c]
                     (simulate (+ turn 1) relief (subvec (conj turn-order id) 1) monkeys'))))))

(defn relief
  "Divide by 3 and round down, returning a long."
  [item]
  (long (/ item 3)))

(defn solve [relief rounds]
  (->> (simulate relief (range (count M)) M)
       ; x rounds is one turn per monkey x times
       (take (* rounds (count M)))
       ; To a map of monkey-id => total inspections
       (reduce (fn [map & args]
                 ; I'm not entirely sure how args becomes a ([t id c]).
                 ; Something to do with the lazy-seq of a lazy-seq?
                 (let [[turn id c] (first args)]
                   (update map id #(+ c (or % 0)))))
               {})
       (trace "result")
       (vals)
       (sort >)
       (take 2)
       (apply *)))

; PART 1
(println "PART 1: " (solve relief 20))

; PART 2: More Bigger More Integeryer
; The relief function is now the identity function, so worry values get B I G.
;
; BigInt[eger] arithmetic gets very expensive. When profiled, we can see the
; cost associated with squaring a large number drives performance. Our solution
; will take advantage of number theory to keep worry values small.

; Each monkey divides by a prime number p. We can multiply those together into
; E. If x is congruent 0 mod E, then x is also congruent p mod 0 for all p. It's
; also true of 1 -- and in general should be true for any value smaller than the
; smallest p (once x >= min(p), it's congruent 0 mod min(p), but congruent x mod
; E.)
(def E (* 3 5 2 19 13 11 17 7))
(defn relief2 [x] (let [x' (mod x E)]
                    (if (< 2 x') x' x)))

(println "PART 2: " (solve relief2 10000))
