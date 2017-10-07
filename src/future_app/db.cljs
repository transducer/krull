(ns future-app.db
  (:require [clojure.spec.alpha :as s]))

;; Spec of app-db
(s/def ::greeting        string?)
(s/def ::pos-int         #(and (integer? %) (pos? %)))
(s/def ::pos-float       #(and (float? %) (pos? %)))
(s/def ::person-count    ::pos-int)
(s/def ::longitude       ::pos-float)
(s/def ::latitude        ::pos-float)
(s/def ::longitude-delta ::pos-float)
(s/def ::latitude-delta  ::pos-float)
(s/def ::coordinate      (s/keys :req-un [::longitude
                                          ::latitude]))
(s/def ::initial-region  (s/merge ::coordinate
                                  (s/keys :req-un [::latitude-delta
                                                   ::longitude-delta])))
(s/def ::krul            (s/keys :req-un [::person-count
                                          ::coordinate]))
(s/def ::krullen         (s/coll-of ::krul :min-count 1))
(s/def ::app-db          (s/keys :req-un [::greeting
                                          ::krullen
                                          ::initial-region]))

(def region-center-of-amsterdam
  {:latitude        52.3743
   :longitude       4.8985
   :latitude-delta  0.0422
   :longitude-delta 0.0421})

(def urinoirs_stadsdeel_centrum
  [{:person-count 1
    :coordinate   {:longitude 4.8985819465932
                   :latitude  52.373166656113}}
   {:person-count 1
    :coordinate   {:longitude 4.8916544694664
                   :latitude  52.37941128307}}
   {:person-count 2
    :coordinate   {:longitude 4.8863435621382
                   :latitude  52.38151003607}}
   {:person-count 1
    :coordinate   {:longitude 4.8836679172734
                   :latitude  52.38432082578}}
   {:person-count 1
    :coordinate   {:longitude 4.9116829404877
                   :latitude  52.370209114543}}
   {:person-count 2
    :coordinate   {:longitude 4.8848811734158
                   :latitude  52.374466469619}}
   {:person-count 2
    :coordinate   {:longitude 4.8897232344814
                   :latitude  52.364348767479}}
   {:person-count 1
    :coordinate   {:longitude 4.8985811297742
                   :latitude  52.363909308194}}
   {:person-count 1
    :coordinate   {:longitude 4.8839971370689
                   :latitude  52.363075272972}}
   {:person-count 1
    :coordinate   {:longitude 4.886387866876
                   :latitude  52.361979919758}}
   {:person-count 1
    :coordinate   {:longitude 4.8807103423627
                   :latitude  52.379634609958}}
   {:person-count 1
    :coordinate   {:longitude 4.9041082008076
                   :latitude  52.375418281364}}
   {:person-count 1
    :coordinate   {:longitude 4.8981476714186
                   :latitude  52.372598637777}}
   {:person-count 1
    :coordinate   {:longitude 4.8989057267204
                   :latitude  52.37443525797}}
   {:person-count 1
    :coordinate   {:longitude 4.8982214079323
                   :latitude  52.373902159507}}
   {:person-count 1
    :coordinate   {:longitude 4.9131194958343
                   :latitude  52.367689330391}}
   {:person-count 1
    :coordinate   {:longitude 4.9184413460411
                   :latitude  52.364277216425}}
   {:person-count 1
    :coordinate   {:longitude 4.8868842635452
                   :latitude  52.379148556978}}
   {:person-count 1
    :coordinate   {:longitude 4.8825069360626
                   :latitude  52.370178220853}}
   {:person-count 1
    :coordinate   {:longitude 4.8838693938594
                   :latitude  52.365276723726}}
   {:person-count 1
    :coordinate   {:longitude 4.8961791772491
                   :latitude  52.362056886821}}
   {:person-count 1
    :coordinate   {:longitude 4.9072870826738
                   :latitude  52.361212653024}}
   {:person-count 1
    :coordinate   {:longitude 4.8940531831324
                   :latitude  52.378980899629}}
   {:person-count 1
    :coordinate   {:longitude 4.8924439090657
                   :latitude  52.377104745373}}
   {:person-count 1
    :coordinate   {:longitude 4.8890382081885
                   :latitude  52.373054986197}}
   {:person-count 1
    :coordinate   {:longitude 4.8883635019961
                   :latitude  52.370373807002}}
   {:person-count 1
    :coordinate   {:longitude 4.9050265533634
                   :latitude  52.365238953419}}
   {:person-count 1
    :coordinate   {:longitude 4.8977251429251
                   :latitude  52.378951196089}}
   {:person-count 1
    :coordinate   {:longitude 4.8900868937889
                   :latitude  52.367271305051}}
   {:person-count 1
    :coordinate   {:longitude 4.8894949823932
                   :latitude  52.374225309289}}
   {:person-count 1
    :coordinate   {:longitude 4.8770610191409
                   :latitude  52.36875279656}}
   {:person-count 1
    :coordinate   {:longitude 4.9108915274942
                   :latitude  52.363231471027}}
   {:person-count 1
    :coordinate   {:longitude 4.9006520856942
                   :latitude  52.373202143815}}
   {:person-count 2
    :coordinate   {:longitude 4.8955026667753
                   :latitude  52.37143725676}}
   {:person-count 1
    :coordinate   {:longitude 4.8801923163085
                   :latitude  52.381268154977}}])

;; initial state of app-db
(def app-db
  {:greeting       "Hello Clojure in iOS and Android!"
   :krullen        urinoirs_stadsdeel_centrum
   :initial-region region-center-of-amsterdam})
