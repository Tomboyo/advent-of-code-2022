(ns advent-2022.bitset)

;; Convert a list of integers to a 64-bit set.
(defn bitset64 [ilist]
  (bit-shift-right (reduce bit-set 0 ilist) 1))

(comment
  ;; set the first four bits, giving the number 15
  (bitset64 '(1 2 3 4))
  ;; set the 52nd bit
  (bitset64 '(52))
  :ref)

;; Return the intersection of two bitsets as a bitset.
(defn intersection
  ([a b] (bit-and a b)) 
  ([a b & r]
   (loop [a a
          b b
          r r]
     (if (empty? r)
       (bit-and a b)
           (recur
            (bit-and a b)
            (first r)
            (rest r))))))

;; Return the position of the first element of the bitset, starting with the least significant bits.
(defn firstbit [x]
  (loop [i x
         p 0]
    (if (= 0 i)
      p
      (recur (bit-shift-right i 1) (+ p 1)))))

(comment
  ;; The intersection of {1 6 7 8} and {1 2 3 4} is {1}.
  ;; Note that this is a set.
  (intersection (bitset64 '(1 6 7 8)) (bitset64 '(1 2 3 4)))
  :ref)