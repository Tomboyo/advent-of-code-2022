(ns advent-2022.day5-test
  (:require [clojure.test :refer [deftest is testing]]
            [advent-2022.day5 :as day5]))

(def crates-input
  '("[Q]         [N]             [N]    "
    "[H]     [B] [D]             [S] [M]"
    "[C]     [Q] [J]         [V] [Q] [D]"
    "[T]     [S] [Z] [F]     [J] [J] [W]"
    "[N] [G] [T] [S] [V]     [B] [C] [C]"
    "[S] [B] [R] [W] [D] [J] [Q] [R] [Q]"
    "[V] [D] [W] [G] [P] [W] [N] [T] [S]"
    "[B] [W] [F] [L] [M] [F] [L] [G] [J]"
    " 1   2   3   4   5   6   7   8   9 "
    "                                   "
    "move 2 from 6 to 2                 "
    "move 2 from 8 to 7                 "
    "move 3 from 3 to 8                 "
    "move 2 from 5 to 3                 "
    "move 5 from 9 to 7                 "))

(deftest parse-crates-test
  (testing "Day 5 Part 1 - Parse a line of crates to a list"
    (is (= {1 (list "Q") 4 (list "N") 8 (list "N")}
           (day5/parse-crates (first crates-input))))
    (is (= {1 (list "H") 3 (list "B") 4 (list "D") 8 (list "S") 9 (list "M")}
           (day5/parse-crates (second crates-input))))))

(deftest parse-command-test
  (testing "Day 5 Part 1 - Parsing commands"
    (is (= {1 ["Q" "H"] 3 ["B"] 4 [] 7 ["D" "N"] 8 ["N" "S"] 9 ["M"]}
           (day5/parse-command
            "move 15 from 4 to 7"
            {1 ["Q" "H"] 3 ["B"] 4 ["N" "D"] 7 [] 8 ["N" "S"] 9 ["M"]})))))

(deftest parse-command-part-2-test
  (testing "Day 5 Part 2 - Parsing commands part 2"
    (is (= {7 ["A" "B" "C" "Z" "X"] 4 []}
           (day5/parse-command-part-2
             "move 15 from 4 to 7"
             {4 ["A" "B" "C"] 7 ["Z" "X"]})))))

(deftest top-crates-test
  (testing "Day 5 Pat 1 - Get the top crates"
    (is (= "aB"
           (day5/top-crates {1 ["a" "F"] 2 ["B"]} 2)))))

(deftest part-1-test
  (testing "Day 5 Pat 1 - This sucked!"
    (is (= "QWBNFFVNM"
           (day5/part1-2
            (take 11 crates-input))))))