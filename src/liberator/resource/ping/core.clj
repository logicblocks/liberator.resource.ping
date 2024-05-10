(ns liberator.resource.ping.core
  (:require
   [halboy.resource :as hal]
   [hype.core :as hype]
   [liberator.mixin.core :as lm-core]
   [liberator.mixin.util :as lm-util]
   [liberator.mixin.json.core :as json-mixin]
   [liberator.mixin.hypermedia.core :as hypermedia-mixin]
   [liberator.mixin.hal.core :as hal-mixin]))

(defn definitions
  ([_]
   {:body (fn [_] {:message "pong"})

    :self-link
    (fn [{:keys [request router]}]
      (hype/absolute-url-for request router :ping))

    :handle-ok
    (fn [context]
      (let [body (lm-util/resource-attribute-as-value context :body)
            self-link (lm-util/resource-attribute-as-value context :self-link)]
        (hal/add-properties
          (hal/new-resource self-link)
          body)))}))

(defn handler
  ([dependencies]
   (handler dependencies {}))
  ([dependencies overrides]
   (lm-core/build-resource
     (json-mixin/with-json-mixin dependencies)
     (hypermedia-mixin/with-hypermedia-mixin dependencies)
     (hal-mixin/with-hal-mixin dependencies)
     (definitions dependencies)
     overrides)))
