(ns liberator-hal.ping-resource.core-test
  (:require
    [clojure.test :refer :all]

    [halboy.resource :as hal]
    [halboy.json :as hal-json]

    [ring.mock.request :as ring]
    [ring.middleware.keyword-params :as ring-keyword-params]
    [ring.middleware.params :as ring-params]

    [liberator-hal.ping-resource.core :as ping-resource]))

(def routes
  [""
   [["/" :discovery]
    ["/ping" :ping]]])

(def dependencies
  {:routes routes})

(defn build-handler
  ([dependencies] (build-handler dependencies {}))
  ([dependencies options]
   (let [handler (ping-resource/build-resource-for dependencies options)
         handler (-> handler
                   ring-keyword-params/wrap-keyword-params
                   ring-params/wrap-params)]
     handler)))

(deftest has-status-200
  (let [handler (build-handler dependencies)
        request (ring/request :get "/ping")
        result (handler request)]
    (is (= (:status result) 200))))

(deftest includes-pong-message-by-default
  (let [handler (build-handler dependencies)
        request (ring/request :get "/ping")
        result (handler request)
        resource (hal-json/json->resource (:body result))]
    (is (= (hal/get-property resource :message) "pong"))))

(deftest includes-provided-message-when-specified
  (let [handler (build-handler dependencies {:message "ok"})
        request (ring/request :get "/ping")
        result (handler request)
        resource (hal-json/json->resource (:body result))]
    (is (= (hal/get-property resource :message) "ok"))))

(deftest includes-self-link
  (let [handler (build-handler dependencies)
        request (ring/request :get "http://localhost/ping")
        result (handler request)
        resource (hal-json/json->resource (:body result))]
    (is (= (hal/get-href resource :self) "http://localhost/ping"))))

(deftest includes-discovery-link
  (let [handler (build-handler dependencies)
        request (ring/request :get "http://localhost/ping")
        result (handler request)
        resource (hal-json/json->resource (:body result))]
    (is (= (hal/get-href resource :discovery) "http://localhost/"))))
