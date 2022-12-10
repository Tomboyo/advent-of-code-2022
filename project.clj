(defproject advent-2022 "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME" 
  :dependencies [
                 [org.clojure/clojure "1.11.1"]
                 [org.clojure/tools.trace "0.7.11"]]
  :main ^:skip-aot advent-2022.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
