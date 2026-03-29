BEGIN
  ORDS.ENABLE_SCHEMA(
    p_enabled             => TRUE,
    p_schema              => 'FDBO',
    p_url_mapping_type    => 'BASE_PATH',
    p_url_mapping_pattern => 'fdbo',
    p_auto_rest_auth      => FALSE
  );
  COMMIT;
END;
/

BEGIN
  ORDS.ENABLE_OBJECT(
    p_enabled      => TRUE,
    p_schema       => 'FDBO',
    p_object       => 'OLAP_VIEW_TXN_CALENDAR_V',
    p_object_type  => 'VIEW',
    p_object_alias => 'olap_view_txn_calendar_v'
  );
  COMMIT;
END;
/

BEGIN
  ORDS.ENABLE_OBJECT(TRUE, 'FDBO', 'OLAP_VIEW_TXN_MERCHANT_GEO_V', 'VIEW', 'olap_view_txn_merchant_geo_v');
  ORDS.ENABLE_OBJECT(TRUE, 'FDBO', 'OLAP_VIEW_TXN_CREDIT_AGE_V', 'VIEW', 'olap_view_txn_credit_age_v');
  ORDS.ENABLE_OBJECT(TRUE, 'FDBO', 'OLAP_VIEW_TXN_CARD_BRAND_TYPE_V', 'VIEW', 'olap_view_txn_card_brand_type_v');
  ORDS.ENABLE_OBJECT(TRUE, 'FDBO', 'OLAP_VIEW_TXN_SOURCE_CHANNEL_V', 'VIEW', 'olap_view_txn_source_channel_v');
  ORDS.ENABLE_OBJECT(TRUE, 'FDBO', 'OLAP_VIEW_TXN_STATE_BRAND_CUBE_V', 'VIEW', 'olap_view_txn_state_brand_cube_v');
  ORDS.ENABLE_OBJECT(TRUE, 'FDBO', 'OLAP_VIEW_TXN_INCOME_DEBT_V', 'VIEW', 'olap_view_txn_income_debt_v');
  ORDS.ENABLE_OBJECT(TRUE, 'FDBO', 'OLAP_VIEW_TXN_CARD_SECURITY_CUBE_V', 'VIEW', 'olap_view_txn_card_security_cube_v');
  ORDS.ENABLE_OBJECT(TRUE, 'FDBO', 'OLAP_VIEW_TXN_YEAR_STATE_SOURCE_GSETS_V', 'VIEW', 'olap_view_txn_year_state_source_gsets_v');
  ORDS.ENABLE_OBJECT(TRUE, 'FDBO', 'OLAP_VIEW_TXN_MONTH_INCOME_MCC_GSETS_V', 'VIEW', 'olap_view_txn_month_income_mcc_gsets_v');
  COMMIT;
END;
/

http://localhost:8080/ords/fdbo/olap_view_txn_calendar_v/
http://localhost:8080/ords/fdbo/olap_view_txn_merchant_geo_v/
http://localhost:8080/ords/fdbo/olap_view_txn_credit_age_v/
http://localhost:8080/ords/fdbo/olap_view_txn_card_brand_type_v/
http://localhost:8080/ords/fdbo/olap_view_txn_source_channel_v/
http://localhost:8080/ords/fdbo/olap_view_txn_state_brand_cube_v/
http://localhost:8080/ords/fdbo/olap_view_txn_income_debt_v/
http://localhost:8080/ords/fdbo/olap_view_txn_card_security_cube_v/
http://localhost:8080/ords/fdbo/olap_view_txn_year_state_source_gsets_v/
http://localhost:8080/ords/fdbo/olap_view_txn_month_income_mcc_gsets_v/


BEGIN
  ORDS.ENABLE_OBJECT(TRUE, 'FDBO', 'WV_TXN_RUNNING_TOTAL_CLIENT_V', 'VIEW', 'wv_txn_running_total_client_v');
  ORDS.ENABLE_OBJECT(TRUE, 'FDBO', 'WV_TXN_CLIENT_AVG_DIFF_V', 'VIEW', 'wv_txn_client_avg_diff_v');
  ORDS.ENABLE_OBJECT(TRUE, 'FDBO', 'WV_CARD_TOTAL_RANK_V', 'VIEW', 'wv_card_total_rank_v');
  ORDS.ENABLE_OBJECT(TRUE, 'FDBO', 'WV_TXN_MONTH_SHARE_RUNNING_V', 'VIEW', 'wv_txn_month_share_running_v');
  ORDS.ENABLE_OBJECT(TRUE, 'FDBO', 'WV_TXN_CLIENT_FIRST_LAST_TOP_V', 'VIEW', 'wv_txn_client_first_last_top_v');
  ORDS.ENABLE_OBJECT(TRUE, 'FDBO', 'WV_MERCHANT_STATE_RANK_V', 'VIEW', 'wv_merchant_state_rank_v');
  ORDS.ENABLE_OBJECT(TRUE, 'FDBO', 'WV_CREDIT_MONTH_PERFORMANCE_V', 'VIEW', 'wv_credit_month_performance_v');
  COMMIT;
END;
/


http://localhost:8080/ords/fdbo/wv_txn_running_total_client_v/
http://localhost:8080/ords/fdbo/wv_txn_client_avg_diff_v/
http://localhost:8080/ords/fdbo/wv_card_total_rank_v/
http://localhost:8080/ords/fdbo/wv_txn_month_share_running_v/
http://localhost:8080/ords/fdbo/wv_txn_client_first_last_top_v/
http://localhost:8080/ords/fdbo/wv_merchant_state_rank_v/
http://localhost:8080/ords/fdbo/wv_credit_month_performance_v/