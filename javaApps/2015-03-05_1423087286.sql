--
-- 2015-03-05_1423087286.sql
--test
-- PA-26003: Create Country Infotype
--
-- Drop ZZZ backup table
--
-- DML
--
-- Execution plan: PRE-DEPLOYMENT
--
-- PODS
--
-- Author: Charles Spencer
--

IF EXISTS (
	SELECT TOP 1 1
	FROM INFORMATION_SCHEMA.TABLES
	WHERE TABLE_NAME = 'ZZZ_PA26003_Backup_Screening_Version_Item_Info_Types'
)
BEGIN
	DROP TABLE ZZZ_PA26003_Backup_Screening_Version_Item_Info_Types
END
GO

-- DB Update
EXEC addDBUpdate N'2015-03-05_1423087286'
GO
