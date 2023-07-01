(ns liberator.resource.ping.core
  (:require
   [halboy.resource :as hal]
   [hype.core :as hype]
   [liberator.mixin.core :as mixin]
   [liberator.mixin.json.core :as json-mixin]
   [liberator.mixin.hypermedia.core :as hypermedia-mixin]
   [liberator.mixin.hal.core :as hal-mixin]))

(defn definitions
  ([dependencies] (definitions dependencies {}))
  ([{:keys [routes]}
    {:keys [message]
     :or {message "pong"}}]
   {:handle-ok
    (fn [{:keys [request]}]
      (hal/add-properties
        (hal/new-resource
          (hype/absolute-url-for request routes :ping))
        {:message message}))}))

(defn handler
  ([dependencies] (handler dependencies {}))
  ([dependencies options]
   (mixin/build-resource
     (json-mixin/with-json-mixin dependencies)
     (hypermedia-mixin/with-hypermedia-mixin dependencies)
     (hal-mixin/with-hal-mixin dependencies)
     (definitions dependencies options))))
