(ns future-app.android.core
  (:require [future-app.events]
            [future-app.subs]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [reagent.core :as r :refer [atom]]))

(def ReactNative (js/require "react-native"))


(def app-registry (.-AppRegistry ReactNative))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))

(def logo-img (js/require "./images/cljs.png"))

(defn alert [title msg]
  (.alert (.-Alert ReactNative) title msg))

(def MapView (js/require "react-native-maps"))

(def map-view (r/adapt-react-class (js/require "react-native-maps")))
(def map-marker (r/adapt-react-class (.-Marker MapView)))
(def map-circle (r/adapt-react-class (.-Circle MapView)))


(defn set-current-position!
  "Gets current position via js/navigator API and sets it on the atom
  current-pos"
  [current-pos]
  (-> (.-geolocation js/navigator)
      (.getCurrentPosition
       (fn [js-position]
         (let [{{:keys [longitude latitude]} :coords}
               (js->clj js-position :keywordize-keys true)]
           (swap! current-pos assoc
                  :longitude longitude
                  :latitude latitude))))))

(defn distance
  "Calculates the distance in meter between two points of lat/long using the
  Haversine formula.

  Takes two positions with maps of latitude and longitude.

  Based on https://github.com/ThomasMeier/haversine"
  [{lat1 :latitude long1 :longitude :as pos1}
   {lat2 :latitude long2 :longitude :as pos2}]
  (let [earth-radius-m 6372800
        to-radians     #(/ (* % (.-PI js/Math)) 180)
        cos            (.-cos js/Math)
        asin           (.-asin js/Math)
        sin            (.-sin js/Math)
        sin2           #(* (sin %) (sin %))
        sqrt           (.-sqrt js/Math)
        alpha          (fn [lat1 lat2 delta-lat delta-long]
                         (+ (sin2 (/ delta-lat 2))
                            (* (sin2 (/ delta-long 2))
                               (cos lat1) (cos lat2))))
        delta-lat      (to-radians (- lat2 lat1))
        delta-long     (to-radians (- long2 long1))
        lat1           (to-radians lat1)
        lat2           (to-radians lat2)]
    (* 2 earth-radius-m
       (asin (sqrt (alpha lat1 lat2 delta-lat delta-long))))))


(defn distances-to-pos
  "Creates a map from distance to position.
  The distance reflects the distance from `position` to the position in
  `positions`."
  [positions position]
  (->> positions
       (map (fn [pos]
              [(distance pos position) pos]))
       (into {})))


(defn nearest
  "Find nearest position from `positions` to `position`."
  [positions position]
  (let [distance     key
        shortest     first
        get-distance second]
    (->> (distances-to-pos positions position)
         (sort-by distance)
         shortest
         get-distance)))


(defn krullen-map []
  (let [krullen        (subscribe [:krullen])
        initial-region (subscribe [:initial-region])
        current-pos    (r/atom {:latitude 0 :longitude 0})]
    (fn []
      (let [nearest-krul-coord (nearest (keep :coordinate @krullen)
                                        @current-pos)]
        [map-view {:style            {:flex 1}
                   :initial-region   @initial-region
                   :on-region-change #(fn [position])}
         (do
           (set-current-position! current-pos)

           (conj
            (for [{:keys [coordinate person-count] :as krul}
                  (filter (fn [{:keys [coordinate] :as krul}]
                            (= coordinate nearest-krul-coord))
                          @krullen)]
                      ^{:key krul}
                      [map-marker {:coordinate coordinate
                                   :title      (str "Aantal: "
                                                    person-count)}])
            [map-marker {:key        :current-pos
                         :title      "HALLO"
                         :coordinate @current-pos}]))]))))


(defn app-root []
  [view {:style {:flex             1
                 :background-color "white"}}
   [krullen-map]])


(defn init []
  (dispatch-sync [:initialize-db])
  (.registerComponent app-registry "FutureApp"
                      #(r/reactify-component app-root)))
