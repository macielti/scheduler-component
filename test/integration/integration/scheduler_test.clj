(ns integration.scheduler-test
  (:require [chime.core :as chime]
            [clojure.test :refer [is testing]]
            [integrant.core :as ig]
            [scheduler-component.core :as component.scheduler]
            [schema.test :as s])
  (:import (java.time Duration Instant)))

(def test-state (atom nil))

(defn every-5-seconds [] (chime/periodic-seq (Instant/now) (Duration/ofSeconds 5)))

(def jobs
  {:every-5-seconds-jon-test {:schedule every-5-seconds
                              :handler  (fn [context]
                                          (println "Hello world!")
                                          (swap! test-state conj (:as-of context)))}})

(def system-setup
  {::component.scheduler/scheduler {:jobs       jobs
                                    :components {:config {:hello :world}}}})

(s/deftest schedule-job-test
  (reset! test-state [])
  (testing "Should be able to schedule a job for every 5 minutes"
    (let [system (ig/init system-setup)]
      (Thread/sleep 7000)
      (is (= 2 (count @test-state)))
      (ig/halt! system))))

(def test-state-interceptors (atom nil))

(def jobs-with-interceptor
  {:every-5-seconds-jon-test {:schedule     every-5-seconds
                              :interceptors [(component.scheduler/handler-fn->interceptor (fn [context]
                                                                                            (swap! test-state-interceptors assoc :interceptor :executed)
                                                                                            context))]
                              :handler      (fn [_context]
                                              (println "Hello world! - Interceptors")
                                              (swap! test-state-interceptors assoc :job-handler-fn :executed))}})

(def system-setup-interceptors
  {::component.scheduler/scheduler {:jobs       jobs-with-interceptor
                                    :components {:config {:hello :world}}}})

(s/deftest schedule-job-interceptors-test
  (reset! test-state {})
  (testing "Should be able to schedule a job for every 5 minutes"
    (let [system (ig/init system-setup-interceptors)]
      (Thread/sleep 5000)
      (is (= {:interceptor    :executed
              :job-handler-fn :executed} @test-state-interceptors))
      (ig/halt! system))))
