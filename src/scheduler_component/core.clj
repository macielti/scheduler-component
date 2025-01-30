(ns scheduler-component.core
  (:require [chime.core :as chime]
            [clojure.tools.logging :as log]
            [integrant.core :as ig]
            [io.pedestal.interceptor :as interceptor]
            [io.pedestal.interceptor.chain :as chain]))

(defn handler-fn->interceptor
  [handler-fn]
  (interceptor/interceptor
   {:name  ::job-handler-fn-interceptor
    :enter (fn [context]
             (handler-fn context)
             context)}))

(defmethod ig/init-key ::scheduler
  [_ {:keys [jobs components]}]
  (log/info :starting ::scheduler)
  {:channels (mapv (fn [job-key-id]
                     (let [job' (get jobs job-key-id)]
                       (chime/chime-at ((:schedule job'))
                                       (fn [as-of]
                                         (chain/execute {:components components
                                                         :job-id     job-key-id
                                                         :as-of      as-of}
                                                        (conj (or (:interceptors job') []) (handler-fn->interceptor (:handler job'))))))))
                   (keys jobs))})

(defmethod ig/halt-key! ::scheduler
  [_ scheduler]
  (log/info :stopping ::scheduler)
  (doseq [chan (:channels scheduler)]
    (.close chan)))
