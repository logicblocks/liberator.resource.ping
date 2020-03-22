(ns liberator-hal.ping-resource.core-test
  (:require
    [clojure.test :refer :all]

    [ring.mock.request :as ring]
    [ring.middleware.keyword-params :as ring-keyword-params]
    [ring.middleware.params :as ring-params]

    [liberator-hal.ping-resource.core :as ping-resource]))

(deftest has-status-200
  (let [routes [["/" :discovery]
                ["/ping" :ping]]
        dependencies {:routes routes}
        resource (ping-resource/build-for dependencies)
        handler (-> resource
                  ring-keyword-params/wrap-keyword-params
                  ring-params/wrap-params)
        request (ring/request :get "/ping")
        result (handler request)]
    (is (= (:status result) 200))))
