-- ============================================
-- 00_deploy_all.sql
-- One-click deployment for all SQL scripts
-- Run from sql/ directory in gsql/psql
-- ============================================

\set ON_ERROR_STOP on
\echo '== Deploy Start =='

\echo '[1/7] create schema'
\i 01_create_schema.sql

\echo '[2/7] init data'
\i 02_init_data.sql

\echo '[3/7] views'
\i 03_views.sql

\echo '[4/7] indexes'
\i 04_indexes.sql

\echo '[5/7] triggers'
\i 05_triggers.sql

\echo '[6/7] procedures'
\i 06_procedures.sql

\echo '[7/7] queries'
\i 07_queries.sql

\echo '== Deploy Done =='
