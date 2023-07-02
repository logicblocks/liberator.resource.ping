(ns liberator.resource.ping.core-test
  (:require
   [clojure.test :refer [deftest is]]

   [halboy.resource :as hal]
   [halboy.json :as hal-json]

   [ring.mock.request :as ring]
   [ring.middleware.keyword-params :as ring-keyword-params]
   [ring.middleware.params :as ring-params]

   [liberator.resource.ping.core :as ping-resource]))

(def router
  [""
   [["/" :discovery]
    ["/ping" :ping]]])

(def dependencies
  {:router router})

(defn resource-handler
  ([dependencies] (resource-handler dependencies {}))
  ([dependencies overrides]
   (let [handler (ping-resource/handler dependencies overrides)
         handler (-> handler
                   ring-keyword-params/wrap-keyword-params
                   ring-params/wrap-params)]
     handler)))

(deftest has-status-200
  (let [handler (resource-handler dependencies)
        request (ring/request :get "/ping")
        result (handler request)]
    (is (= 200 (:status result)))))

(deftest includes-self-link
  (let [handler (resource-handler dependencies)
        request (ring/request :get "http://localhost/ping")
        result (handler request)
        resource (hal-json/json->resource (:body result))]
    (is (= "http://localhost/ping" (hal/get-href resource :self)))))

(deftest includes-discovery-link
  (let [handler (resource-handler dependencies)
        request (ring/request :get "http://localhost/ping")
        result (handler request)
        resource (hal-json/json->resource (:body result))]
    (is (= "http://localhost/" (hal/get-href resource :discovery)))))

(deftest includes-pong-message-in-body-by-default
  (let [handler (resource-handler dependencies)
        request (ring/request :get "/ping")
        result (handler request)
        resource (hal-json/json->resource (:body result))]
    (is (= "pong" (hal/get-property resource :message)))))

(deftest allows-body-to-be-overridden-with-data
  (let [handler (resource-handler dependencies
                  {:body {:message "ok"}})
        request (ring/request :get "/ping")
        result (handler request)
        resource (hal-json/json->resource (:body result))]
    (is (= "ok" (hal/get-property resource :message)))))

(deftest allows-body-to-be-overridden-with-function-of-context
  (let [handler (resource-handler dependencies
                  {:body (fn [{:keys [request]}]
                           (select-keys request [:request-method]))})
        request (ring/request :get "/ping")
        result (handler request)
        resource (hal-json/json->resource (:body result))]
    (is (= "get" (hal/get-property resource :requestMethod)))))

(deftest allows-override-definitions
  (let [handler (resource-handler dependencies {:exists? false})
        request (ring/request :get "http://localhost/ping")
        result (handler request)]
    (is (= 404 (:status result)))))
