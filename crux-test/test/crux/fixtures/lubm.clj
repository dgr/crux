(ns crux.fixtures.lubm
  (:require [crux.fixtures.api :refer [*api*]]
            [crux.api :as api]
            [crux.rdf :as rdf]))

(def ^:const lubm-triples-resource "lubm/University0_0.ntriples")

(defn with-lubm-data [f]
  (let [tx-ops (->> (concat (rdf/->tx-ops (rdf/ntriples "lubm/univ-bench.ntriples"))
                            (rdf/->tx-ops (rdf/ntriples lubm-triples-resource)))
                    (rdf/->default-language)
                    vec)]
    (doseq [tx-ops (partition-all 1000 tx-ops)]
      (api/submit-tx *api* (vec tx-ops)))
    (api/sync *api* nil)
    (f)))
