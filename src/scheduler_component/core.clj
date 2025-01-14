(ns scheduler-component.core
  (:require [chime.core :as chime]
            [clojure.tools.logging :as log]
            [integrant.core :as ig]))

(defmethod ig/init-key ::scheduler
  [_ {:keys [jobs components]}]
  (log/info :starting ::scheduler)
  (doseq [job (keys jobs)
          :let [job' (get jobs job)]]
    (chime/chime-at ((:schedule job'))
                    (partial (:handler job') components))))

(defmethod ig/halt-key! ::scheduler
  [_ _]
  (log/info :stopping ::scheduler))
