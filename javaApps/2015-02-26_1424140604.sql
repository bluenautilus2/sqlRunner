-- 2015-02-26_1424140604.sql
-- PA-27079: Athens Services Custom Portal
-- test
-- Leodegario Pasakdal
--
-- COMMON
--
-- DML
-- Pre-Deployment
--
-- Update Portal
--
SET XACT_ABORT ON;
SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
GO
BEGIN TRANSACTION;
DECLARE @template_name varchar(60)
DECLARE @template_tag varchar(60)
DECLARE @template_title varchar(60)
DECLARE @pa_user_id int
DECLARE @next_skin_id int
DECLARE @next_template_id int

SET @template_name = N'Athens1'
SET @template_tag = N'Athens1'
SET @template_title = N'Athens1'
SET @pa_user_id = 17178082

DECLARE @insertThis_PortalSkin INT = (SELECT TOP 1 Portal_Skin_Id FROM Portal_Skins WHERE Skin_Name =    N'Athens1')
IF (@insertThis_PortalSkin IS NULL)
  BEGIN

    CREATE TABLE #tmp (id INT);
    INSERT INTO #tmp
    EXEC dbo.getTableSequenceNumbers N'Portal_Skins', 1;
    SELECT @next_skin_id = id FROM #tmp;
    DROP TABLE #tmp;

    INSERT INTO Portal_Skins
    ([Portal_Skin_Id]
      ,[Skin_Name]
      ,[Display_Tag]
      ,[Created_By]
      ,[Created_Date]
      ,[Last_Updated_By]
      ,[Last_Updated_Date])
    VALUES
      (@next_skin_id,
       @template_title,
       @template_tag,
       @pa_user_id,
       SYSUTCDATETIME(),
       @pa_user_id,
       SYSUTCDATETIME())

  END

DECLARE @insertThis_PortalTemplate INT = (SELECT TOP 1 Portal_Template_Id FROM Portal_Templates WHERE Template_Name = N'Athens1')
IF (@insertThis_PortalTemplate IS NULL)
  BEGIN

    CREATE TABLE #tmp2 (id INT);
    INSERT INTO #tmp2
    EXEC dbo.getTableSequenceNumbers N'Portal_Templates', 1;
    SELECT @next_template_id = id FROM #tmp2;
    DROP TABLE #tmp2;

    INSERT INTO Portal_Templates
    ([Portal_Template_Id]
      ,[Template_Name]
      ,[Page_Header_HTML]
      ,[Page_Footer_HTML]
      ,[Status]
      ,[Created_By]
      ,[Created_Date]
      ,[Last_Updated_By]
      ,[Last_Updated_Date]
      ,[Popup_Header_HTML]
      ,[Popup_Footer_HTML]
      ,[Window_Title]
      ,[Has_Frames]
      ,[Portal_Skin_Id]
      ,[Portal_URL])
    VALUES
      (@next_template_id,
       @template_name,
       null,
       null,
       1,
       @pa_user_id,
       SYSUTCDATETIME(),
       @pa_user_id,
       SYSUTCDATETIME(),
       null,
       null,
       @template_title,
       N'Y',
       @next_skin_id,
       null)

  END

COMMIT TRANSACTION;
GO

EXEC addDBUpdate N'2015-02-26_1424140604'
GO
