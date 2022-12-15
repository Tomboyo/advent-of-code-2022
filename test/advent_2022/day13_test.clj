(ns advent-2022.day13-test
  (:require [advent-2022.day13 :refer [inorder?]]
            [clojure.test :refer [deftest testing is]]))

(deftest inorder-test
  (testing "integers"
    (is (= :<
           (inorder? 1 2)))
    (is (= :>
           (inorder? 2 1))))
  (testing "sequences"
    (is (= :<
           (inorder? [1] [2])))
    (is (= :>
           (inorder? [2] [1])))
    (is (= :<
           (inorder? [] [1])))
    (is (= :>
           (inorder? [1] [])))
    (is (= :<
           (inorder? [1 2 3] [2]))))
  (testing "integer and sequence pairs"
    (is (= :<
           (inorder? [0] 1)))
    (is (= :>
           (inorder? [1] 0)))
    (is (= :<
           (inorder? 0 [1])))
    (is (= :>
           (inorder? 1 [0])))
    (is (= :<
           (inorder? [1 1 3 1 1] [1 1 5 1 1])))
    (is (= :<
           (inorder? [[1] [2 3 4]]
                     [[1] 4]))))
  (testing "nested sequences"
    (is (= :<
           (inorder? [1 [2 [3 [4 [5 6 7]]]] 8 9]
                     [1 [2 [3 [4 [5 7 7]]]] 8 9])))))