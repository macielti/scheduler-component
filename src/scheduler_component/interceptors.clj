(ns scheduler-component.interceptors
  (:require [iapetos.core :as prometheus]
            [io.pedestal.interceptor :as pedestal.interceptor]))

(def job-execution-timing-interceptor
  (pedestal.interceptor/interceptor
   {:name  ::job-execution-timing-interceptor
    :enter (fn [context]
             (assoc context ::start-ms (System/currentTimeMillis)))
    :leave (fn [{:keys [components] :as context}]
             (let [{::keys [start-ms]} context
                   prometheus-registry (get-in components [:prometheus :registry])
                   service-name (get-in components [:config :service-name])
                   elapsed-ms (- (System/currentTimeMillis) start-ms)
                   job-id (get-in context [:job-id])]
               (prometheus/observe prometheus-registry :job-execution-timing
                                   {:service (name service-name)
                                    :job-id  (name job-id)}
                                   elapsed-ms)
               (dissoc context ::start-ms)))}))
