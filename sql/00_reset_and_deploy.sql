-- ============================================
-- 00_reset_and_deploy.sql
-- Reset public schema and redeploy all scripts
-- For demo/local re-initialization only
-- ============================================

\set ON_ERROR_STOP on
\echo '== Reset Start =='

DROP SCHEMA IF EXISTS public CASCADE;
CREATE SCHEMA public;

\echo '== Schema Reset Done =='
\i 00_deploy_all.sql
