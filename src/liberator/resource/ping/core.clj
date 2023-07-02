(ns liberator.resource.ping.core
  (:require
   [halboy.resource :as hal]
   [hype.core :as hype]
   [liberator.mixin.core :as mixin]
   [liberator.mixin.json.core :as json-mixin]
   [liberator.mixin.hypermedia.core :as hypermedia-mixin]
   [liberator.mixin.hal.core :as hal-mixin]))

(defn definitions
  ([{:keys [router]}]
   {:body (fn [_] {:message "pong"})

    :handle-ok
    (fn [{:keys [request resource] :as context}]
      (let [body-fn (:body resource)
            body (body-fn context)]
        (hal/add-properties
          (hal/new-resource
            (hype/absolute-url-for request router :ping))
          body)))}))

(defn handler
  ([dependencies]
   (handler dependencies {}))
  ([dependencies overrides]
   (mixin/build-resource
     (json-mixin/with-json-mixin dependencies)
     (hypermedia-mixin/with-hypermedia-mixin dependencies)
     (hal-mixin/with-hal-mixin dependencies)
     (definitions dependencies)
     overrides)))
