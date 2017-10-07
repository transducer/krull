(ns future-app.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :krullen
  (fn [db _]
    (:krullen db)))

(reg-sub
 :initial-region
 (fn [db _]
   (:initial-region db)))
