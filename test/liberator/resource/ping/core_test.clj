(ns liberator.resource.ping.core-test
  (:require
   [clojure.test :refer :all]

   [halboy.resource :as hal]
   [halboy.json :as hal-json]

   [ring.mock.request :as ring]
   [ring.middleware.keyword-params :as ring-keyword-params]
   [ring.middleware.params :as ring-params]

   [liberator.resource.ping.core :as ping-resource]))

(def routes
  [""
   [["/" :discovery]
    ["/ping" :ping]]])

(def dependencies
  {:routes routes})

(defn resource-handler
  ([dependencies] (resource-handler dependencies {}))
  ([dependencies options]
   (let [handler (ping-resource/handler dependencies options)
         handler (-> handler
                   ring-keyword-params/wrap-keyword-params
                   ring-params/wrap-params)]
     handler)))

(deftest has-status-200
  (let [handler (resource-handler dependencies)
        request (ring/request :get "/ping")
        result (handler request)]
    (is (= (:status result) 200))))

(deftest includes-pong-message-by-default
  (let [handler (resource-handler dependencies)
        request (ring/request :get "/ping")
        result (handler request)
        resource (hal-json/json->resource (:body result))]
    (is (= (hal/get-property resource :message) "pong"))))

(deftest includes-provided-message-when-specified
  (let [handler (resource-handler dependencies {:message "ok"})
        request (ring/request :get "/ping")
        result (handler request)
        resource (hal-json/json->resource (:body result))]
    (is (= (hal/get-property resource :message) "ok"))))

(deftest includes-self-link
  (let [handler (resource-handler dependencies)
        request (ring/request :get "http://localhost/ping")
        result (handler request)
        resource (hal-json/json->resource (:body result))]
    (is (= (hal/get-href resource :self) "http://localhost/ping"))))

(deftest includes-discovery-link
  (let [handler (resource-handler dependencies)
        request (ring/request :get "http://localhost/ping")
        result (handler request)
        resource (hal-json/json->resource (:body result))]
    (is (= (hal/get-href resource :discovery) "http://localhost/"))))
