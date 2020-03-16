(defproject io.logicblocks/liberator-hal-ping-resource "0.1.0-SNAPSHOT"
  :description "A HAL ping resource for liberator"
  :url "https://github.com/logicblocks/liberator-hal-ping-resource"

  :license {:name "The MIT License"
            :url  "https://opensource.org/licenses/MIT"}

  :plugins [[lein-eftest "0.5.8"]]

  :profiles {:shared {:dependencies [[org.clojure/clojure "1.10.1"]
                                     [nrepl "0.6.0"]
                                     [eftest "0.5.8"]]}
             :dev    [:shared {:source-paths ["dev"]
                               :eftest       {:multithread? false}}]
             :test   [:shared {:eftest {:multithread? false}}]}

  :aliases {"test" ["with-profile" "test" "eftest" ":all"]})
