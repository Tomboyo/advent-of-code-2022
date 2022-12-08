(ns advent-2022.util 
  (:require [clojure.java.io :as jio]))

(defn solve [solver resource]
  (let [url (jio/resource resource)]
    (with-open [r (jio/reader url)]
      (solver (line-seq r)))))