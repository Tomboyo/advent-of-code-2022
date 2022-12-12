(ns advent-2022.day10
  (:require [advent-2022.util :refer [read-lines-eager]]))

(defn run-machine
  "Produces a lazy sequence of X register states indexed by clock cycle, [c x]"
  ;; x: the (initial) state of the machine
  ;; program: the (initial) program to run, as a list of commands
  ([x program] (run-machine x (rest program) 1 (first program)))
  ;; cycle: the current cycle
  ;; [f c]: the current instruction to run, consisting of
  ;; f: the function to aply to x
  ;; c: the number of cycles remaining until f is applied to x
  ([x program cycle [f c debug]]
   (cond
     ;; The program finished.
     (nil? f) nil
     ;; The cmd will finish at the end of this cycle, so the effects should be
     ;; passed to the next cycle. But, the effects are not visible YET, so when
     ;; we emit our state, it uses the current value of x.
     (= c 1) (let [x' (f x)
                   cmd (first program)
                   program (rest program)
                   cycle' (+ cycle 1)]
               (lazy-seq (cons [cycle x]
                               (run-machine x' program cycle' cmd))))
     ;; The cmd is still running, so we need to decrement its counter and keep
     ;; going.
     :default (let [cmd [f (- c 1)]
                    cycle' (+ cycle 1)]
                (lazy-seq (cons [cycle x]
                                (run-machine x program cycle' cmd)))))))

;; Parses instruction lines into cmds whose first element is a function to apply
;; to x, second is the number of clock cycles the function costs, and third is
;; the textual representation of the command (for debugging).
(defn parse-command [line]
  (if (= "noop" line)
    [identity 1 "noop"]
    (let [i (-> line
                ((partial re-matches #"[^0-9\-]*(-?\d+)"))
                (second)
                (Integer/parseInt))]
      [(partial + i) 2 (str "advx " i)])))

;; Part 1
;; Get the sum of signal strengths for the first 6 congruent-20-mod-40 states.
(println "PART 1: " (transduce
                      ; Filter to cycles 20, 60, 100, etc, mapped to their signal strength
                      (comp
                        (filter (fn [[c _]] (= 20 (mod c 40))))
                        (map (fn [[c x]] (* c x))))
                      ; Sum of signal strengths
                      +
                      ; Generate the first 180 cycles of state for this program.
                      (->> "day10.txt"
                           read-lines-eager
                           (map parse-command)
                           (run-machine 1)
                           (take 220))))

;; Part 2
;; Paint the screen!
(println "--- PART 2 ---\n"
         (transduce
           (comp
             ; Determine what the CRT should paint. Cycle-mod-40 is the
             ; horizontal position to paint, and we should paint "#" if the
             ; sprite intersects the CRT. The sprite is 3 pixels wide, and X
             ; indicates its center. "." is painted otherwise.
             ; Remember that c is one-based, and should be ashamed of itself.
             (map (fn [[c x]] (if (<= (- x 1) (mod (- c 1) 40) (+ x 1)) "#" ".")))
             ; Partition the output into 40-pixel-wide rows
             (partition-all 40)
             ; Make those lines into strings, adding newlines
             (map #(apply str (cons "\n" %))))
           ; Concat each line of CRT drawing
           str
           ; Generate the first 180 cycles of state for this program.
           ; See also day10.test.txt
           (->> "day10.txt"
                read-lines-eager
                (map parse-command)
                (run-machine 1))))