(ns advent-2022.day7
  (:require
    [advent-2022.util :refer [read-lines]]
    [clojure.tools.trace :refer [trace]]))

;; A "sized-dir" is a list representation of a directory which contains any
;; number of sized-dir children and exactly one integer, the total size of this
;; sized-dir and all nested sized-dirs: '(size sized-dirs....)
(defn sized-dir [dir]
  (conj
    ; Concat the unmodified sized-dir subdirectories...
    (filter (comp not int?) dir)
    ; to the sum of file sizes.
    (reduce + (map (fn [x] (if (int? x) x (first x))) dir))))
(comment
  (sized-dir [1 3 [7] [2] 1])
  ; (14 [7] [2])
  :ref)

(def input (read-lines "day7.txt"))

;; Convert a stream of input strings into a sized-dir tree.
;;
;; The xform of the transducer prepares the input. Our first observation is that we can ignore everything except the
;; following three patterns:
;; - $ cd dirname
;; - size filename
;; - $ cd ..
;; to build the tree. If we convert the size statements to integers and treat the cd's like parenthesis, we can see it
;; really just looks like a nested list: (100 1500 (200 10 (5)) 25 (26 27)), for example.
;;
;; Once we have easy-to-use inputs, the next step is to reduce the input lines into an actual tree. We're using the
;; sized-dir idea above so that the resulting tree is as easy to use as possible (and looks a little like a max-heap,
;; but not quite).
;;
;; The reducer's accumulator consists of a `stack` and a `cwd`. The cwd is the "current" directory.
;; - As we receive file sizes, we add them to the current directory.
;; - If we step into a nested directory, we push cwd onto the stack and start a new cwd, initially empty.
;; - If we step out of a directory, we pop its parent off the stack, convert cwd into a sized-dir, and combine it with
;;   the parent. Now the parent is a list of files sizes and sized-dir children; it does not contain any other kind of
;;   directory representation.
;; - If we hit end of input, we unroll the stack as if we continued to receive `cd ..` commands.
;; We now have a sized-dir tree.
;;
(def tree
  (transduce
    ;; get non-nil match groups only for `$ cd not-/` and `filesize filename` lines, keeping only dirnames and file
    ;; sizes. Filter out the failed matches, then parse the tokens to data.
    (comp
      (map #(re-matches #"\$ cd ([^/]+)|(\d+) \D+" %))
      (filter (comp not nil?))
      (map (fn [[_ dirname size]] (or dirname (Integer/parseInt size)))))
    ;; Reduce the input into a sized-dir tree. All but the last call to the reducer passes both state and x; the final
    ;; call passes nil (which signals the reducer to "finish").
    (fn [[stack cwd :as state] & [x]]
      (cond
        ;; Given a file size, accumulate it into the cwd.
        (int? x) [stack (conj cwd x)]
        ;; Given `cd ..`, pop cwd's parent back off the stack. Convert cwd to a sized-dir, push it onto it's parent dir,
        ;; and make the result the cwd. Recur with the now-smaller stack.
        (or (= ".." x)) (let [[[parent] stack] (split-at 1 stack)]
                          [stack (conj parent (sized-dir cwd))])
        ;; If we've hit EOF, unroll the remaining elements of the stack just like with cd .. until none are left, then
        ;; return cwd (which is now / and its sized-dir children).
        (nil? x) (loop [stack stack cwd cwd]
                   (let [[[parent] stack] (split-at 1 stack)]
                     (if (nil? parent)
                       (sized-dir cwd)
                       (recur stack (conj parent (sized-dir cwd))))))
        ;; Given `cd dirname`, push cwd onto the stack and begin a fresh cwd. The new cwd is going to be a child of what
        ;; we just pushed onto the stack.
        :default [(conj stack cwd) '()]))
    ['() '()]
    input))

;; The hardest part is done. Now for the hardest part.
;;
;; PART 1:
;; Now we need to walk the tree and accumulate the size of every directory smaller than a threshold. We can do this with
;; a recursive reduction function: add this directory's size (or 0 if it is too big) to the sum of sizes of all
;; children, recursively.
;;
(defn sum-tree [[size & children]]
  (reduce + (if (< size 100000) size 0) (map sum-tree children)))

(println "PART 1:" (sum-tree tree))

;; PART 2:
;; Now we do basically the same thing. Take the smaller of a directory's size (or a sufficiently large number if it is
;; not above the threshold) and the smallest such size of all children.
(defn smallest-large-file [[size & children]]
  (reduce min (if (> size 8381165) size 70000000) (map smallest-large-file children)))

(println "PART 2:" (smallest-large-file tree))