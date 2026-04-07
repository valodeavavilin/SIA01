--------------------------------------------------------------------------------
-- FDBO ORDS REST MODULES - COMPLETE PROJECT VERSION
-- CONNECT with FDBO on XEPDB1
--------------------------------------------------------------------------------

BEGIN
    ORDS.delete_module(
        p_module_name => 'fdbo.olap.api'
    );
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        NULL;
END;
/
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
DECLARE
    PROCEDURE add_get_endpoint(
        p_module_name IN VARCHAR2,
        p_pattern     IN VARCHAR2,
        p_sql         IN CLOB
    ) IS
    BEGIN
        ORDS.DEFINE_TEMPLATE(
            p_module_name => p_module_name,
            p_pattern     => p_pattern,
            p_priority    => 0,
            p_etag_type   => 'NONE',
            p_etag_query  => NULL,
            p_comments    => NULL
        );

        ORDS.DEFINE_HANDLER(
            p_module_name    => p_module_name,
            p_pattern        => p_pattern,
            p_method         => 'GET',
            p_source_type    => 'json/collection',
            p_items_per_page => 25,
            p_mimes_allowed  => '',
            p_comments       => NULL,
            p_source         => p_sql
        );
    END;
BEGIN
    --------------------------------------------------------------------------
    -- DEFINE MODULE
    --------------------------------------------------------------------------
    ORDS.DEFINE_MODULE(
        p_module_name    => 'fdbo.olap.api',
        p_base_path      => '/olap/',
        p_items_per_page => 25,
        p_status         => 'PUBLISHED',
        p_comments       => 'OLAP and analytical REST endpoints for FDBO project'
    );

    --------------------------------------------------------------------------
    -- OLAP VIEWS
    --------------------------------------------------------------------------
    add_get_endpoint(
        p_module_name => 'fdbo.olap.api',
        p_pattern     => 'calendar',
        p_sql         => q'[
            SELECT *
            FROM OLAP_VIEW_TXN_CALENDAR_V
        ]'
    );

    add_get_endpoint(
        p_module_name => 'fdbo.olap.api',
        p_pattern     => 'merchant-geo',
        p_sql         => q'[
            SELECT *
            FROM OLAP_VIEW_TXN_MERCHANT_GEO_V
        ]'
    );

    add_get_endpoint(
        p_module_name => 'fdbo.olap.api',
        p_pattern     => 'credit-age',
        p_sql         => q'[
            SELECT *
            FROM OLAP_VIEW_TXN_CREDIT_AGE_V
        ]'
    );

    add_get_endpoint(
        p_module_name => 'fdbo.olap.api',
        p_pattern     => 'card-brand-type',
        p_sql         => q'[
            SELECT *
            FROM OLAP_VIEW_TXN_CARD_BRAND_TYPE_V
        ]'
    );

    add_get_endpoint(
        p_module_name => 'fdbo.olap.api',
        p_pattern     => 'source-channel',
        p_sql         => q'[
            SELECT *
            FROM OLAP_VIEW_TXN_SOURCE_CHANNEL_V
        ]'
    );

    add_get_endpoint(
        p_module_name => 'fdbo.olap.api',
        p_pattern     => 'state-brand-cube',
        p_sql         => q'[
            SELECT *
            FROM OLAP_VIEW_TXN_STATE_BRAND_CUBE_V
        ]'
    );

    add_get_endpoint(
        p_module_name => 'fdbo.olap.api',
        p_pattern     => 'income-debt',
        p_sql         => q'[
            SELECT *
            FROM OLAP_VIEW_TXN_INCOME_DEBT_V
        ]'
    );

    add_get_endpoint(
        p_module_name => 'fdbo.olap.api',
        p_pattern     => 'card-security-cube',
        p_sql         => q'[
            SELECT *
            FROM OLAP_VIEW_TXN_CARD_SECURITY_CUBE_V
        ]'
    );

    add_get_endpoint(
        p_module_name => 'fdbo.olap.api',
        p_pattern     => 'year-state-source-gsets',
        p_sql         => q'[
            SELECT *
            FROM OLAP_VIEW_TXN_YEAR_STATE_SOURCE_GSETS_V
        ]'
    );

    add_get_endpoint(
        p_module_name => 'fdbo.olap.api',
        p_pattern     => 'month-income-mcc-gsets',
        p_sql         => q'[
            SELECT *
            FROM OLAP_VIEW_TXN_MONTH_INCOME_MCC_GSETS_V
        ]'
    );

    --------------------------------------------------------------------------
    -- WINDOW FUNCTION VIEWS
    --------------------------------------------------------------------------
    add_get_endpoint(
        p_module_name => 'fdbo.olap.api',
        p_pattern     => 'running-total-client',
        p_sql         => q'[
            SELECT *
            FROM WV_TXN_RUNNING_TOTAL_CLIENT_V
        ]'
    );

    add_get_endpoint(
        p_module_name => 'fdbo.olap.api',
        p_pattern     => 'client-avg-diff',
        p_sql         => q'[
            SELECT *
            FROM WV_TXN_CLIENT_AVG_DIFF_V
        ]'
    );

    add_get_endpoint(
        p_module_name => 'fdbo.olap.api',
        p_pattern     => 'card-total-rank',
        p_sql         => q'[
            SELECT *
            FROM WV_CARD_TOTAL_RANK_V
        ]'
    );

    add_get_endpoint(
        p_module_name => 'fdbo.olap.api',
        p_pattern     => 'month-share-running',
        p_sql         => q'[
            SELECT *
            FROM WV_TXN_MONTH_SHARE_RUNNING_V
        ]'
    );

    add_get_endpoint(
        p_module_name => 'fdbo.olap.api',
        p_pattern     => 'client-first-last-top',
        p_sql         => q'[
            SELECT *
            FROM WV_TXN_CLIENT_FIRST_LAST_TOP_V
        ]'
    );

    add_get_endpoint(
        p_module_name => 'fdbo.olap.api',
        p_pattern     => 'merchant-state-rank',
        p_sql         => q'[
            SELECT *
            FROM WV_MERCHANT_STATE_RANK_V
        ]'
    );

    add_get_endpoint(
        p_module_name => 'fdbo.olap.api',
        p_pattern     => 'credit-month-performance',
        p_sql         => q'[
            SELECT *
            FROM WV_CREDIT_MONTH_PERFORMANCE_V
            ORDER BY txn_year, txn_month, credit_score_group
        ]'
    );

    COMMIT;
END;
/
--template pt endpoint pe FACT_TRANSACTIONS_V
BEGIN
    ORDS.DEFINE_TEMPLATE(
        p_module_name => 'fdbo.olap.api',
        p_pattern     => 'fact-transactions',
        p_priority    => 0,
        p_etag_type   => 'NONE',
        p_etag_query  => NULL,
        p_comments    => 'Fact transactions endpoint'
    );

    ORDS.DEFINE_HANDLER(
        p_module_name    => 'fdbo.olap.api',
        p_pattern        => 'fact-transactions',
        p_method         => 'GET',
        p_source_type    => 'json/collection',
        p_items_per_page => 25,
        p_mimes_allowed  => '',
        p_comments       => 'Returns FACT_TRANSACTIONS_V',
        p_source         => q'[
            SELECT *
            FROM FACT_TRANSACTIONS_V
            ORDER BY txn_date, txn_id
        ]'
    );

    COMMIT;
END;
/